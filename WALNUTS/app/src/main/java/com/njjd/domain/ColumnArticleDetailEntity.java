package com.njjd.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrwim on 17/9/14.
 * 专栏文章详情
 */

public class ColumnArticleDetailEntity extends ColumnArticleEntity{
    private String columnName;//栏目名称
    private String isPoint;//是否点赞
    private String isSave;//是否收藏
    public ColumnArticleDetailEntity(String head, String name, String title, String content, String pic) {
        this.head = head;
        this.name = name;
        this.title = title;
        this.content = content;
        this.pic = pic;
    }
    public ColumnArticleDetailEntity(JSONObject object){
        super(object);
        try {
            this.columnName=object.isNull("column_name")?"":object.getString("column_name");
            this.isSave=object.getString("is_collect");
            this.isPoint=object.getString("is_point");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getIsPoint() {
        return isPoint;
    }

    public void setIsPoint(String isPoint) {
        this.isPoint = isPoint;
    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }
}
