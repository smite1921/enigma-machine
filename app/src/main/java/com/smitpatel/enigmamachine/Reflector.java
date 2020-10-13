package com.smitpatel.enigmamachine;

public class Reflector {

    private String identifer;
    private int[] reflectorMap;

    public int input(int input) {
        return reflectorMap[input];
    }

    public void setReflectorMap(int[] reflectorMap) {
        this.reflectorMap = reflectorMap;
    }

    public int[] getReflectorMap() {
        return reflectorMap;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }

    public String getIdentifer() {
        return identifer;
    }
}
