package com.ikea.productservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.ikea.productservice.constants.ProductExceptionCode;
import com.ikea.productservice.exception.ProductClientException;
import com.ikea.productservice.vo.ProductErrorResponse;

import reactor.core.publisher.Mono;

@Configuration
public class ProductConfig {
	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}
	

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        return WebClient
                .builder()
                .filter(ExchangeFilterFunction.ofResponseProcessor(this::renderApiErrorResponse));
    }

    private Mono<ClientResponse> renderApiErrorResponse(ClientResponse clientResponse) {
    	System.out.println(clientResponse.statusCode());
        if(clientResponse.statusCode().isError()){
            return clientResponse.bodyToMono(ProductErrorResponse.class)
                    .flatMap(apiErrorResponse -> {
                    	System.out.println(apiErrorResponse.getMessage());
                    	 return Mono.error(new ProductClientException(ProductExceptionCode.BAD_REQUEST, apiErrorResponse.getMessage()));}
                    );
        }
        return Mono.just(clientResponse);
    }

}
