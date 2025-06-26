package org.fcesur.skillengine.service;

import org.fcesur.skillengine.dto.BoardJoinDetails;
import org.fcesur.skillengine.dto.CurrencyDetails;
import org.fcesur.skillengine.dto.ExitDetails;

public interface CurrencyService {
    CurrencyDetails debit(BoardJoinDetails boardJoinDetails);

    CurrencyDetails credit(ExitDetails ExitDetails);

}
