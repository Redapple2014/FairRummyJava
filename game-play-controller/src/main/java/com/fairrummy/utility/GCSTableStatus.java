package com.fairrummy.utility;


import com.fairrummy.response.dto.FMGResponse;

import java.util.Collection;

public class GCSTableStatus {

    public static void updateTableInfo(FMGResponse response, int templateId, int maxPlayers, double skill)
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
            tableInfo.setTemplateId(templateId);
            tableInfo.setStatus(1);
            tableInfo.setSkill(skill);

            TableInfoCache.putTableInfo(templateId, tableInfo);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static synchronized  TableInfo getBestTable(int templateId, double skill )
    {
        TableInfo info = null;

        if( skill > 0 )
        {
            info = getBestTableBasedOnSkill(templateId, skill);
        }
        else {
            for(int i = 0; i < 10; i++ )
            {
                Collection< TableInfo > tableInfos = TableInfoCache.getAllTablesForTemplate(templateId);
                if( tableInfos != null && !tableInfos.isEmpty())
                {
                    for(TableInfo tInfo : tableInfos )
                    {
                        if( tInfo.getAvailableSeats() > 0 && tInfo.getStatus() < 3 || tInfo.getStatus() == 5 || tInfo.getStatus() == 7)
                        {
                            info = tInfo;
                            break;
                        }
                    }
                }
            }

        }

        return info;
    }

    private static TableInfo getBestTableBasedOnSkill(int templateId, double skill)
    {
        TableInfo info = null;

        int increment = 5;

        for( int j = 1; j <= 2; j++)
        {
            double minSkill = skill - (j *increment);
            double maxSkill = skill + (j * increment);

            for(int i = 0; i < 10; i++ )
            {
                Collection< TableInfo > tableInfos = TableInfoCache.getAllTablesForTemplate(templateId);
                if( tableInfos != null && !tableInfos.isEmpty())
                {
                    for(TableInfo tInfo : tableInfos )
                    {
                        if( tInfo.getAvailableSeats() > 0 && tInfo.getStatus() < 3 || tInfo.getStatus() == 5 || tInfo.getStatus() == 7 && tInfo.getSkill() >= minSkill && tInfo.getSkill() <= maxSkill)
                        {
                            info = tInfo;
                            break;
                        }
                    }
                }
            }
        }

        return info;

    }
}
