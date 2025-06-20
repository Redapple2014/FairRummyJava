package com.fairrummy.message.queue;

import com.fairrummy.utility.GsonUtils;
import com.fairrummy.utility.TableInfo;
import com.fairrummy.utility.TableInfoCache;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GCSMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {
        log.info("GCSMessageHandler Received Message {}", message);
        TableInfo tableInfo = GsonUtils.fromJson(message, TableInfo.class);
        System.out.println("GCSMessageHandler Typecasted : " + tableInfo);
        if (tableInfo.getStatus() != 6) {
            TableInfoCache.putTableInfo(tableInfo.getTemplateId(), tableInfo);
        } else {
            TableInfoCache.removeTableInfo(tableInfo.getTemplateId(), tableInfo.getTableId());
        }

    }

}