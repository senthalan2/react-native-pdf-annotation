package com.pdfannotation;

import com.facebook.react.bridge.ReadableMap;

public class Utilities {
  ReadableMap readableMap;
  public Utilities(ReadableMap readableMap) {
    this.readableMap = readableMap;
  }
  public boolean getBoolValue(String keyName,boolean defaultValue){
    boolean result = defaultValue;
    try{
      result = readableMap.getBoolean(keyName);
    }
    catch(Exception ignored){}
    return result;
  }
  public int getIntValue(String keyName,int defaultValue){
    int result = defaultValue;
    try{
      result = readableMap.getInt(keyName);
    }
    catch(Exception ignored){}
    return result;
  }
}
