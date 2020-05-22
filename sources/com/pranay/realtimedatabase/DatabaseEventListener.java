package com.pranay.realtimedatabase;
public interface DatabaseEventListener{
    void onUpdate(String query);
    void onDelete(String query);
    void onAlter(String query);
    void onSelect(String query);
}