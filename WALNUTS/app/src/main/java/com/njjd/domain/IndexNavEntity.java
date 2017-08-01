package com.njjd.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by mrwim on 17/7/31.
 */
@Entity
public class IndexNavEntity {
    @Id
    private String id;
    private String name;
    @Generated(hash = 172469163)
    public IndexNavEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 1565697110)
    public IndexNavEntity() {
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
}
