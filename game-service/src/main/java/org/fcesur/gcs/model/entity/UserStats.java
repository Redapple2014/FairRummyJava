package com.fairrummy.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Entity
@Table(name = "user_stats")
public class UserStats {
    private double skill_score;
    @Id
    private long user_id;

    public void setSkill(double skill) {
        this.skill_score = skill;
    }

    public double getSkill()
    {
        return skill_score;
    }
}
