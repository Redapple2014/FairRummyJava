package com.skillengine.service;

import com.skillengine.dto.BoardJoinDetails;
import com.skillengine.dto.CurrencyDetails;
import com.skillengine.dto.ExitDetails;

public interface CurrencyService {
    CurrencyDetails debit(BoardJoinDetails boardJoinDetails);

    CurrencyDetails credit(ExitDetails ExitDetails);

}
