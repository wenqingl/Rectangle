package com.example;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties;

public class Rectangle {
    private String name;
    private int width;
    private int height;
    private String color;
    private int id;
    private int borderWidth;

    public int getId(){
        return this.id;
    }
    public int getBorderWidth(){
        return this.borderWidth;
    }

    public String getName(){
        return this.name;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public String getColor(){
        return this.color;
    }

    public void setName(String n){
        this.name = n;
    }

    public void setWidth(int w){
        this.width = w;
    }

    public void setHeight(int h){
        this.height = h;
    }

    public void setColor(String c){
        this.color = c;
    }

    public void setId(Integer n){
        this.id = n;
    }

    public void setBorderWidth(Integer n){
        this.borderWidth = n;
    }
    
}
