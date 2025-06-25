package org.fcesur.gcs.http.impl;


/*import com.fairrummy.constants.Constants;
import com.fairrummy.model.entity.RegistrationDO;
import com.fairrummy.utility.GsonUtils;
import com.fairrummy.utility.StringUtils;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MatchMakingServiceRestClient {

  private static final String SERVICE_NAME = "mms";
  private RestClient restClient;

  private String mmURL = "http://localhost:8081/mm";

  @PostConstruct
  public void setup() {
    restClient = new RestClient(SERVICE_NAME);
  }

  public ResponseEntity<String> register(RegistrationDO registrationDO) {
    log.info("Performing register registrationDO = {}", registrationDO);
    String url = mmURL + "/tenant/register";
    String requestBody = GsonUtils.toJson(registrationDO);
    Map<String, Object> props = new HashMap<>();
    props.put(Constants.HTTP_HEADER_OPERATION_NAME, "register");
    props.put(Constants.HTTP_HEADER_SERVICE_NAME, SERVICE_NAME);
    try {
      return restClient.post(url, requestBody, props);
    } catch (Exception e) {
      log.error(
          "Exception: while registering template in matchmaker registrationDO: {} due to {}",
          registrationDO,
          StringUtils.getStackTrace(e));
      return null;
    }
  }

  public ResponseEntity<String> deregister(String tenantId, String templateId) {
    log.info("Performing deregister gameId = {}, templateId = {}", tenantId, templateId);
    String url =
            mmURL
            + "/tenant/"
            + tenantId
            + "/entities/"
            + templateId
            + "/deregister";
    Map<String, Object> props = new HashMap<>();
    props.put(Constants.HTTP_HEADER_OPERATION_NAME, "deregister");
    props.put(Constants.HTTP_HEADER_SERVICE_NAME, SERVICE_NAME);
    try {
      return restClient.delete(url, null, props);
    } catch (Exception e) {
      log.error(
          "Exception: while de-registering Template with contestTemplateId: {} due to {}",
          templateId,
          StringUtils.getStackTrace(e));
      return null;
    }
  }

  public ResponseEntity<String> clearFmg(String tenantId, String templateId) {
    log.info("Performing clear FMGs templateId = {}", templateId);
    String url =
            mmURL
            + "/tenant/"
            + tenantId
            + "/entities/"
            + templateId
            + "/clearFMG";
    Map<String, Object> props = new HashMap<>();
    props.put(Constants.HTTP_HEADER_OPERATION_NAME, "clearFMG");
    props.put(Constants.HTTP_HEADER_SERVICE_NAME, SERVICE_NAME);
    return restClient.delete(url, null, props);
  }

  public ResponseEntity<String> bulkUpdate(List<RegistrationDO> registrationDOList) {
    log.info("Performing bulk update for registrationDOList: {}", registrationDOList);
    String url = mmURL + "/tenant/bulkRegistration";
    String reqBody = GsonUtils.toJson(registrationDOList);
    Map<String, Object> props = new HashMap<>();
    props.put(Constants.HTTP_HEADER_OPERATION_NAME, "bulkRegistration");
    props.put(Constants.HTTP_HEADER_SERVICE_NAME, SERVICE_NAME);
    return restClient.put(url, reqBody, props);
  }
}*/
