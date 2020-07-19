package com.wrath.client.dto;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class Model {


    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    public static final int AUDIO_TYPE = 2;

    public int type;
    public int data;
    public String date;
    public String text;
    public String name;


    public Model(int type, String text,String date, String name, int data) {
        this.type = type;
        this.data = data;
        this.date=date;
        this.text = text;
        this.name = name;

    }

}
