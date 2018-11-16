package cn.redcdn.jec.device.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.BaseInfoUtil;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.device.dto.DeviceListDto;
import cn.redcdn.jec.device.dto.DeviceListPageDto;
import cn.redcdn.jec.device.dto.SearchDevicePageDto;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 获取设备列表 只能查看自己创建的设备
 *
 * @author zhang
 */
@Path("/device/list")
public class GetDeviceListService extends BaseService<DeviceListPageDto> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Autowired
    DeviceExdDao deviceExdDao;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Override
    public ResponseDto<DeviceListPageDto> process(JSONObject params, HttpServletRequest request,
                                                  HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "pageNo", "pageSize");
        // token校验
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);
        String importer = baseInfoUtil.getImporterByToken(token);
        String tokenType = baseInfoUtil.getTokenTypeByToekn(token);

        SearchDevicePageDto pageDto = JSONObject.parseObject(params.toJSONString(), SearchDevicePageDto.class);
        pageDto.setImporter(importer);

        List<DeviceListDto> deviceList = new ArrayList<>();
        if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            String account = tokenApiService.getCreatorByToken(token);
            OrganizeAccount organizeAccount = organizeAccountExtDao.selectInfoByAccount(account);
            if (organizeAccount == null) return new ResponseDto<>();
            pageDto.setGroupId(organizeAccount.getContactGroupId());
            deviceList = deviceExdDao.getDeviceListWitOrgWithPage(pageDto);
        } else if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
            deviceList = deviceExdDao.getDeviceListWithPage(pageDto);
        }

        // 参数获取
        DeviceListPageDto deviceListPageDto = new DeviceListPageDto();
        format(deviceList);

        deviceListPageDto.setTotalSize(pageDto.getTotalSize());
        deviceListPageDto.setDeviceList(deviceList);

        ResponseDto<DeviceListPageDto> rspDto = new ResponseDto<>();
        rspDto.setData(deviceListPageDto);
        return rspDto;
    }

    private void format(List<DeviceListDto> deviceList) {
        for (DeviceListDto dto : deviceList) {
            if (StringUtil.isBlank(dto.getControlFlg())) {
                dto.setControlFlg(Constants.STR_CONCAT_DASH);
            }
            if (StringUtil.isBlank(dto.getSecondGroup())) {
                dto.setSecondGroup(Constants.STR_CONCAT_DASH);
            }
            if (StringUtil.isBlank(dto.getThirdGroup())) {
                dto.setThirdGroup(Constants.STR_CONCAT_DASH);
            }
        }
    }

}
