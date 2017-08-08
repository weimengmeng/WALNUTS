package com.njjd.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by mrwim on 17/8/8.
 */
@Entity
public class TagEntity {
    @Id
    private String id;
    private String name;
    private String code;
    @Generated(hash = 1767980366)
    public TagEntity(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
    @Generated(hash = 2114918181)
    public TagEntity() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }

}
