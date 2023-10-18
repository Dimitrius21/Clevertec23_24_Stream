package ru.clevertec.streamtask.entity;

public enum Operator {
    MTS(33),
    A1(44),
    LIFE(25);

    private int code;

    Operator(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
