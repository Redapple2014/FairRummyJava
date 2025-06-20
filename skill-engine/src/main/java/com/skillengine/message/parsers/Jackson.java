package com.skillengine.message.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillengine.rummy.message.Message;
import com.skillengine.service.message.ServiceMessage;
import com.skillengine.sessions.PlayerSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Jackson {
    private ObjectMapper mapper = null;

    public Jackson() {
        mapper = new ObjectMapper();
    }

    public String writeValueAsString(Message message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T readValue(String message, Class<T> classz) {
        try {
            return mapper.readValue(message, classz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMsgType(String payload) {
        try {
            JsonNode jsonNode = mapper.readTree(payload);
            JsonNode msgTypeNode = jsonNode.get("msgType");
            return msgTypeNode.asText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isMsgType(JsonNode payload, String targetMsgType) {
        try {
            JsonNode msgTypeNode = payload.get("msgType");
            return msgTypeNode.asText().equals(targetMsgType);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMsgType(String payload, String targetMsgType) {
        try {
            JsonNode jsonNode = mapper.readTree(payload);
            JsonNode parsedPayload = jsonNode.get("payload");
            JsonNode msgTypeNode = parsedPayload.get("msgType");
            return msgTypeNode.asText().equals(targetMsgType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String writeValueAsString(JsonNode message) {
        try {

            return mapper.writeValueAsString(message.asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String writeValueAsString(PlayerSession message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String writeValueAsString(ServiceMessage message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String writeValueAsString(List<Message> message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T readValue(InputStream message, Class<T> classz) {
        try {
            return mapper.readValue(message, classz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String writeValueAsString(Object classz) {
        try {
            return mapper.writeValueAsString(classz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
