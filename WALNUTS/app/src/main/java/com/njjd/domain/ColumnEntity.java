package com.njjd.domain;

import java.io.Serializable;

/**
 * Created by mrwim on 17/9/14.
 */

public class ColumnEntity implements Serializable{
    private String id;//栏目ID
    private String article_id;//文章ID
    private String columnName;//栏目名称
    private String head;//作者头像
    private String name;//作者名称
    private String title;//文章标题
    private String content;//文章内容
    private String pic;//文章配图
    private String commentNum;
    private String pointNum;
    public ColumnEntity(String id, String head, String name, String title, String content, String pic) {
        this.id = id;
        this.head = head;
        this.name = name;
        this.title = title;
        this.content = content;
        this.pic = pic;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getPointNum() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum = pointNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
