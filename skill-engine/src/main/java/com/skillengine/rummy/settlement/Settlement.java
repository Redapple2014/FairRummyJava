package com.skillengine.rummy.settlement;

import com.skillengine.dto.PointSettlementInfo;
import com.skillengine.dto.SettlementCurrencyInfo;

public interface Settlement
{
	SettlementCurrencyInfo settlement( PointSettlementInfo info );
}
