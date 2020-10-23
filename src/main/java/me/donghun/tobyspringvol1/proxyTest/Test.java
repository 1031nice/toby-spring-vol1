package me.donghun.tobyspringvol1.proxyTest;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        Hello hello = (Hello) Proxy.newProxyInstance(
                Test.class.getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        System.out.println(hello.sayHello("donghun"));
        System.out.println(hello.sayHi("donghun"));
        System.out.println(hello.sayThankYou("donghun"));
    }

}
