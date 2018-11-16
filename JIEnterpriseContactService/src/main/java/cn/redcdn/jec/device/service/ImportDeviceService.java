package cn.redcdn.jec.device.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.*;
import cn.redcdn.jec.common.dao.CacheDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.exception.ExternalException;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.*;
import cn.redcdn.jec.common.util.DateUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.device.dto.ImportDeviceDataDto;
import cn.redcdn.jec.device.dto.ImportDeviceDto;
import cn.redcdn.jec.group.dao.GroupExtendDao;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 导入设备信息
 *
 * @author zhang
 */
@Path("/device/import")
public class ImportDeviceService extends BaseService<ImportDeviceDto> {

    private static Logger logger = LoggerFactory.getLogger(ImportDeviceService.class);

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    UserCenterApiService userCenterApiService;

    @Autowired
    DeviceApiService deviceApiService;

    @Autowired
    CacheDao cacheDao;

    @Autowired
    DeviceExdDao deviceExtendDao;

    @Autowired
    FileApiService fileApiService;

    @Autowired
    GroupExtendDao groupExtendDao;

    @Autowired
    GroupApiService groupApiService;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    // xls格式的excel文件
    private static final String EXCEL_XLS = "xls";
    // xlsx格式的excel文件
    private static final String EXCEL_XLSX = "xlsx";
    // 每行数据量
    private static final int ROW_CELL_NUMBER = 4;

    @Override
    public ResponseDto<ImportDeviceDto> process(JSONObject params, HttpServletRequest request,
                                                HttpServletResponse response) {

        MultipartHttpServletRequest multipartRequest = null;
        MultipartFile file = null;
        if (request instanceof MultipartHttpServletRequest) {
            multipartRequest = (MultipartHttpServletRequest) request;
            // 获导入得文件
            file = multipartRequest.getFile("deviceData");
        }

        String token = request.getParameter("token");
        // 必须check
        if (StringUtils.isBlank(token)) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("params.empty");
            String message = messageInfo.getMsg();
            message = MessageFormat.format(message, "token");
            messageInfo.setMsg(message);
            throw new ExternalServiceException(messageInfo);
        }
        // 请求的token带上appKey时，把appKey去掉
        String[] strArr = token.split("_");
        token = strArr[0];
        tokenApiService.checkSystemLoginToken(token);
        // 导入的管理员
        String importer = baseInfoUtil.getImporterByToken(token);

