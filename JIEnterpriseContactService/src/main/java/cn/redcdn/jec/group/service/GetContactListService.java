package cn.redcdn.jec.group.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.redcdn.jec.common.util.BaseInfoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.ContactApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ContactStructureDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.group.dto.ContactInfoDto;
import cn.redcdn.jec.group.dto.ContactListPageDto;
import cn.redcdn.jec.group.dto.SearchContactPageDto;

/**
 * 获取分组联系人列表
 *
 * @author zhang
 */
@Path("/group/contactlist")
public class GetContactListService extends BaseService<ContactListPageDto> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    DeviceExdDao deviceExdDao;

    @Autowired
    private ContactApiService contactApiService;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<ContactListPageDto> process(JSONObject params, HttpServletRequest request,
                                                   HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "pageNo", "pageSize");
        // token校验
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        // 参数获取
        ResponseDto<ContactListPageDto> rspDto = new ResponseDto<ContactListPageDto>();
        ContactListPageDto contactListPageDto = new ContactListPageDto();
        List<ContactInfoDto> contacts = new ArrayList<ContactInfoDto>();
        SearchContactPageDto pageDto = JSONObject.parseObject(params.toJSONString(), SearchContactPageDto.class);

        String id = params.getString("id");
        if (StringUtils.isNotBlank(id)) {
            String importer = baseInfoUtil.getImporterByToken(token);
            pageDto.setImporter(importer);
            pageDto.setGroupId(params.getString("id"));
            // 分页查询联系人列表
            contacts = deviceExdDao.getContactListWithPage(pageDto);
            format(contacts);
        }

        contactListPageDto.setTotalSize(pageDto.getTotalSize());
        contactListPageDto.setContacts(contacts);

        rspDto.setData(contactListPageDto);
        return rspDto;
    }

    private void format(List<ContactInfoDto> contacts) {
        for (ContactInfoDto dto : contacts) {
            if (StringUtil.isNotBlank(dto.getControlFlg())
                    && Integer.parseInt(dto.getControlFlg()) == Constants.ENABLED) {
                List<ContactStructureDto> csList = contactApiService.getContactStructure(dto.getNube());
                dto.setStructures(csList);
            }
        }

    }
}
