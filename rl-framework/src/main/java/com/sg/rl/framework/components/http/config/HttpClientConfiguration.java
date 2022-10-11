package com.sg.rl.framework.components.http.config;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfiguration {


	private Logger log = LoggerFactory.getLogger(this.getClass());


	@Bean
	HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {

		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30,
				TimeUnit.SECONDS);

		pollingConnectionManager.setMaxTotal(1000);

		pollingConnectionManager.setDefaultMaxPerRoute(1000);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(pollingConnectionManager);

		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));

		httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
		headers.add(new BasicHeader("Accept-Language", "en-US"));
		headers.add(new BasicHeader("Connection", "Keep-Alive"));

		httpClientBuilder.setDefaultHeaders(headers);

		HttpClient httpClient = httpClientBuilder.build();

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);

		clientHttpRequestFactory.setConnectTimeout(5000);

		/*clientHttpRequestFactory.setReadTimeout(5000);*/
		clientHttpRequestFactory.setReadTimeout(10000);
		clientHttpRequestFactory.setConnectionRequestTimeout(200);

		return clientHttpRequestFactory;
	}



	@Bean("restTemplate")
	RestTemplate restTemplate() {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory());
		restTemplate.setErrorHandler(new HttpThrowErrorHandler());
		return restTemplate;
	}
}
