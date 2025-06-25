package org.fcesur.gcs.message.queue;

import org.fcesur.gcs.utility.GsonUtils;
import org.fcesur.gcs.utility.TableInfo;
import org.fcesur.gcs.utility.TableInfoCache;
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