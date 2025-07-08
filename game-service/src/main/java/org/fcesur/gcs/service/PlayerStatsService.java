package com.fairrummy.service;

import com.fairrummy.mapper.PlayerStatsMapper;
import com.fairrummy.model.entity.UserStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PlayerStatsService {

    @Autowired
    private PlayerStatsMapper playerStatsMapper;

    @Transactional
    public boolean updateSkill(long playerId, double skill)
    {
        UserStats stat = playerStatsMapper.findById(playerId).orElse(null);
        if( stat != null )
        {
            stat.setSkill(skill);
            playerStatsMapper.save(stat);
        }
        else {
            stat = new UserStats(skill, playerId);
            playerStatsMapper.save(stat);
        }

        return true;
    }
}
