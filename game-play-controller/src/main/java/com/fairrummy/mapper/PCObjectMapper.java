package com.fairrummy.mapper;

import static com.fairrummy.constants.ErrorCodes.JSON_PROCESSING_ERROR;

import com.fairrummy.exception.InternalServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PCObjectMapper {

    @Autowired
    private final ObjectMapper objectMapper;

    public PCObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String writeValueAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Json Processing Exception while writeValueAsString for object {}", object, e);
            throw new InternalServerErrorException(
                    JSON_PROCESSING_ERROR.getErrorCode(),
                    JSON_PROCESSING_ERROR.getHttpStatus(),
                    JSON_PROCESSING_ERROR.getMessage());
        }
    }

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            log.error(
                    "Json Processing Exception while readValue content {} valueType {}",
                    content,
                    valueType.getName(),
                    e);
            throw new InternalServerErrorException(
                    JSON_PROCESSING_ERROR.getErrorCode(),
                    JSON_PROCESSING_ERROR.getHttpStatus(),
                    JSON_PROCESSING_ERROR.getMessage());
        }
    }

    public JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (JsonProcessingException e) {
            log.error("Json Processing Exception while readTree content {}", content, e);
            throw new InternalServerErrorException(
                    JSON_PROCESSING_ERROR.getErrorCode(),
                    JSON_PROCESSING_ERROR.getHttpStatus(),
                    JSON_PROCESSING_ERROR.getMessage());
        }
    }

    public <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error(
                    "Json Processing Exception while readValue content {} valueTypeRef {}",
                    content,
                    valueTypeRef.getType().getTypeName(),
                    e);
            throw new InternalServerErrorException(
                    JSON_PROCESSING_ERROR.getErrorCode(),
                    JSON_PROCESSING_ERROR.getHttpStatus(),
                    JSON_PROCESSING_ERROR.getMessage());
        }
    }

    public JsonNode convertValue(Object object) {
        return objectMapper.convertValue(object, JsonNode.class);
    }

    public <T> T treeToValue(JsonNode node, Class<T> valueType) {
        try {
            return objectMapper.treeToValue(node, valueType);
        } catch (JsonProcessingException e) {
            log.error(
                    "Json Processing Exception while treeToValue node {} valueType {}",
                    node,
                    valueType.getName(),
                    e);
            throw new InternalServerErrorException(
                    JSON_PROCESSING_ERROR.getErrorCode(),
                    JSON_PROCESSING_ERROR.getHttpStatus(),
                    JSON_PROCESSING_ERROR.getMessage());
        }
    }

    public <T> T convertValue(Object object, Class<T> valueType) {
        return objectMapper.convertValue(object, valueType);
    }

    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }
}
