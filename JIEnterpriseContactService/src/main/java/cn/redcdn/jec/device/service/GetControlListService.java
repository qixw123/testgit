package cn.redcdn.jec.device.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.redcdn.jec.common.util.BaseInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.device.dto.DeviceListDto;
import cn.redcdn.jec.device.dto.DeviceListPageDto;
import cn.redcdn.jec.device.dto.SearchDevicePageDto;

/**
 * 获取中控列表
 *
 * @author zhang
 */
@Path("/control/list")
public class GetControlListService extends BaseService<DeviceListPageDto> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    DeviceExdDao deviceExdDao;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<DeviceListPageDto> process(JSONObject params, HttpServletRequest request,
                                                  HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "pageNo", "pageSize");
        // token校验
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String tokenType = tokenApiService.getCreatorByToken(token + "_type");
        String importer = baseInfoUtil.getImporterByToken(token);

        SearchDevicePageDto pageDto = JSONObject.parseObject(params.toJSONString(), SearchDevicePageDto.class);
        pageDto.setImporter(importer);
        pageDto.setGroupId(tokenApiService.getCreatorByToken(token + "_group"));

        List<DeviceListDto> controlList = new ArrayList<>();
        if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
            controlList = deviceExdDao.getControlListWithPage(pageDto);
        } else if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            controlList = deviceExdDao.getControlListWithOrgWithPage(pageDto);
        }

        // 参数获取
        DeviceListPageDto deviceListPageDto = new DeviceListPageDto();
        format(controlList);

        deviceListPageDto.setTotalSize(pageDto.getTotalSize());
        deviceListPageDto.setDeviceList(controlList);

        ResponseDto<DeviceListPageDto> rspDto = new ResponseDto<>();
        rspDto.setData(deviceListPageDto);
        return rspDto;
    }

    private void format(List<DeviceListDto> deviceList) {
        for (DeviceListDto dto : deviceList) {
            if (StringUtil.isBlank(dto.getSecondGroup())) {
                dto.setSecondGroup(Constants.STR_CONCAT_DASH);
            }
            if (StringUtil.isBlank(dto.getThirdGroup())) {
                dto.setThirdGroup(Constants.STR_CONCAT_DASH);
            }
        }
    }
}
