package net.myplayplanet.bwinfbackend.model;

public enum TaskType {
    JWINF,
    BWINF;

    public String shortName(){
        return switch (this) {
            case JWINF -> "J";
            case BWINF -> "A";
        };
    }
}
