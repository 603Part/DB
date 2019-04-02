package com.iflytek.sybil.smarthome.db;

public class ThreeBean {
    private int three_id;
    private String name;
    private int three_position;
    private int two_id;
    private int have;
    private String bobao;

    public ThreeBean() {
    }

    public ThreeBean(int three_id, String name, int three_position, int two_id, int have, String bobao) {
        this.three_id = three_id;
        this.name = name;
        this.three_position = three_position;
        this.two_id = two_id;
        this.have = have;
        this.bobao = bobao;
    }

    public int getThree_id() {
        return three_id;
    }

    public void setThree_id(int three_id) {
        this.three_id = three_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThree_position() {
        return three_position;
    }

    public void setThree_position(int three_position) {
        this.three_position = three_position;
    }

    public int getTwo_id() {
        return two_id;
    }

    public void setTwo_id(int two_id) {
        this.two_id = two_id;
    }

    public int getHave() {
        return have;
    }

    public void setHave(int have) {
        this.have = have;
    }

    public String getBobao() {
        return bobao;
    }

    public void setBobao(String bobao) {
        this.bobao = bobao;
    }
}
