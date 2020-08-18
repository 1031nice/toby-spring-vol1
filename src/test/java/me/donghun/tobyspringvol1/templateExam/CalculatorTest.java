package me.donghun.tobyspringvol1.templateExam;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    Calculator calculator;
    String numFilepath;

    @Before
    public void setUp(){
        this.calculator = new Calculator();
        this.numFilepath = "C:\\Users\\1031n\\IntelliJ\\toby-spring-vol1\\src\\main\\resources\\numbers.txt";
    }

    @Test
    public void sumOfNumbers() throws IOException {
        assertThat(calculator.calcSum(this.numFilepath)).isEqualTo(10);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        assertThat(calculator.calcMultiply(this.numFilepath)).isEqualTo(24);
    }

}