package org.lacitysan.landfill.server.persistence.dao.instrument;

import java.util.List;

import org.lacitysan.landfill.server.persistence.entity.instrument.InstrumentType;

/**
 * @author aquach
 */
public interface InstrumentTypeDao {
	
	InstrumentType getInstrumentTypeById(Integer id);

	List<InstrumentType> getAllInstrumentTypes();
	
	InstrumentType create(InstrumentType instrumentType);

	InstrumentType update(InstrumentType instrumentType);

	InstrumentType delete(InstrumentType instrumentType);

}
