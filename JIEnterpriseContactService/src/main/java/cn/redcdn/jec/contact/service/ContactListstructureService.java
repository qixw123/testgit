package cn.redcdn.jec.contact.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.ContactApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ContactStructureDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;

@Path("/contact/liststructure")
public class ContactListstructureService extends BaseService<List<ContactStructureDto>> {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;    

    /**
     * ContantApiService
     */
    @Autowired
    private ContactApiService contactApiService;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<List<ContactStructureDto>> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token");
        tokenApiService.checkUserLoginToken(params.getString("token"));
        
        String nube = tokenApiService.getCreatorByToken(params.getString("token"));
        List<ContactStructureDto> csList = contactApiService.getContactStructure(nube);
        ResponseDto<List<ContactStructureDto>> rspDto = new ResponseDto<List<ContactStructureDto>>();
        rspDto.setData(csList);
        
        return rspDto;
    }
}
