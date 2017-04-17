package org.lacitysan.landfill.server.persistence.dao.serviceemission.instantaneous;

import org.lacitysan.landfill.server.persistence.dao.serviceemission.ServiceEmissionExceedanceNumberDao;
import org.lacitysan.landfill.server.persistence.entity.serviceemission.instantaneous.ImeNumber;

public interface ImeNumberDao extends ServiceEmissionExceedanceNumberDao<ImeNumber> {

	ImeNumber getByImeNumber(ImeNumber imeNumber);

}