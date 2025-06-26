package org.fcesur.skillengine.rummy.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDetails {
    private long channelId;
    private int serviceId;
}
