package com.fairrummy.mapper;

import com.fairrummy.model.entity.UserStats;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PlayerStatsMapper extends JpaRepository<UserStats, Long> {
}

