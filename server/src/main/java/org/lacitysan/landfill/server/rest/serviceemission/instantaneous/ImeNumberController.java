package org.lacitysan.landfill.server.rest.serviceemission.instantaneous;

import java.util.List;

import org.lacitysan.landfill.server.config.app.ApplicationConstant;
import org.lacitysan.landfill.server.persistence.dao.serviceemission.instantaneous.ImeNumberDao;
import org.lacitysan.landfill.server.persistence.entity.serviceemission.instantaneous.ImeNumber;
import org.lacitysan.landfill.server.service.MonitoringPointService;
import org.lacitysan.landfill.server.service.serviceemission.instantaneous.ImeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alvin Quach
 */
@RequestMapping(ApplicationConstant.RESOURCE_PATH + "/ime-number")
@RestController
public class ImeNumberController {
	
	@Autowired
	ImeNumberDao imeNumbersDao;
	
	@Autowired
	ImeService imeService;
	
	// TODO Delete this.
	@Autowired
	MonitoringPointService monitoringPointService;
	
	@RequestMapping(value="/list/all", method=RequestMethod.GET)
	public List<ImeNumber> getAll() {
		return imeNumbersDao.getAll();
	}
	
	@RequestMapping(value="/list/site/{siteName}", method=RequestMethod.GET)
	public List<ImeNumber> getBySite(@PathVariable String siteName) {
		return imeNumbersDao.getBySiteAndDateCode(monitoringPointService.getSiteByName(siteName), null);
	}
	
	@RequestMapping(value="/unique/imeNumber/{imeNumber}", method=RequestMethod.GET)
	public ImeNumber getByImeNumber(@PathVariable String imeNumber) {
		ImeNumber temp = imeService.getImeNumberFromString(imeNumber);
		return imeNumbersDao.getByImeNumber(temp);
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public ImeNumber create(@RequestBody ImeNumber imeNumber) {
		return imeNumbersDao.create(imeNumber);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public ImeNumber update(@RequestBody ImeNumber imeNumber) {
		return imeNumbersDao.update(imeNumber);
	}

	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public ImeNumber delete(@RequestBody ImeNumber imeNumber) {
		return imeNumbersDao.delete(imeNumber);
	}

	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ImeNumber deleteById(@PathVariable String id) {
		try {
			ImeNumber imeNumber = new ImeNumber();
			imeNumber.setId(Integer.parseInt(id));
			return delete(imeNumber);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}

}