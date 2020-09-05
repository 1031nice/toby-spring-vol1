package me.donghun.tobyspringvol1.templateExam;

public interface LineCallback<T> {

    T doSomethingWithLine(String line, T value);

}
