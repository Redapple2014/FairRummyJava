package com.fairrummy.mapper;

import com.fairrummy.model.entity.Template;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface GameTemplateMapper extends JpaRepository<Template, Integer> {
}
