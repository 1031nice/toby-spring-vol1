package me.donghun.tobyspringvol1.user.domain;

public enum Level {

    // 와 순서도 상관이 있네. BASIC부터 GOLD 순으로하면 컴파일 에러
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next){
        this.value = value;
        this.next = next;
    }

    public int intValue(){
        return value;
    }

    public Level nextLevel() { return next; }

    public static Level valueOf(int value){
        switch (value){
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown value: " + value);
        }
    }

}
