package org.fcesur.cs.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fcesur.cs.message.Message;
import org.fcesur.cs.msg.Frame;
import org.fcesur.cs.msg.TransmittedMessage;
import org.fcesur.cs.services.PlayerSession;
import org.fcesur.cs.services.ServiceMessage;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Slf4j
@Deprecated
public class JacksonObjectWrapper {
    private ObjectMapper mapper = null;

    public JacksonObjectWrapper() {
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

    public String writeValueAsString(Frame message) {
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

    public String getPayload(@NonNull String payload) {

        if (log.isDebugEnabled()) {
            log.debug("Received payload: {}", payload);
        }

        return Try.of(() -> mapper.readTree(payload))
              .map(node -> node.get("payload"))
              .map(node -> node.toString())
              .onFailure(e -> log.error("Error while getting payload", e))
              .getOrNull();
    }

    public String writeValueAsString(JsonNode message) {
        try {

            return mapper.writeValueAsString(message.asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode parse(Message message) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(message);
            oos.flush();
            byte[] byteJson = bos.toByteArray();
            return mapper.readTree(byteJson);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String writeValueAsString(TransmittedMessage message) {
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

    public String writeValueAsString(PlayerSession message) throws JsonProcessingException {
        return mapper.writeValueAsString(message);
    }

}
