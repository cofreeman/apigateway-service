package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Custom Pre Filter
        // 반환되는 Gateway 는 인터페이스이므로 그것의 구현체를 apply 메서드에서 구현하고 반환해야한다.
        //람다식으로 선언하므로써 apply 메서드의 인자 두개를 받은 일급함수같은것?이라고 보면된다.
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom RRE filter: request id ->{}", request.getId());

            //Custom Post Filter
            //필터 잘 적용한 뒤에 반환값으로 Mono 를 주겠습니다.
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST filter response code ->{}", response.getStatusCode());
            }));
        };
    }

    public static class Config {

    }
}
