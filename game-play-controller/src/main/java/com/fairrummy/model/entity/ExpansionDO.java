package com.fairrummy.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class ExpansionDO {
    String target;
    List<ExpansionStep> steps;

    public void addStep(ExpansionStep step)
    {
        if( steps == null )
            steps = new ArrayList<>();
        steps.add(step);
    }
}
