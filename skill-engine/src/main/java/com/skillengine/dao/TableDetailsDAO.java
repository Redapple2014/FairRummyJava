package com.skillengine.dao;

import org.apache.ibatis.session.SqlSession;

import com.skillengine.dao.mapper.TableDetailsMapper;
import com.skillengine.dao.model.TableDetails;
import com.skillengine.db.config.DataSource;

public class TableDetailsDAO
{
	public Long insertTableDetails( TableDetails tableDetails )
	{
		try (SqlSession session = DataSource.getSqlSessionFactory().openSession( true ))
		{
			TableDetailsMapper mapper = session.getMapper( TableDetailsMapper.class );
			mapper.insertTableDetails( tableDetails );
			return tableDetails.getTableId();
		}
	}
}
