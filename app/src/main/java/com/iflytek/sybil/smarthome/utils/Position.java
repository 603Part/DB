package com.iflytek.sybil.smarthome.utils;

public class Position {

    /**
     * 位序
     */
    private String index;
    /**
     * 封装
     */
    private String fznum;
    /**
     * 播报号
     */
    private String bobaohao;
    /**
     * 行号
     */
    private String hanghao;
    /**
     * 列号
     */
    private String leihao;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFznum() {
        return fznum;
    }

    public void setFznum(String fznum) {
        this.fznum = fznum;
    }

    public String getBobaohao() {
        return bobaohao;
    }

    public void setBobaohao(String bobaohao) {
        this.bobaohao = bobaohao;
    }

    public String getHanghao() {
        return hanghao;
    }

    public void setHanghao(String hanghao) {
        this.hanghao = hanghao;
    }

    public String getLeihao() {
        return leihao;
    }

    public void setLeihao(String leihao) {
        this.leihao = leihao;
    }

    @Override
    public String toString() {
        return "Position{" +
                "index='" + index + '\'' +
                ", fznum='" + fznum + '\'' +
                ", bobaohao='" + bobaohao + '\'' +
                ", hanghao='" + hanghao + '\'' +
                ", leihao='" + leihao + '\'' +
                '}';
    }
}
