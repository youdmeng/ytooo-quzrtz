package com.ytooo.repository.dao;

import com.ytooo.repository.QuartzInfoEO;
import com.ytooo.repository.QuartzInfoEOPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartzInfoEODao  {

    List<QuartzInfoEO> queryByList(QuartzInfoEOPage quartzInfoEOPage);

}
