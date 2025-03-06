package com.example.bt1.model;
public class MonHoc {
    private String name;
    private String decs;
    private int pic;

    public MonHoc(String name, String decs, int pic) {
        this.name = name;
        this.decs = decs;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
