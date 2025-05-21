package com.fairrummy.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrationDO {
    String tenantId;
    String shardingId;
    Integer interval;
    String communication;

    List<AttributeDO> staticAttributes;
    List<AttributeDO> dynamicAttributes;
    Integer pollIntervalMs;
    GroupDO grouping;
    List<ExpansionDO> expansions;

    public void addExpansion(ExpansionDO expansionDO) {
        if(expansions == null)
            expansions = new ArrayList<>();
        expansions.add(expansionDO);
    }
}