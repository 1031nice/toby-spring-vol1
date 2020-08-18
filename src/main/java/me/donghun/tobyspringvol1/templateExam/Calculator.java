package me.donghun.tobyspringvol1.templateExam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calSum(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        int sum = 0;
        String line = null;
        while((line = br.readLine()) != null){
            sum += Integer.valueOf(line);
        }
        br.close();
        return sum;
    }
}
