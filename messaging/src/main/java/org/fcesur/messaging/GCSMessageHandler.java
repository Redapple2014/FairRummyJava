package org.fcesur.messaging;

import lombok.extern.slf4j.Slf4j;
import org.fcesur.messaging.model.TableInfo;
import org.fcesur.messaging.model.TableInfoCache;
import org.fcesur.messaging.util.GsonUtils;
import org.jspecify.annotations.NonNull;

@Slf4j
public final class GCSMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(@NonNull String message) {
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