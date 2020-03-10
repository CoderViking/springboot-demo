package com.viking.elasticsearch.springbootweb.lambda;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.lang.Iterable;

/**
 * Created by Viking on 2019/10/9
 * java官方教程——lambda表达式
 */
public class RosterTest {
    interface CheckPerson {
        boolean test(Person p);
    }

    // 方法 1:创建搜索符合一个特征的成员的方法
    //一种简单的方法是创建几种方法。每种方法都会搜索与一个特征（例如性别或年龄）相匹配的成员。以下方法将打印出超过指定年龄的成员：

    public static void printPersonsOlderThan(List<Person> roster, int age) {
        for (Person p : roster) {
            if (p.getAge() >= age) {
                p.printPerson();
            }
        }
    }

    // 方法 2: 创建更通用的搜索方法
    //以下方法相比printPersonsOlderThan；它会打印指定年龄范围内的成员：

    public static void printPersonsWithinAgeRange(List<Person> roster, int low, int high) {
        for (Person p : roster) {
            if (low <= p.getAge() && p.getAge() < high) {
                p.printPerson();
            }
        }
    }

    // 方法 3: 在当前类中指定搜索条件代码
    // 方法 4: 在匿名类中指定搜索条件代码
    // 方法 5: 使用Lambda表达式指定搜索条件代码

    public static void printPersons(List<Person> roster, CheckPerson tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    // 方法 6: 将标准功能接口与Lambda表达式一起使用

    public static void printPersonsWithPredicate(
            List<Person> roster, Predicate<Person> tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    // 方法 7: 在整个应用程序中使用Lambda表达式

    public static void processPersons(
            List<Person> roster,
            Predicate<Person> tester,
            Consumer<Person> block) {
        for (Person p : roster) {
            if (tester.test(p)) {
                block.accept(p);
            }
        }
    }

    // 方法 7, 第二个示例

    public static void processPersonsWithFunction(
            List<Person> roster,
            Predicate<Person> tester,
            Function<Person, String> mapper,
            Consumer<String> block) {
        for (Person p : roster) {
            if (tester.test(p)) {
                String data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

    // 方法 8: Use Generics More Extensively

    public static <X, Y> void processElements(
            Iterable<X> source,
            Predicate<X> tester,
            Function<X, Y> mapper,
            Consumer<Y> block) {
        for (X p : source) {
            if (tester.test(p)) {
                Y data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

    public static void main(String... args) {

        List<Person> roster = Person.createRoster();

        for (Person p : roster) {
            p.printPerson();
        }

        // 方法 1: 创建搜索符合一个特征的成员的方法

        System.out.println("年龄大于20的人员:");
        printPersonsOlderThan(roster, 20);
        System.out.println();

        // 方法 2: 创建更通用的搜索方法

        System.out.println("年龄在14到30岁之间的人员:");
        printPersonsWithinAgeRange(roster, 14, 30);
        System.out.println();

        // 方法 3: 在本地类中指定搜索条件代码

        System.out.println("符合选择服务资格的人员:");

        class CheckPersonEligibleForSelectiveService implements CheckPerson {
            public boolean test(Person p) {
                return p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25;
            }
        }

        printPersons(
                roster, new CheckPersonEligibleForSelectiveService());


        System.out.println();

        // 方法 4: 在匿名类中指定搜索条件代码

        System.out.println("符合选择服务资格的人员" +
                "(匿名內部类):");

        printPersons(
                roster,
                new CheckPerson() {
                    public boolean test(Person p) {
                        return p.getGender() == Person.Sex.MALE
                                && p.getAge() >= 18
                                && p.getAge() <= 25;
                    }
                }
        );

        System.out.println();

        // 方法 5: 使用Lambda表达式指定搜索条件代码

        System.out.println("符合选择服务资格的人员" +
                "(lambda表达式):");

        printPersons(
                roster,
                (Person p) -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25
        );

        System.out.println();

        // 方法 6: 将标准功能接口与Lambda表达式一起使用

        System.out.println("符合选择服务资格的人员" +
                "(with Predicate parameter):");

        printPersonsWithPredicate(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25
        );

        System.out.println();

        // 方法 7: 在整个应用程序中使用Lambda表达式

        System.out.println("符合选择服务资格的人员" +
                "(with Predicate and Consumer parameters):");

        processPersons(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25,
                p -> p.printPerson()
        );

        System.out.println();

        // 方法 7, 第二个例子

        System.out.println("符合选择服务资格的人员" +
                "(with Predicate, Function, and Consumer parameters):");

        processPersonsWithFunction(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25,
                p -> p.getEmailAddress(),
                email -> System.out.println(email)
        );

        System.out.println();

        // 方法 8: 更广泛地使用泛型

        System.out.println("符合选择服务资格的人员" +
                "(generic version):");

        processElements(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25,
                p -> p.getEmailAddress(),
                email -> System.out.println(email)
        );

        System.out.println();

        // 方法 9: 使用接受Lambda表达式作为参数的聚合操作
        // as Parameters

        System.out.println("符合选择服务资格的人员" +
                "(with bulk data operations):");

        roster.stream().filter(
                        p -> p.getGender() == Person.Sex.MALE
                                && p.getAge() >= 18
                                && p.getAge() <= 25)
                .map(p -> p.getEmailAddress())
                .forEach(email -> System.out.println(email));
    }
}
