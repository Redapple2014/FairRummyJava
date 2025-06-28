package com.skillengine.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.skillengine.dao.mapper.UserStatMapper;
import com.skillengine.dao.model.GameEndDetails;
import com.skillengine.db.config.DataSource;
import com.skillengine.rummy.util.SkillCalculation;

public class UserStatsDAO
{
	public void upsertGameEndDetails( List< GameEndDetails > endDetails, long tableId )
	{
		try (SqlSession session = DataSource.getSqlSessionFactory().openSession( true ))
		{
			UserStatMapper mapper = session.getMapper( UserStatMapper.class );
			for( GameEndDetails gameEndDetails : endDetails )
			{
				System.out.println( "gameEndDetails" + gameEndDetails );
				GameEndDetails updatedDetails = mapper.upsertGameStats( gameEndDetails );
				double skillScore = SkillCalculation.calculateSkillScore( updatedDetails );
				skillScore = skillScore <= 0 ? 1 : skillScore;
				mapper.updateSkillScore( gameEndDetails.getUserId(), skillScore );
				System.out.println( "updatedDetails" + updatedDetails );
				System.out.println( "$$$" + skillScore );
			}
		}
	}
}
