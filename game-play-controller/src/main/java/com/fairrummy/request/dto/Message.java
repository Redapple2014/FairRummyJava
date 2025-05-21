package com.fairrummy.request.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Message
{
    private int serviceType;
    private String msgType;
    private long tableId;

    public Message( int serviceType, String msgType, long tableId )
    {
        super();
        this.serviceType = serviceType;
        this.msgType = msgType;
        this.tableId = tableId;
    }

    public int getServiceType()
    {
        return serviceType;
    }

    public String getMsgType()
    {
        return msgType;
    }

    public long getTableId()
    {
        return tableId;
    }

}
