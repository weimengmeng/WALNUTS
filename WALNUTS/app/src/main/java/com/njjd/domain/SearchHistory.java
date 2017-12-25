package com.njjd.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by mrwim on 17/12/5.
 */
@Entity
public class SearchHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id(autoincrement=true)
    private long id;
    private String keywords;
    private Date date;
    @Generated(hash = 1931103856)
    public SearchHistory(long id, String keywords, Date date) {
        this.id = id;
        this.keywords = keywords;
        this.date = date;
    }
    @Generated(hash = 1905904755)
    public SearchHistory() {
    }
    public String getKeywords() {
        return this.keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
