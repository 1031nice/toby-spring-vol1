package me.donghun.tobyspringvol1.templateExam;

import org.assertj.core.api.AbstractBigIntegerAssert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        LineCallback sumCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };
        return fileReadTemplate(filepath, sumCallback, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback sumCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        };
        return fileReadTemplate(filepath, sumCallback, 1);
    }

    // initVal 초깃값. 덧셈일 경우 0, 곱셈일 경우 1이어야 하므로
    public Integer fileReadTemplate(String filepath, LineCallback callback, int initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer res = initVal;
            String line = null;
            while((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            throw e;
        }
        finally {
            if(br != null){
                try {br.close();}
                catch (IOException e) { System.out.println(e.getMessage()); }
            }
        }
    }

}
