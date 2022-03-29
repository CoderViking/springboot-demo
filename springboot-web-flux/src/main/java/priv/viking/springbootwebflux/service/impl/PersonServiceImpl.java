package priv.viking.springbootwebflux.service.impl;

import org.springframework.stereotype.Service;
import priv.viking.springbootwebflux.model.Person;
import priv.viking.springbootwebflux.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By Viking on 2022/3/28
 * 业务处理层与传统的mvc中的service层一样
 */
@Service
public class PersonServiceImpl implements PersonService {
    Map<Integer, Person> persons = new HashMap<>();
    public PersonServiceImpl(){
        this.persons.put(1, new Person("Jack", 20));
        this.persons.put(2, new Person("Rose", 16));
    }
    @Override
    public Mono<Void> savePerson(Mono<Person> person) {
        return person.doOnNext(it -> {
            Integer id = persons.size()+1;
            persons.put(id, it);
            System.out.println("Saved " + it.toString() + " with " + id);
        }).thenEmpty(Mono.empty());
    }

    // 根据 id 获取 Mono 对象包装的 person 对象
    @Override
    public Mono<Person> getPerson(Integer id) {
        return Mono.justOrEmpty(persons.get(id));
    }

    // 返回所有的 Person 数据，包装在 Flux 对象中
    @Override
    public Flux<Person> allPeople() {
        return Flux.fromIterable(persons.values());
    }
}
