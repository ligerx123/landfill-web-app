package org.lacitysan.landfill.server.rest.integrated;

import java.util.List;

import org.lacitysan.landfill.server.config.app.ApplicationConstant;
import org.lacitysan.landfill.server.persistence.dao.integrated.IntegratedDataDao;
import org.lacitysan.landfill.server.persistence.entity.integrated.IntegratedData;
import org.lacitysan.landfill.server.security.annotation.RestAllowSuperAdminOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alvin Quach
 */
@RequestMapping(ApplicationConstant.RESOURCE_PATH + "/integrated-data")
@RestController
public class IntegratedDataController {
	
	@Autowired
	IntegratedDataDao integratedDataDao;
	
	@RequestMapping(value="/{siteName}", method=RequestMethod.GET)
	@ResponseBody
	public List<IntegratedData> getBySite(@PathVariable String siteName) {
		return integratedDataDao.getBySite(siteName);
	}
	
	@RequestMapping(value="/{siteName}/{start}/{end}", method=RequestMethod.GET)
	@ResponseBody
	public List<IntegratedData> getBySiteAndDate(@PathVariable String siteName, @PathVariable Long start, @PathVariable Long end) {
		return integratedDataDao.getBySiteAndDate(siteName, start, end);
	}
	
	@RestAllowSuperAdminOnly
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public IntegratedData create(@RequestBody IntegratedData integratedData) {
		return integratedDataDao.create(integratedData);
	}
	
	@RestAllowSuperAdminOnly
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public IntegratedData update(@RequestBody IntegratedData integratedData) {
		return integratedDataDao.update(integratedData);
	}

	@RestAllowSuperAdminOnly
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public IntegratedData delete(@RequestBody IntegratedData integratedData) {
		return integratedDataDao.delete(integratedData);
	}
	
	@RestAllowSuperAdminOnly
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public IntegratedData deleteById(@PathVariable String id) {
		try {
			IntegratedData integratedData = new IntegratedData();
			integratedData.setId(Integer.parseInt(id));
			return delete(integratedData);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
}