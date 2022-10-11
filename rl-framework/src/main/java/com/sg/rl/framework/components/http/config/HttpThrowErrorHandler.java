package com.sg.rl.framework.components.http.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class HttpThrowErrorHandler implements ResponseErrorHandler {

    /** logger */
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {

        log.warn("client http response status {}, status content {}", response.getStatusCode(), response.getStatusText());

        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }
}