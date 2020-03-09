package com.viking.springbootweb.lambda;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by Viking on 2019/10/9
 * lambda表达式中变量的作用域测试
 */
public class LambdaScopeTest {
    public int x = 0;

    class FirstLevel {

        public int x = 1;

        void methodInFirstLevel(int x) {

            //下面的这一行语句导致编译器在语句A中产生错误“引用自lambda表达式的局部变量必须是final或有效的final”:
            //x = 99;

            Consumer<Integer> myConsumer = (y) ->
            {
                System.out.println("x = " + x); // 语句 A
                System.out.println("y = " + y);
                System.out.println("this.x = " + this.x);
                System.out.println("LambdaScopeTest.this.x = " +
                        LambdaScopeTest.this.x);
            };

            myConsumer.accept(x);

        }
    }

    public static void main(String... args) {
        LambdaScopeTest st = new LambdaScopeTest();
        LambdaScopeTest.FirstLevel fl = st.new FirstLevel();
        fl.methodInFirstLevel(23);
        String[] stringArray = { "Barbara", "James", "Mary", "John",
                "Patricia", "Robert", "Michael", "Linda" };
        Arrays.sort(stringArray, String::compareToIgnoreCase);
    }

}
