package me.donghun.tobyspringvol1.templateExam;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    @Test
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        int sum = calculator.calSum(getClass().getResource("numbers.txt").getPath());
        assertThat(sum).isEqualTo(10);
    }

}