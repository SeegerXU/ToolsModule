package com.seeger.library.bean;

/**
 * 饼状图数据
 *
 * @author seeger
 */
public class PieBean {
    private String title;
    private int percent;
    private int color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
