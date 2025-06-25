package org.fcesur.gcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GroupDO {
    Integer minPlayers;
    Integer maxPlayers;
    String distribution;
}
