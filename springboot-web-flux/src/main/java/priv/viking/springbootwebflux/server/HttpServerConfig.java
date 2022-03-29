package priv.viking.springbootwebflux.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;

import java.util.Objects;

/**
 * Created By Viking on 2022/3/28
 * Reactive Web 服务配置类
 */
@Configuration
public class HttpServerConfig {

    @Bean
    public HttpServer httpServer(RouterFunction<ServerResponse> routerFunction, Environment environment) {
        HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
        ReactorHttpHandlerAdapter httpHandlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
        return HttpServer.create().host("localhost")
                .port(Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.port"))))
                .handle(httpHandlerAdapter);
    }
}
