package com.skillengine.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.skillengine.dao.model.GameEndDetails;

public interface UserStatMapper
{
	@Select( "Insert into user_stats(user_id,total_rake,total_entry_fee,total_games,winners_cnt,dropped_count,winning_amt,losing_amt,created_at,updated_on) values(#{userId},#{rake},#{entryFee},#{gamecnt},#{winnerOrNot},#{droppedOrNot},#{winningAmt},#{losingAmt},now(),now()) ON CONFLICT(user_id) DO UPDATE SET total_rake = user_stats.total_rake + EXCLUDED.total_rake ,total_entry_fee = user_stats.total_entry_fee + EXCLUDED.total_entry_fee,total_games = user_stats.total_games + EXCLUDED.total_games,winners_cnt = user_stats.winners_cnt + EXCLUDED.winners_cnt,dropped_count = user_stats.dropped_count + EXCLUDED.dropped_count,winning_amt = user_stats.winning_amt + EXCLUDED.winning_amt, losing_amt =user_stats. losing_amt + EXCLUDED.losing_amt,updated_on = now() RETURNING total_rake,total_entry_fee,total_games,winners_cnt,dropped_count,winning_amt,losing_amt" )
	@Results( { @Result( property = "entry_fee", column = "total_entry_fee" ), @Result( property = "rake", column = "total_rake" ), @Result( property = "winningAmt", column = "winning_amt" ),
			@Result( property = "losingAmt", column = "losing_amt" ), @Result( property = "totalWinningGames", column = "winners_count" ),
			@Result( property = "totalDroppedGames", column = "dropped_count" ), @Result( property = "totalGames", column = "total_games" ) } )
	GameEndDetails upsertGameStats( GameEndDetails gameEndDetails );

	@Update( "update user_stats set updated_on = now(),skill_score = skill_score + #{score} where user_id = #{userId}" )
	void updateSkillScore( @Param( "userId" ) Long userId, @Param( "score" ) Double score );
}
