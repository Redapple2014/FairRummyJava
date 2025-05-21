package com.fairrummy.utility;


import com.fairrummy.response.dto.FMGResponse;

import java.util.Collection;

public class GCSTableStatus {

    public static boolean updateTableInfo(FMGResponse response, int templateId, int maxPlayers)
    {

        long tableId = response.getTableId();
        try
        {
            TableInfo tableInfo = TableInfoCache.getTableInfo(templateId, tableId);
            if( tableInfo == null )
            {
                tableInfo = new TableInfo(tableId);
            }
            tableInfo.setEngineIp(response.getEngineIP());
            tableInfo.setAvailableSeats( maxPlayers -1 );

            TableInfoCache.putTableInfo(templateId, tableInfo);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static synchronized  TableInfo getBestTable(int templateId )
    {
        TableInfo info = null;
        Collection< TableInfo > tableInfos = TableInfoCache.getAllTablesForTemplate(templateId);
        if( tableInfos != null && tableInfos.size() > 0 )
        {
            for(TableInfo tInfo : tableInfos )
            {
                if( tInfo.getAvailableSeats() > 0 )
                {
                    tInfo.setAvailableSeats(tInfo.getAvailableSeats() - 1);

                    info = tInfo;

                    if( tInfo.getAvailableSeats() == 0 )
                    {
                        TableInfoCache.removeTableInfo(templateId, tInfo.getTableId());
                    }
                }
            }
        }

        return info;
    }
}
