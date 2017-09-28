package com.njjd.domain;

/**
 * Created by mrwim on 17/8/1.
 */

public class CommonEntity {
    private String id;
    private String name;
    private String code;

    public CommonEntity(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
    public CommonEntity(){

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
