package com.javageek.web.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {
	
	private final Integer MAX_TOTAL_CONNECTION;
	private final Integer DEFAULT_MAX_TOTAL_CONNECTION;
	private final Integer CONNECTION_REQUEST_TIMEOUT;
	private final Integer SOCKET_TIMEOUT;
	
	public BlockingRestTemplateCustomizer(@Value("${sfg.maxTotalConnections}") Integer maxTotalConnection, 
			@Value("${sfg.defaultMaxTotalConnection}") Integer defaultMaxTotalConnection,
			@Value("${sfg.connectionRequestTimeout}") Integer connectionRequestTimeout, 
			@Value("${sfg.socketTimeout}") Integer socketTimeout) {
		MAX_TOTAL_CONNECTION = maxTotalConnection;
		DEFAULT_MAX_TOTAL_CONNECTION = defaultMaxTotalConnection;
		CONNECTION_REQUEST_TIMEOUT = connectionRequestTimeout;
		SOCKET_TIMEOUT = socketTimeout;
	}

	public ClientHttpRequestFactory clientHttpRequestFactory() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(MAX_TOTAL_CONNECTION);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_TOTAL_CONNECTION);
		
		RequestConfig requestConfig = RequestConfig
				.custom()
				.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		
		CloseableHttpClient httpClient = HttpClients
				.custom()
				.setConnectionManager(connectionManager)
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setDefaultRequestConfig(requestConfig)
				.build();
		
		return new HttpComponentsClientHttpRequestFactory(httpClient);
	}

	@Override
	public void customize(RestTemplate restTemplate) {
		restTemplate.setRequestFactory(this.clientHttpRequestFactory());
	}

}
