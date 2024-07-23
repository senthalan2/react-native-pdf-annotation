package com.pdfannotation;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = PdfAnnotationModule.NAME)
public class PdfAnnotationModule extends ReactContextBaseJavaModule {
  public static final String NAME = "PdfAnnotation";
  static public ReactContext reactContext = null;

  public PdfAnnotationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void openPdf(String url, ReadableMap readableMap, Promise promise) {
    try{
      if(reactContext != null){
        Intent intent = new Intent(reactContext, MuPDFActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if(readableMap != null){
          Utilities utilities = new Utilities(readableMap);
          intent.putExtra("isEnableAnnot",utilities.getBoolValue("isEnableAnnot",true));
          intent.putExtra("continuePage",utilities.getIntValue("continuePage",0));
          intent.putExtra("isEnableCustomHeaderColor",utilities.getBoolValue("isEnableCustomHeaderColor",false));
          intent.putExtra("isEnableCustomFooterColor",utilities.getBoolValue("isEnableCustomFooterColor",false));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getReactApplicationContext().startActivity(intent);
        promise.resolve(true);
      }
      else{
        promise.reject("Error","Error to open Document, Context null");
      }
    }
    catch (Exception exception){
      promise.reject("Error",exception.getMessage());
    }
  }
}
