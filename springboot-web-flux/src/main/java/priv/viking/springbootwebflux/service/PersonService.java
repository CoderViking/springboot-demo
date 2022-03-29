package priv.viking.springbootwebflux.service;

import priv.viking.springbootwebflux.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created By Viking on 2022/3/28
 */
public interface PersonService {

    Mono<Void> savePerson(Mono<Person> person);// 创建person对象

    Mono<Person> getPerson(Integer id);// 获取一个person对象

    Flux<Person> allPeople();// 查询people列表
}
