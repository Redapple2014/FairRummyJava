package com.fairrummy.http.impl;

import com.fairrummy.dto.constants.template.TemplateInfo;
import com.fairrummy.mapper.PCObjectMapper;
import com.fairrummy.model.response.ApiResponse;
import com.fairrummy.response.dto.FMGResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GameEngineRestClient {

    private static final String SERVICE_NAME = "ge";
    private RestClient restClient;

    @Autowired
    private PCObjectMapper pcObjectMapper;

    private String geURL = "http://localhost:8082/";

    @PostConstruct
    public void setup() {
        restClient = new RestClient(SERVICE_NAME);
    }

    public FMGResponse tableJoin(int templateId) {
        log.info("Calling tablecreate API = {}", templateId);
        String url = geURL + "v1/tablecreate";

        Map<String, Object> props = new HashMap<>();
        props.put("Content-Type", "application/json");

        TemplateInfo templateInfo = new TemplateInfo(templateId);

        ResponseEntity<String> responseEntity =
              this.restClient.post(
                    url,
                    pcObjectMapper.writeValueAsString(templateInfo),
                    props);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return pcObjectMapper.readValue(responseEntity.getBody(), FMGResponse.class);
        }
        return null;
    }
}
