package org.fcesur.gcs.http.impl;

import org.fcesur.gcs.dto.constants.template.TemplateInfo;
import org.fcesur.gcs.mapper.PCObjectMapper;
import org.fcesur.gcs.response.dto.FMGResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GameEngineRestClient {

    private static final String SERVICE_NAME = MQ_EXCHANGE_GAME_ENGINE;
    private RestClient restClient;

    @Autowired
    private PCObjectMapper pcObjectMapper;

    private String geURL = "http://18.191.105.81:8082/";

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