        // 判断上传文件是否为空
        if (file == null) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("file.upload.error");
            throw new ExternalServiceException(messageInfo);
        }

        // 读取文件的设备数据
        List<ImportDeviceDataDto> fileDataList = readFile(file);
        // 待导入的数据总量 用于返回失败数
        int totalDataNum = fileDataList.size();
        int success = 0;
        String url = "";
        if (totalDataNum > 0) {
            // 错误数据列表
            List<ImportDeviceDataDto> errorDataList = new ArrayList<ImportDeviceDataDto>();
            // 检查读取的文件的数据是否合法,不合法数据加入错误数据列表
            checkDataList(fileDataList, errorDataList);
            // 初始化group_info
            groupApiService.initGroupCache(importer);
            // 将数据插入Db中
            success = insertData(fileDataList, importer);
            // 将错误数据写到txt中，并返回可访问地址
            url = writeToFile(errorDataList, request);
        }

        ImportDeviceDto dto = new ImportDeviceDto();
        dto.setSuccess(success);
        dto.setFailure(totalDataNum - success);
        dto.setUrl(url);
        ResponseDto<ImportDeviceDto> rspDto = new ResponseDto<ImportDeviceDto>();
        rspDto.setData(dto);
        return rspDto;
    }

    /**
     * 将错误数据写到txt中，并返回可访问地址
     *
     * @param errorDataList
     * @param request
     * @return
     */
    private String writeToFile(List<ImportDeviceDataDto> errorDataList, HttpServletRequest request) {
        String url = "";
        if (errorDataList.size() > 0) {
            String data = StringUtil.join(errorDataList, "\n");
            String fileName = DateUtil.getDateyyyyMMddHHmmss(new Date()) + ".txt";
            String filePath = request.getSession().getServletContext().getRealPath("") + "errordata"
                    + File.separator + fileName;
            fileApiService.writeTXT(filePath, data);

            // 返回可访问的URL
            url = String.format("http://%s:%s/jec/errordata/%s",
                    request.getServerName(), request.getServerPort(), fileName);
        }

        return url;
    }

    /**
     * 向Db插入合法的设备数据
     *
     * @param fileDataList
     * @param importer
     */
    private int insertData(List<ImportDeviceDataDto> fileDataList, String importer) {
        // 插入组织
        insertGroup(fileDataList, importer);

        int insNum = 0;
        // 待插入设备信息
        List<Device> insDeviceList = new ArrayList<Device>();
        for (ImportDeviceDataDto data : fileDataList) {
            Device device = new Device();
            BeanUtils.copyProperties(data, device);
            device.setName(cacheDao.get("name_" + data.getNube()));
            device.setAccount(cacheDao.get("account_" + data.getNube()));
            device.setAccountType(Integer.parseInt(cacheDao.get("account_type_" + data.getNube())));
            // 设置中控标识
            if (device.getAccountType() != null
                    && device.getAccountType() == Constants.ACCOUNT_TYPE_TP) {
                device.setControlFlg(Constants.DISABLED);
            }
            device.setImporter(importer);
            device.setCreator(importer);
            device.setGroupId(getDeviceGroupId(device));
            insDeviceList.add(device);
        }
        if (insDeviceList.size() > 0) {
            insNum = deviceExtendDao.insertBatch(insDeviceList);
        }

        return insNum;
    }

    /**
     * 获取设备的直属组织
     *
     * @param device
     * @return
     */
    private String getDeviceGroupId(Device device) {
        String groupId = "";
        try {
            String firstGroupName = device.getFirstGroup();
            String importer = device.getImporter();
            String firstGroupId = groupApiService.getGroupIdByNameLevelParent(importer, firstGroupName,
                    Constants.FIRST_LEVEL, "");

            if (StringUtil.isNotBlank(device.getSecondGroup())) {
                String secondGroupName = device.getSecondGroup();
                String secondGroupId = groupApiService.getGroupIdByNameLevelParent(importer, secondGroupName,
                        Constants.SECOND_LEVEL, firstGroupId);

                if (StringUtil.isNotBlank(device.getThirdGroup())) {
                    String thirdGroupName = device.getThirdGroup();
                    String thirdGroupId = groupApiService.getGroupIdByNameLevelParent(importer, thirdGroupName,
                            Constants.THIRD_LEVEL, secondGroupId);
                    groupId = thirdGroupId;
                } else {
                    groupId = secondGroupId;
                }
            } else {
                groupId = firstGroupId;
            }
        } catch (ExternalException e) {
            logger.info(device.getAccount() + "没有直属组织...");
        }

        return groupId;
    }

    /**
     * 插入group表
     *
     * @param fileDataList
     * @param importer
     */
    private void insertGroup(List<ImportDeviceDataDto> fileDataList, String importer) {
        List<String> firstGroupNameList = new ArrayList<String>();
        List<String> secondGroupNameList = new ArrayList<String>();
        List<String> thirdGroupNameList = new ArrayList<String>();
        // key:子级组名 value:父级组名
        Map<String, String> parentMap = new HashMap<String, String>();
        // 去掉重复的组名
        for (ImportDeviceDataDto data : fileDataList) {
            if (!firstGroupNameList.contains(data.getFirstGroup())) {
                firstGroupNameList.add(data.getFirstGroup());
            }
            if (StringUtil.isNotBlank(data.getSecondGroup())
                    && !secondGroupNameList.contains(data.getFirstGroup() + "_" + data.getSecondGroup())) {
                secondGroupNameList.add(data.getFirstGroup() + "_" + data.getSecondGroup());
                parentMap.put(data.getSecondGroup(), data.getFirstGroup());
                Collections.sort(secondGroupNameList);
            }
            if (StringUtil.isNotBlank(data.getThirdGroup())
                    && !thirdGroupNameList.contains(data.getFirstGroup() + "_" + data.getSecondGroup() + "_" + data.getThirdGroup())) {
                thirdGroupNameList.add(data.getFirstGroup() + "_" + data.getSecondGroup() + "_" + data.getThirdGroup());
                parentMap.put(data.getThirdGroup(), data.getSecondGroup());
                Collections.sort(thirdGroupNameList);
            }
        }

        // 插入一级group
        groupApiService.insertLevelGroup(firstGroupNameList, Constants.FIRST_LEVEL, importer);

        // 插入二级group
        groupApiService.insertLevelGroup(secondGroupNameList, Constants.SECOND_LEVEL, importer);

        // 插入三级group
        groupApiService.insertLevelGroup(thirdGroupNameList, Constants.THIRD_LEVEL, importer);
    }

    /**
     * 校验读取的文件数据，返回错误数据列表
     *
     * @param fileDataList
     * @param errorDataList
     */
    private List<ImportDeviceDataDto> checkDataList(List<ImportDeviceDataDto> fileDataList, List<ImportDeviceDataDto> errorDataList) {
        // 所有的数据库视讯号列表 + 符合条件的待导入视讯号
        List<String> nubeList = deviceExtendDao.queryAllNube();
        // 去用户中心查询信息的视讯号列表
        List<String> searchNubeList = new ArrayList<String>();
        Iterator<ImportDeviceDataDto> iterator = fileDataList.iterator();
        while (iterator.hasNext()) {
            ImportDeviceDataDto data = iterator.next();
            // 校验组结构
            // 一级组名不能为空
            if (StringUtil.isBlank(data.getFirstGroup())) {
                data.setReason("一级组名不能为空。");
                errorDataList.add(data);
                iterator.remove();
                continue;
            }
            // 下级组名不为空 上级组织不能空
            if (StringUtil.isNotBlank(data.getThirdGroup()) && StringUtil.isBlank(data.getSecondGroup())) {
                data.setReason("下级组名不为空，上级组织不能空。");
                errorDataList.add(data);
                iterator.remove();
                continue;
            }
            // 校验nube号
            // 是否为空
            if (StringUtil.isBlank(data.getNube())) {
                data.setReason("视讯号不能为空。");
                errorDataList.add(data);
                iterator.remove();
                continue;
            }
            // 是否重复 1、是否与数据库数据重复 2、导入的数据内部是否重复
            if (nubeList.contains(data.getNube())) {
                data.setReason("视讯号已存在。");
                errorDataList.add(data);
                iterator.remove();
                continue;
            }
            // 是否有效
            // 1、是否有缓存
            if (StringUtil.isBlank(cacheDao.get("account_type_" + data.getNube()))) {
                // 2、加入个人用户中心查询的list中
                searchNubeList.add(data.getNube());
            }
            nubeList.add(data.getNube());
        }
        // 批量查询用户，去掉有效的视讯号返回无效列表
        batchCheckNubeList(searchNubeList);
        if (searchNubeList.size() > 0) {
            iterator = fileDataList.iterator();
            while (iterator.hasNext()) {
                ImportDeviceDataDto data = iterator.next();
                if (searchNubeList.contains(data.getNube())) {
                    data.setReason("视讯号无效。");
                    errorDataList.add(data);
                    iterator.remove();
                }
            }
        }

        return errorDataList;
    }

    private void batchCheckNubeList(List<String> searchNubeList) {
        if (searchNubeList.size() > 0) {
            // 批量查询用户信息
            JSONObject resultObj = userCenterApiService.searchAccount(searchNubeList);
            // 有效视讯号列表
            List<String> validNubeList = new ArrayList<String>();
            if (resultObj != null && resultObj.getInteger("status") == 0) {
                JSONArray dataArray = resultObj.getJSONArray("users");
                if (dataArray != null && dataArray.size() > 0) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONObject userObj = dataArray.getJSONObject(i);
                        validNubeList.add(userObj.getString("nubeNumber"));
                        // 缓存用户信息
                        // 注：单个查询和批量查询返回的nickname拼写不同
                        userObj.put("nickname", userObj.getString("nickName"));
                        deviceApiService.cacheInfo(userObj);
                    }
                }
                searchNubeList.removeAll(validNubeList);
            }
        }
    }

    /**
     * 读取上传的excel文件
     *
     * @param file
     * @return
     */
    private List<ImportDeviceDataDto> readFile(MultipartFile file) {
        List<ImportDeviceDataDto> list = new ArrayList<>();
        String str = null;
        ImportDeviceDataDto dataDto = null;
        Workbook wb = null;
        int count = 0;
        boolean breakFlg = false;
        try {
            if (file.getOriginalFilename().endsWith(EXCEL_XLS)) {// Excel 2003
                wb = new HSSFWorkbook(file.getInputStream());
            } else if (file.getOriginalFilename().endsWith(EXCEL_XLSX)) {
                // Excel 2007/2010及以后
                wb = new XSSFWorkbook(file.getInputStream());
            }
            Sheet sheet = wb.getSheetAt(0);
            // 遍历excel所有行数据
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                count = 0;
                // 排除第一行head数据
                if (i == 0) {
                    continue;
                }
                Row row = sheet.getRow(i);
                if (row == null) {
                    breakFlg = true;
                    logger.info("excel中有空行");
                } else {
                    dataDto = new ImportDeviceDataDto();
                    for (int j = 0; j < ROW_CELL_NUMBER; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            str = StringUtil.EMPTY;
                        } else {
                            cell.setCellType(CellType.STRING);
                            str = cell.getStringCellValue().trim();
                        }
                        if (StringUtils.isBlank(str)) {
                            count++;
                        }
                        // 一行数据量为空，结束读取
                        if (j == ROW_CELL_NUMBER - 1 && count == ROW_CELL_NUMBER) {
                            breakFlg = true;
                            break;
                        }
                        switch (j) {
                            case 0:
                                dataDto.setNube(str);
                                break;
                            case 1:
                                dataDto.setFirstGroup(str);
                                break;
                            case 2:
                                dataDto.setSecondGroup(str);
                                break;
                            case 3:
                                dataDto.setThirdGroup(str);
                                break;
                        }
                    }
                }
                if (breakFlg) {
                    break;
                }
                list.add(dataDto);
            }
            return list;
        } catch (Exception e) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("file.parse.error");
            throw new ExternalServiceException(messageInfo);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("file.parse.error");
                    throw new ExternalServiceException(messageInfo);
                }
            }
        }
    }

}
