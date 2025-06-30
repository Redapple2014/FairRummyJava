package org.fcesur.messaging.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableInfoCache {
    private static Map<Integer, ConcurrentHashMap<Long, TableInfo>> tableInfoCacheRummy = null;

    public static void initializeTableInfoCache(String cacheLoc, String cacheName) {
        tableInfoCacheRummy = new ConcurrentHashMap<Integer, ConcurrentHashMap<Long, TableInfo>>();
    }

    public static void putTableInfo(int templateId, TableInfo tableInfo) {
        try {
            ConcurrentHashMap<Long, TableInfo> tableInfoMap = tableInfoCacheRummy.get(templateId);
            if (tableInfoMap == null) {
                tableInfoMap = new ConcurrentHashMap<>();
            }
            tableInfoMap.put(tableInfo.getTableId(), tableInfo);
            tableInfoCacheRummy.put(templateId, tableInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TableInfo getTableInfo(int templateId, long tableId) {
        TableInfo tableInfo = null;
        try {
            ConcurrentHashMap<Long, TableInfo> tableInfoMap = tableInfoCacheRummy.get(templateId);
            tableInfo = tableInfoMap.get(tableId);
        } catch (NullPointerException ne) {
            System.out.println("Table info not found..!  templateId:" + templateId + " tableId:" + tableId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableInfo;
    }

    public static TableInfo removeTableInfo(int templateId, long tableId) {
        TableInfo tableInfo = null;
        try {
            ConcurrentHashMap<Long, TableInfo> tableInfoMap = tableInfoCacheRummy.get(templateId);
            tableInfo = tableInfoMap.remove(tableId);
            tableInfoCacheRummy.put(templateId, tableInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableInfo;
    }

    public static Collection<TableInfo> getAllTablesForTemplate(int templateId) {
        Collection<TableInfo> tableInfos = null;
        try {
            ConcurrentHashMap<Long, TableInfo> tableInfoMap = tableInfoCacheRummy.get(templateId);
            if (tableInfoMap != null)
                tableInfos = new ArrayList<TableInfo>(tableInfoMap.values());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableInfos;
    }

    public static void populate(int templateId, Collection<TableInfo> tableInfos) {
        try {
            ConcurrentHashMap<Long, TableInfo> tableMap = tableInfoCacheRummy.get(templateId);
            if (tableMap == null) {
                tableMap = new ConcurrentHashMap<Long, TableInfo>();

            }
            for (TableInfo info : tableInfos) {
                tableMap.put(info.getTableId(), info);
            }
            tableInfoCacheRummy.put(templateId, tableMap);
            System.out.println("Table Info size populate  " + tableInfoCacheRummy.size() + "    templateId   " + templateId + "  tableMap " + tableMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, ConcurrentHashMap<Long, TableInfo>> getCache() {
        return tableInfoCacheRummy;
    }
}