package com.sg.rl.framework.components.http;

import com.sg.rl.framework.components.base.ComponentInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Component
public class RestTemplateComponent implements ComponentInterface {

    @Autowired
    private RestTemplate restTemplate;

    public String sendGet(String url) {
        return sendWithParamsHeaders(url, null, HttpMethod.GET, new HttpHeaders());
    }

    public String sendGetWithParms(String url, Map<String, ?> urlParam) {
        return sendWithParams(url, urlParam, HttpMethod.GET);
    }

    public String sendGetWithParamsHeaders(String url, Map<String, ?> urlParam, HttpHeaders headers) {
        return sendWithParamsHeaders(url, urlParam, HttpMethod.GET, headers);
    }

    public String sendPostJson(String url, Map<String, ?> body) {
        return sendJson(url, null, body, HttpMethod.POST, new HttpHeaders());
    }


    public String sendPostWithJsonHeaders(String url, Map<String, ?> body,HttpHeaders headers) {
        return sendJson(url, null, body, HttpMethod.POST, headers);
    }

    public String sendPostWithJsonParamsHeaders(String url, Map<String, ?> urlParam, Map<String, ?> body,HttpHeaders headers) {
        return sendJson(url, urlParam, body, HttpMethod.POST, headers);
    }

    public String sendPostWithJsonParams(String url, Map<String, ?> urlParam, Map<String, ?> body) {
        return sendJson(url, urlParam, body, HttpMethod.POST, new HttpHeaders());
    }


    public String sendPostWithFormBody(String url, Map<String, ?> body) {
        return sendForm(url, null, body, HttpMethod.POST, new HttpHeaders());
    }

    public String sendPostWithFormParamsBody(String url, Map<String, ?> urlParam, Map<String, ?> body) {
        return sendForm(url, urlParam, body, HttpMethod.POST, new HttpHeaders());
    }

    public String sendWithParams(String url, Map<String, ?> urlParam, HttpMethod method) {
        return sendWithParamsHeaders(url, urlParam, method, new HttpHeaders());
    }

    public String sendWithParamsHeaders(String url, Map<String, ?> urlParam, HttpMethod method, HttpHeaders headers) {
        if (urlParam == null) {
            urlParam = new HashMap<>(0);
        }
        url = handleUrlParam(url, urlParam);
        HttpEntity<MultiValueMap<String, ?>> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, method, requestEntity, String.class, urlParam).getBody();
    }



    public String sendForm(String url, Map<String, ?> urlParam, Map<String, ?> body, HttpMethod method) {
        return sendForm(url, urlParam, body, method, new HttpHeaders());
    }

    public String sendForm(String url, Map<String, ?> urlParam, Map<String, ?> body,
                                  HttpMethod method, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return send(url, urlParam, body, method, headers);
    }



    public String sendWithJsonParams(String url, Map<String, ?> urlParam, Map<String, ?> body, HttpMethod method) {
        return sendJson(url, urlParam, body, method, new HttpHeaders());
    }

    public String sendJson(String url, Map<String, ?> urlParam, Map<String, ?> body,
                                  HttpMethod method, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return send(url, urlParam, body, method, headers);
    }

    public String send(String url, Map<String, ?> urlParam, Map<String, ?> body, HttpMethod method,
                              HttpHeaders headers) {
        if (urlParam == null) {
            urlParam = new HashMap<>(0);
        }

        url = handleUrlParam(url, urlParam);
        HttpEntity<Map<String, ?>> requestEntity = null;
        if (Objects.equals(headers.getContentType(), MediaType.APPLICATION_JSON)) {

            requestEntity = new HttpEntity<>(body, headers);
        }
        if (Objects.equals(headers.getContentType(), MediaType.APPLICATION_FORM_URLENCODED)) {
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            Iterator<? extends Map.Entry<String, ?>> iterator = body.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ?> next = iterator.next();
                param.add(next.getKey(), next.getValue());
            }
            requestEntity = new HttpEntity<>(param, headers);
        }
        return restTemplate.exchange(url, method, requestEntity, String.class, urlParam).getBody();
    }

    private String handleUrlParam(String url, Map<String, ?> urlParam) {
        if (urlParam == null || urlParam.isEmpty()) {
            return url;
        }
        Iterator<? extends Map.Entry<String, ?>> iterator = urlParam.entrySet().iterator();
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");
        while (iterator.hasNext()) {
            Map.Entry<String, ?> entry = iterator.next();
            urlBuilder.append(entry.getKey()).append("={").append(entry.getKey()).append("}").append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        return urlBuilder.toString();
    }
}