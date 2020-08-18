package me.donghun.tobyspringvol1.templateExam;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    @Test
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        int sum = calculator.calSum("C:\\Users\\1031n\\IntelliJ\\toby-spring-vol1\\src\\main\\resources\\numbers.txt");
        assertThat(sum).isEqualTo(10);
    }

}