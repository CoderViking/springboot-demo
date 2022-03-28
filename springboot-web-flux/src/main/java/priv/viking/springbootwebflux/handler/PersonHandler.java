package priv.viking.springbootwebflux.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import priv.viking.springbootwebflux.model.Person;
import priv.viking.springbootwebflux.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created By Viking on 2022/3/28
 * handler中的方法主要处理routerConfig中配置的路由，
 * handler相当于spring mvc 中的controller中的方法，routerConfig中的配置相当于controller中的@RequestMapping注解
 */
@Service
public class PersonHandler {
    private final PersonService service;

    public PersonHandler(PersonService service) {
        this.service = service;
    }

    /**
     * 创建person对象
     * @param request 服务请求对象包装
     * @return 服务响应对象包装
     */
    public Mono<ServerResponse> createPerson(ServerRequest request) {
        Mono<Person> person = request.bodyToMono(Person.class);// 对象是在http请求的body中，以json字符串的格式传输
        return ServerResponse.ok().build(service.savePerson(person));
    }

    /**
     * 获取一个person对象
     * @param request 服务请求对象包装
     * @return 服务响应对象包装
     */
    public Mono<ServerResponse> getPerson(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Person> personMono = service.getPerson(id);
        return personMono.flatMap(
                person -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(person)))
                .switchIfEmpty(notFound);
    }

    /**
     * 拉取people列表
     * @param request 服务请求对象包装
     * @return 服务响应对象包装
     */
    public Mono<ServerResponse> listPeople(ServerRequest request) {
        Flux<Person> allPeople = service.allPeople();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(allPeople, Person.class);
    }
}
