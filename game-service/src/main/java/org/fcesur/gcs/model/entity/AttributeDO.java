package org.fcesur.gcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AttributeDO {
    String name;
    String type;
    boolean shardKey;
}
