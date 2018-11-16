package cn.redcdn.jec.common.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.redcdn.jec.common.dao.ContactCommonDao;
import cn.redcdn.jec.common.dto.ContactDetailDto;
import cn.redcdn.jec.common.dto.ContactStructureDto;
import cn.redcdn.jec.common.entity.SystemAdmin;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.user.dao.AdminLoginDao;

@Service
public class ContactApiService  {

    /**
     * ContactExtendDao
     */
    @Autowired
    private ContactCommonDao contactCommonDao;

    /**
     * AdminLoginDao
     */
    @Autowired
    private AdminLoginDao adminLoginDao;
    
	/**
	 * 设置用户token
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public List<ContactStructureDto> getContactStructure(String nube) {
		int count  = contactCommonDao.countContactsByNube(nube);
		List<ContactDetailDto> detailList = null;
		if (count > 0) {
			detailList = contactCommonDao.listGroupByNube(nube);
		} else {
			detailList = contactCommonDao.listGroupDefaultByNube(nube);
		}
		ContactStructureDto dto = new ContactStructureDto();
		ContactStructureDto firstDto = null;
		ContactStructureDto secondDto = null;
		ContactStructureDto childDto = null;
		if (detailList != null) {
			for (ContactDetailDto detail : detailList) {
				if (detail.getTlevel() == Constants.GROUP_FIRST) {
					// 取第一级分组
					childDto = new ContactStructureDto();
					childDto.setId(detail.getTid());
					childDto.setName(detail.getTname());
					findContactStructure(dto, childDto);
				} else if (detail.getTlevel() == Constants.GROUP_SECOND) {
					// 取第一级分组
					childDto = new ContactStructureDto();
					childDto.setId(detail.getSid());
					childDto.setName(detail.getSname());
					firstDto = findContactStructure(dto, childDto);
					
					// 取第二级分组
					childDto = new ContactStructureDto();
					childDto.setId(detail.getTid());
					childDto.setName(detail.getTname());
					findContactStructure(firstDto, childDto);
				} else if (detail.getTlevel() == Constants.GROUP_THIRD) {
					// 取第一级分组
					childDto = new ContactStructureDto();
					childDto.setId(detail.getFid());
					childDto.setName(detail.getFname());
					firstDto = findContactStructure(dto, childDto);
					
					// 取第二级分组
					childDto = new ContactStructureDto();
					childDto.setId(detail.getSid());
					childDto.setName(detail.getSname());
					secondDto = findContactStructure(firstDto, childDto);
					
					// 取第三级分组
					childDto = new ContactStructureDto();
					childDto.setId(detail.getTid());
					childDto.setName(detail.getTname());
					findContactStructure(secondDto, childDto);
				}
			}
		}
		
		List<ContactStructureDto> allList = new ArrayList<ContactStructureDto>();
		ContactStructureDto allDto = new ContactStructureDto();
		SystemAdmin adminInfo = adminLoginDao.getSystemAdminInfoByNubeOrAccount(nube, null);
		if (adminInfo != null) {
			allDto.setName(adminInfo.getName());
		} else {
			allDto.setName("通讯录");
		}
		allDto.setGroups(dto.getGroups());
		allList.add(allDto);
		return allList;
	}
	
	public ContactStructureDto findContactStructure(
			ContactStructureDto parentDto, ContactStructureDto childDto) {
		List<ContactStructureDto> groups = parentDto.getGroups();
		ContactStructureDto resultDto = new ContactStructureDto();
		if (groups == null) {
			groups = new ArrayList<ContactStructureDto>();
			parentDto.setGroups(groups);
		}
		int index = groups.indexOf(childDto);
		if (index == -1) {
			BeanUtils.copyProperties(childDto, resultDto);
			groups.add(resultDto);
		} else {
			resultDto =  groups.get(index);
		}
		
		return resultDto;
	}
}
