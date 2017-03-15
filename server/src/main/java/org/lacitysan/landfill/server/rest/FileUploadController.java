package org.lacitysan.landfill.server.rest;

import java.util.Map;
import java.util.Set;

import org.lacitysan.landfill.server.config.constant.ApplicationProperty;
import org.lacitysan.landfill.server.persistence.dao.instantaneous.ImeNumberDao;
import org.lacitysan.landfill.server.persistence.dao.unverified.UnverifiedDataSetDao;
import org.lacitysan.landfill.server.persistence.dao.user.UserDao;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.ImeNumber;
import org.lacitysan.landfill.server.persistence.entity.unverified.UnverifiedDataSet;
import org.lacitysan.landfill.server.persistence.entity.unverified.UnverifiedInstantaneousData;
import org.lacitysan.landfill.server.persistence.entity.user.User;
import org.lacitysan.landfill.server.service.mobile.MobileDataDeserializer;
import org.lacitysan.landfill.server.service.mobile.model.MobileDataContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping(ApplicationProperty.RESOURCE_PATH + "/file")
@RestController
public class FileUploadController {

	@Autowired
	MobileDataDeserializer mobileDataDeserializer;
	
	@Autowired
	UnverifiedDataSetDao unverifiedDataSetDao;
	
	@Autowired
	ImeNumberDao imeNumberDao;
	
	@Autowired
	UserDao userDao;

	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public Object testUpload(@RequestBody MultipartFile file) {
		System.out.println("FILE SIZE: " + file.getSize());
		ObjectMapper mapper = new ObjectMapper();
		try {
			MobileDataContainer rawData = mapper.readValue(file.getBytes(), new TypeReference<MobileDataContainer>(){});

			Set<UnverifiedDataSet> dataSets = mobileDataDeserializer.deserializeData(rawData);
			
			// Get the current user from the security context holder, so we can update the 'modified by' and/or the 'created by' fields.
			User user = new User();
			Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principle instanceof Map) {
				Integer id = (Integer)((Map<?, ?>)principle).get("id");
				if (id != null) {
					user.setId(id);
				}
			}
			
			for (UnverifiedDataSet dataSet : dataSets) {
				dataSet.setFilename(file.getOriginalFilename());
				
				for (UnverifiedInstantaneousData unverifiedInstantaneousData : dataSet.getUnverifiedInstantaneousData()) {
					
					for (ImeNumber imeNumber : unverifiedInstantaneousData.getImeNumbers()) {
						if (imeNumber.getId() != null) {
							continue;
						}
						Object id = imeNumberDao.create(imeNumber);
						if (id instanceof Integer) {
							imeNumber.setId((Integer)id);
						}
					}
					
				}
				
				Object result = unverifiedDataSetDao.create(dataSet);
				if (result instanceof Integer) {
					if (user.getId() != null) {
						dataSet.setInspector(user);
					}
					dataSet.setId((Integer)result);
				}
			}
			
			return dataSets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
