package com.pdfannotation;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.artifex.mupdfdemo.BookMark;
import com.facebook.react.bridge.ReadableMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
  ReadableMap readableMap;
  public static String DB_NAME = "master_v2.db";

  public static String DB_PATH;
  private static SQLiteDatabase ebookDatabase ;
  public static String Table_BookMark_Master = "BookMarkMaster";
  public static boolean isSetContinuePage = false;

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

  public static void setIsSetContinuePage(boolean isContinuePageset){
    isSetContinuePage = isContinuePageset;
  }

  public synchronized void close() {
    if (ebookDatabase != null) {
      ebookDatabase.close();
    }
  }

  //opens the database from the internal memory
  public static void opendatabase(Context context) throws SQLException {
    //Open the database
    if(DB_PATH == null) {
      File dbDirectory = new File("/data/data/" + context.getPackageName() + "/databases");
      if (dbDirectory.exists()) {
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
      }
    }

    String mypath = DB_PATH + DB_NAME;

    File file = new File(mypath);
    if (file.exists() && !file.isDirectory()) {


      ebookDatabase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

    } else {
      try {
        copydatabase(context);
      } catch (IOException e) {

        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  //checks for the database's existence in the internal memory
  private static boolean checkdatabase() {
    //SQLiteDatabase checkdb = null;
    boolean checkdb = false;
    try {
      String myPath = DB_PATH + DB_NAME;
      File dbfile = new File(myPath);
      //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
      checkdb = dbfile.exists();
    } catch (SQLiteException e) {
      System.out.println("Database doesn't exist");
      e.printStackTrace();
    }
    //Log.v("Notification", "Successfully create folder");
    //Log.v("Notification", "Exist : "+checkdb);
    return checkdb;
  }


  //copies the database from assets to the internal storage
  private static void copydatabase(Context context) throws IOException {
    //Open your local db as the input stream

    if(DB_PATH == null) {
      File dbDirectory = new File("/data/data/" + context.getPackageName() + "/databases");
      Log.d("IIIII", "copydatabase: "+dbDirectory.exists());
      if (!dbDirectory.exists()) {
        // Try to create the directory
        boolean isCreated = dbDirectory.mkdirs();
      }
      if (dbDirectory.exists()) {
        DB_PATH = dbDirectory.getAbsolutePath() + "/";
      }
      else{
        Log.d("Utilities", "Failed to create database directory");
      }
//      if (dbDirectory.exists()) {
//        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
//      }
    }

    // Path to the just created empty db
    String outfilename = DB_PATH + DB_NAME;

    Log.v("Notification", "Output file path : " + outfilename);
    File file = new File(outfilename);
    if (!file.exists()) {

      try{
        InputStream myinput = context.getAssets().open("db/" + DB_NAME);

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
          myoutput.write(buffer, 0, length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();

      }
      catch (FileNotFoundException e) {

        throw e;
      } catch (IOException e) {

        throw e;
      } catch (Exception e) {
      }
    }

  }


  //creates the database in the internal memory
  public static void createdatabase(Context context) throws IOException {
    boolean dbexist = checkdatabase();
    if (dbexist) {

    } else {
      try {
        copydatabase(context);
      } catch (IOException e) {
        e.printStackTrace();
        throw new Error("Error copying database");
      }
    }
  }

  public static SQLiteDatabase getDatabase(Context context) {
    if (ebookDatabase == null) {
      try {
        opendatabase(context);
      } catch (SQLException e) {
        // TO-DO: Auto-generated catch block
        e.printStackTrace();
      }
    }

    return ebookDatabase;
  }

  public static void deleteBookMark(Context context, String bookId, int pageNo) {
    Log.v("Notification", "Delete function called");

    if (ebookDatabase != null)
      ebookDatabase.delete(Table_BookMark_Master, "BookID =? AND PageNumber =? ", new String[]{String.valueOf(bookId), String.valueOf(pageNo)});
    else {
      try {
        opendatabase(context);

        ebookDatabase.delete(Table_BookMark_Master, "BookID =? AND PageNumber =? ", new String[]{String.valueOf(bookId), String.valueOf(pageNo)});
      } catch (SQLException e) {
        // TO-DO: Auto-generated catch block
        e.printStackTrace();
      }
    }
    Log.v("Notification", "row Deleted ");
  }

  // Getting All BookMark
  @SuppressLint("Range")
  public static List<BookMark> getAllBookMark(Context context, String bookId) {
    List<BookMark> bookMarkList = new ArrayList<BookMark>();
    // Select All Query
    String selectQuery = "SELECT PageNumber,PageDescription FROM " + Table_BookMark_Master + " where BookID = ?";

    Cursor cursor = null;

    if (ebookDatabase != null)
      cursor = ebookDatabase.rawQuery(selectQuery, new String[]{bookId});
    else {
      try {
        opendatabase(context);

        cursor = ebookDatabase.rawQuery(selectQuery, new String[]{bookId});
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        BookMark bookMark = new BookMark();
        bookMark.setBookId(bookId);
        bookMark.setPageNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex("PageNumber"))));
        bookMark.setBookMarkDesc(cursor.getString(cursor.getColumnIndex("PageDescription")));
        bookMarkList.add(bookMark);
      } while (cursor.moveToNext());
    }

    cursor.close();

    // return contact list
    return bookMarkList;
  }

  public static int getBookMarkCount(Context context,String bookId) {

    int count = 0;

    String countQuery = "SELECT  * FROM " + Table_BookMark_Master + " where BookID = ?";

    if (ebookDatabase != null) {
      Cursor cursor = ebookDatabase.rawQuery(countQuery, new String[]{bookId});

      count = cursor.getCount();

      cursor.close();

    } else {
      try {
        opendatabase(context);

        Cursor cursor = ebookDatabase.rawQuery(countQuery, new String[]{bookId});

        count = cursor.getCount();

        cursor.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }


    }
    // return count
    return count;
  }


  public static boolean addBookMark(Context context, BookMark bookMark) {
    SQLiteDatabase db = getDatabase(context);

    if (db != null) {
      ContentValues values = new ContentValues();
      values.put("BookID", bookMark.getBookId());
      values.put("PageNumber", bookMark.getPageNo());
      values.put("PageDescription", bookMark.getBookMarkDesc());
      try {
        List<BookMark> bookMarkList = getAllBookMark(context, bookMark.getBookId());
        boolean isMatched = false;
        if (bookMarkList.size() > 0) {
          for (int i = 0; i < bookMarkList.size(); i++) {
            Log.v("Notification", "Bookmark page number : " + bookMark.getPageNo());
            Log.v("Notification", "Bookmarklist page number : " + bookMarkList.get(i).getPageNo());
            if (bookMark.getPageNo() == bookMarkList.get(i).getPageNo()) {
              isMatched = true;
              break;
            }
          }

          if (!isMatched) {
            db.insertOrThrow(Table_BookMark_Master, null, values);

            Toast.makeText(context, "Bookmark successfully added", Toast.LENGTH_LONG).show();
          } else {
            Toast.makeText(context, "This page is already a bookMark", Toast.LENGTH_SHORT).show();
          }
        } else {
          db.insertOrThrow(Table_BookMark_Master, null, values);
        }
      } catch (android.database.sqlite.SQLiteConstraintException error) {
        error.printStackTrace();
        return false;
      }

    }

    return true;

  }

}
