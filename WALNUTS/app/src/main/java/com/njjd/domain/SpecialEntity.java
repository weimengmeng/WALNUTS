package com.njjd.domain;

/**
 * Created by mrwim on 17/9/14.
 * 精选实体类
 */

public class SpecialEntity {
    private ColumnEntity columnEntity=null;
    private SelectedAnswerEntity answerEntity=null;
    public SpecialEntity(){

    }
    public SpecialEntity(ColumnEntity columnEntity,SelectedAnswerEntity answerEntity){
        this.columnEntity=columnEntity;
        this.answerEntity=answerEntity;
    }

    public ColumnEntity getColumnEntity() {
        return columnEntity;
    }

    public void setColumnEntity(ColumnEntity columnEntity) {
        this.columnEntity = columnEntity;
    }

    public SelectedAnswerEntity getAnswerEntity() {
        return answerEntity;
    }

    public void setAnswerEntity(SelectedAnswerEntity answerEntity) {
        this.answerEntity = answerEntity;
    }
}
