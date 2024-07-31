package com.artifex.mupdfdemo;

public class BookMark {

    String _bookId;
    int _pageNo;
    boolean _isChecked;
    String _pageDesc;

    public BookMark() {
        // TO-DO: Auto-generated constructor stub
    }

    public BookMark(String bookId, int pageNo, boolean isChecked, String desc) {
        this._bookId = bookId;
        this._pageNo = pageNo;
        this._isChecked = isChecked;
        this._pageDesc = desc;
    }

    public void setBookId(String bookId) {
        this._bookId = bookId;
    }

    public String getBookId() {
        return this._bookId;
    }

    public void setPageNo(int pageNo) {
        this._pageNo = pageNo;
    }

    public int getPageNo() {
        return this._pageNo;
    }

    public boolean getIsChecked() {
        return this._isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this._isChecked = isChecked;
    }

    public void setBookMarkDesc(String desc) {
        this._pageDesc = desc;
    }

    public String getBookMarkDesc() {
        return this._pageDesc;
    }

}
