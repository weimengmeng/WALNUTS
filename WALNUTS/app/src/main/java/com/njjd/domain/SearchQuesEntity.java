package com.njjd.domain;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/12/6.
 */

public class SearchQuesEntity implements Serializable {
    private String articleId;
    private String title;
    private String content;
    private String answerNum;
    private String focusNum;

    public SearchQuesEntity(JsonObject object) {
        this.articleId = object.get("id").getAsString();
        this.title = object.get("title").getAsString();
        this.content = object.get("comment_content").getAsString();
        this.answerNum = object.get("answer_num").getAsString();
        this.focusNum = object.get("follow_num").getAsString();
    }

    public SearchQuesEntity() {
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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

    public String getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }

    public String getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(String focusNum) {
        this.focusNum = focusNum;
    }
}
//    {id=59.0, uid=12146319448, type=1.0,
//            title=感觉自己有时候太为客户着想了，狠不下心来要价，怎么办？,
//        desci=, contents=不知道这算不算太善良哈哈，因为知道自己产品的优缺点，总是不敢报高价，
//        每次都弄得很被动。,
//        imgs=[http://192.168.0.111/hetao_api/public/uploads/530915065/artImg/2017-11-01/
//        // 59f985caba2ad.jpg], answer_num=9.0, follow_num=55.0, point_num=0.0,
//        // collect_num=0.0, invisible=0.0, anonymous=1.0, stat=��, sort_id=null,
//        // is_select=0.0, change_time=2017-10-11 06:46:17, add_time=2017-10-11 06:46:17,
//        // headimg=http://192.168.0.111/hetao_api/public/static/headimgs/tu188.jpg,
//        // is_focus=0.0}]
//
//    }
