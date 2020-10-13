package com.smitpatel.enigmamachine;

public class Rotor {

    private String identifier;
    private int ring;
    private int position;
    private int turnover;
    private int[] forwardMap;
    private int[] reverseMap;

    public int mapping(int input, boolean direction) {
        int[] map = direction ? this.forwardMap : this.reverseMap;
        int mapsTo = map[floorMod(input + this.position - ring,26)];
        return floorMod(mapsTo - position + ring,26);
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }

    public void shift() {
        this.position = floorMod(this.position + 1,26);
    }

    public void reverseShift() {
        this.position = floorMod(this.position - 1, 26);
    }

    public void setForwardMap(int[] forwardMap) {
        this.forwardMap = forwardMap;
    }

    public void setReverseMap(int[] reverseMap) {
        this.reverseMap = reverseMap;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getTurnover() {
        return turnover;
    }

    public int getPosition() {
        return position;
    }

    private int floorMod(int x, int y) {
        int mod = x % y;
        if ((mod ^ y) < 0 && mod != 0) {
            mod += y;
        }
        return mod;
    }
}
