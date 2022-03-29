package priv.viking.springbootwebflux.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import priv.viking.springbootwebflux.handler.PersonHandler;

/**
 * Created By Viking on 2022/3/28
 * 请求路由处理层
 * 配置请求路由，把请求映射到相应的Handler的处理方法
 */
@Configuration
public class RouterConfig {

    // 对标@Controller、@RequestMapping等标准的Spring MVC注解，提供一套函数式风格的API，用于创建Router、Handler和Filter
    @Bean
    public  RouterFunction<ServerResponse> routerFunction(PersonHandler handler){
        return RouterFunctions.route()
                .GET("/api/person/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler :: getPerson)
                .GET("/api/person", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler :: listPeople)
                .POST("/api/person", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler :: createPerson)
                .build();
    }
}
