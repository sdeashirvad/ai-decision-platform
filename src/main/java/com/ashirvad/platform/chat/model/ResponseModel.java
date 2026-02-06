package com.ashirvad.platform.chat.model;

public class ResponseModel {
    private String text;
    public ResponseModel() {}
    public ResponseModel(String text) { this.text = text; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
