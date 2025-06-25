package org.fcesur.gcs.http.impl;


import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class RestClient {

    private static final String HTTP_HEADER_SERVICE_NAME = "servicename";
    private static final String HTTP_HEADER_OPERATION_NAME = "opname";
    private static final String DOT = ".";

    private RestTemplate restTemplate;

    private String clientName;
    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private final CloseableHttpClient closeableHttpClient;

    public RestClient(String clientName) {
        this.clientName = clientName;
        this.poolingHttpClientConnectionManager = getConnectionManager(clientName);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        this.closeableHttpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory =
              new HttpComponentsClientHttpRequestFactory(closeableHttpClient);

        factory.setConnectionRequestTimeout(10000);
        factory.setConnectTimeout(10000);
        //factory.setReadTimeout(configuration.getInt(clientName + DOT + "http.read.timeout", 10000));
        restTemplate = new RestTemplate(factory);
        restTemplate
              .getMessageConverters()
              .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    /**
     * @param clientName based on client name it fetched the properties from zk
     * @return PoolingHttpClientConnectionManager <br>
     * Based on zk properties initialise PoolingHttpClientConnectionManager
     */
    private PoolingHttpClientConnectionManager getConnectionManager(String clientName) {
        PoolingHttpClientConnectionManager conMan = new PoolingHttpClientConnectionManager();
        conMan.setDefaultMaxPerRoute(100);
        conMan.setMaxTotal(100);
        conMan.setValidateAfterInactivity(100);
        return conMan;
    }

    /**
     * @param url         url to which rest call has to be made
     * @param props       Header Properties
     * @param queryParams query parameter in the url
     * @param pathParams  path params in the url
     * @return Response Entity with String as response body
     */
    public ResponseEntity<String> get(
          String url,
          Map<String, Object> props,
          Map<String, Object> queryParams,
          Map<String, Object> pathParams) {

        HttpHeaders httpHeaders = getAllHttpHeaders(props);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        if (log.isDebugEnabled()) {
            log.debug(
                  "http GET req on {} ,queryParams {} pathParams {} httpHeaders {} ",
                  url,
                  queryParams,
                  pathParams,
                  httpHeaders);
        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        queryParams.forEach(uriComponentsBuilder::queryParam);
        ResponseEntity<String> responseEntity = null;
        responseEntity =
              restTemplate.exchange(
                    uriComponentsBuilder.buildAndExpand(pathParams).toUri(),
                    HttpMethod.GET,
                    entity,
                    String.class);
        if (log.isDebugEnabled()) {
            log.debug(
                  "Received http GET response {} on url {} ,queryParams {} pathParams {} httpHeaders {}",
                  responseEntity,
                  url,
                  queryParams,
                  pathParams,
                  httpHeaders);
        }
        return responseEntity;
    }

    /**
     * @param url         url to which rest call has to be made
     * @param requestBody requestBody with which call has to be made
     * @param headers     Header Properties
     * @return
     */
    public ResponseEntity<String> post(String url, String requestBody, Map<String, Object> headers) {
        HttpHeaders httpHeaders = getAllHttpHeaders(headers);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        if (log.isDebugEnabled()) {
            log.debug(
                  "Invoking http POST req on {}, properties {} , requestBody {}",
                  url,
                  headers,
                  requestBody);
        }
        ResponseEntity<String> responseEntity =
              restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (log.isDebugEnabled()) {
            log.debug(
                  "Received http POST response on url {} for reqBody {} properties {} responseEntity {}",
                  url,
                  requestBody,
                  headers,
                  responseEntity);
        }
        return responseEntity;
    }

    /**
     * @param url
     * @param requestBody
     * @param props
     * @return
     */
    public ResponseEntity<String> put(String url, String requestBody, Map<String, Object> props) {
        HttpHeaders httpHeaders = getAllHttpHeaders(props);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        if (log.isDebugEnabled()) {
            log.info(
                  "Invoking http PUT req on {}, properties {} requestBody {} ", url, props, requestBody);
        }
        ResponseEntity<String> responseEntity =
              restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        if (log.isDebugEnabled()) {
            log.debug(
                  "Received http PUT response on url {} for reqBody {} properties {} responseEntity {}",
                  url,
                  requestBody,
                  props,
                  responseEntity);
        }
        return responseEntity;
    }

    public ResponseEntity<String> delete(String url, String requestBody, Map<String, Object> props) {
        HttpHeaders httpHeaders = getAllHttpHeaders(props);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        if (log.isDebugEnabled()) {
            log.debug(
                  "Invoking http DELETE req on {}, requestBody {} using headers {}",
                  url,
                  requestBody,
                  props);
        }
        ResponseEntity<String> responseEntity =
              restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        if (log.isDebugEnabled()) {
            log.debug(
                  "Received http DELETE response on url {} for reqBody {} properties {} responseEntity {}",
                  url,
                  requestBody,
                  props,
                  responseEntity);
        }
        return responseEntity;
    }

    /**
     * @param props HEADERS to be added to the default https headers
     * @return HttpHeaders add default headers and what all are passed as props
     */
    protected HttpHeaders getAllHttpHeaders(Map<String, Object> props) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HTTP_HEADER_SERVICE_NAME, clientName);
        headers.set(HTTP_HEADER_OPERATION_NAME, (String) props.get(HTTP_HEADER_OPERATION_NAME));

        props.keySet().forEach(key -> headers.set(key, (String) props.get(key)));

        return headers;
    }

    /**
     * Gracefully shutdown of rest client
     *
     * @throws IOException
     */
    public void shutdown() throws IOException {
        log.info("Shutting down rest client for {} started", clientName);
        poolingHttpClientConnectionManager.close();
        closeableHttpClient.close();
        log.info("Shutting down rest client for {} completed", clientName);
    }
}
