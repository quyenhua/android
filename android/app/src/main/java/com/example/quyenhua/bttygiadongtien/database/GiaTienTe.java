package com.example.quyenhua.bttygiadongtien.database;

/**
 * Created by Quyen Hua on 3/15/2017.
 */

public class GiaTienTe {

    private String id;
    private String name;
    private String value;

    public GiaTienTe(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
