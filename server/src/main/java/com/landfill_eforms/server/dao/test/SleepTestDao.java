package com.landfill_eforms.server.dao.test;

import com.landfill_eforms.server.entities.test.Sleep;
import com.landfill_eforms.server.entities.test.Test;

public interface SleepTestDao {

	Sleep getSleepById(Integer id);

	Test getTestById(Integer id);

}
