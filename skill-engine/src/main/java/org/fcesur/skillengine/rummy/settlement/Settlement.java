package org.fcesur.skillengine.rummy.settlement;

import org.fcesur.skillengine.dto.PointSettlementInfo;
import org.fcesur.skillengine.dto.SettlementCurrencyInfo;

public interface Settlement {
    SettlementCurrencyInfo settlement(PointSettlementInfo info);
}
