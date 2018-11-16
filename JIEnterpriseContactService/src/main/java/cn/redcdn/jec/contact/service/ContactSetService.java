package cn.redcdn.jec.contact.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.EntContact;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.contact.dao.ContactExtendDao;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
@Path("/contact/set")
public class ContactSetService extends BaseService {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;

    /**
     * ContactExtendDao
     */
    @Autowired
    private ContactExtendDao contactExtendDao;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto process(JSONObject params,
                               HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "id");
        tokenApiService.checkSystemLoginToken(params.getString("token"));

        String id = params.getString("id");
        contactExtendDao.deleteById(id);

        String contactStr = params.getString("contacts");
        if (StringUtils.isNotBlank(contactStr)) {
            String[] contacts = contactStr.split(",");
            List<EntContact> list = new ArrayList<>();
            for (String contact : contacts) {
                EntContact entContact = new EntContact();
                entContact.setDeviceId(id);
                entContact.setContactGroupId(contact);
                list.add(entContact);
            }
            contactExtendDao.insertBatch(list);
        }

        return new ResponseDto();
    }
}
