package org.lacitysan.landfill.server.persistence.dao.surfaceemission.instantaneous;

import org.hibernate.Hibernate;
import org.lacitysan.landfill.server.persistence.dao.surfaceemission.SurfaceEmissionExceedanceNumberDaoImpl;
import org.lacitysan.landfill.server.persistence.entity.surfaceemission.instantaneous.ImeNumber;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implemented data access object for <code>ImeNumber</code> entities.
 * @author Alvin Quach
 */
@Repository
public class ImeNumberDaoImpl extends SurfaceEmissionExceedanceNumberDaoImpl<ImeNumber> implements ImeNumberDao {

	@Override
	@Transactional
	public ImeNumber update(ImeNumber imeNumber) {
		hibernateTemplate.update(imeNumber);
		return imeNumber;
	}

	@Override
	@Transactional
	public ImeNumber getByImeNumber(ImeNumber imeNumber) {
		return getByExceedanceNumber(imeNumber);
	}

	@Override
	public ImeNumber initialize(ImeNumber imeNumber) {
		if (imeNumber == null) {
			return null;
		}
		Hibernate.initialize(imeNumber.getMonitoringPoints());
		imeNumber.getInstantaneousData().forEach(instantaneousData -> {
			Hibernate.initialize(instantaneousData);
		});
		imeNumber.getUnverifiedInstantaneousData().forEach(unverifiedInstantaneousData -> {
			Hibernate.initialize(unverifiedInstantaneousData.getUnverifiedDataSet());
		});
		imeNumber.getImeData().forEach(imeData -> {
			imeData.getImeRepairData().forEach(imeRepairData -> {
				Hibernate.initialize(imeRepairData);
			});
		});
		return imeNumber;
	}

}
