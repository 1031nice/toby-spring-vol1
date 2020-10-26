package me.donghun.tobyspringvol1;

import me.donghun.tobyspringvol1.proxyTest.Hello;
import me.donghun.tobyspringvol1.proxyTest.HelloTarget;
import me.donghun.tobyspringvol1.proxyTest.UppercaseHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class DynamicProxyTest {

    @Test
    public void simpleProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("donghun")).isEqualTo("HELLO DONGHUN");
        assertThat(proxiedHello.sayHi("donghun")).isEqualTo("HI DONGHUN");
        assertThat(proxiedHello.sayThankYou("donghun")).isEqualTo("THANK YOU DONGHUN");
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            String ret = (String) methodInvocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("donghun")).isEqualTo("HELLO DONGHUN");
        assertThat(proxiedHello.sayHi("donghun")).isEqualTo("HI DONGHUN");
        assertThat(proxiedHello.sayThankYou("donghun")).isEqualTo("Thank You donghun");
    }

}
