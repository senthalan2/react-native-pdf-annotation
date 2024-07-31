package com.artifex.mupdfdemo;

import java.util.ArrayList;
import java.util.List;

import com.teachingsofswamidayananda.R;
import com.teachingsofswamidayananda.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class BookMarkActivity extends Activity implements OnClickListener {

    ListView bookMarkListView;
    List<BookMark> bookMarkInfo;
    ImageButton btnDeleteBookMark;
    ImageButton bookmarkCloseButton;
    BookMarkAdapter bkAdapter;

    public static final String BOOK_ID = "bookId";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String IS_LIST_EMPTY = "isListEmpty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bookmark_list);

        bookMarkListView = findViewById(R.id.listViewBookMark);
        btnDeleteBookMark = findViewById(R.id.bookMarkDeleteButton);
        bookmarkCloseButton = findViewById(R.id.bookmarkCloseButton);
        bookmarkCloseButton.setOnClickListener(this);
        btnDeleteBookMark.setOnClickListener(this);

        // Set empty view
        TextView emptyView = findViewById(R.id.emptyView);
        bookMarkListView.setEmptyView(emptyView);

        bookMarkInfo = Utilities.getAllBookMark(this, getIntent().getExtras().getString(BOOK_ID));

        if (bookMarkInfo != null && !bookMarkInfo.isEmpty()) {
            bkAdapter = new BookMarkAdapter(this, R.id.listViewBookMark, bookMarkInfo);
            bookMarkListView.setAdapter(bkAdapter);
        } else {
            Log.v("Notification", "Book mark info is null or empty");
        }

        bookMarkListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Notification", "Getpage no : " + bookMarkInfo.get(position).getPageNo());
//                setResult(bookMarkInfo.get(position).getPageNo() - 1);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(CURRENT_PAGE, bookMarkInfo.get(position).getPageNo() - 1);
                resultIntent.putExtra(IS_LIST_EMPTY, bookMarkInfo == null || bookMarkInfo.isEmpty());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        updateDeleteButtonVisibility();
    }

    private void updateDeleteButtonVisibility() {
        int checkedItemCount = 0;
        for (BookMark bookMark : bookMarkInfo) {
            if (bookMark.getIsChecked()) {
                checkedItemCount++;
            }
        }
        btnDeleteBookMark.setVisibility(checkedItemCount > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bookMarkDeleteButton) {
            for (BookMark checked : bkAdapter.getCheckedItems()) {
                if (checked.getIsChecked()) {
                    bkAdapter.remove(checked);
                    Log.v("Notification", "Checked bookid : " + checked.getBookId());
                    Log.v("Notification", "Checked pageno : " + checked.getPageNo());
                    Utilities.deleteBookMark(this, checked.getBookId(), checked.getPageNo());
                }
            }
            bkAdapter.notifyDataSetChanged();
            updateDeleteButtonVisibility();
        } else if (v.getId() == R.id.bookmarkCloseButton) {
            backAction();
        } else {
            Log.v("Notification", "Checked array is null");
        }
    }

    private void backAction() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(CURRENT_PAGE, getIntent().getExtras().getInt(CURRENT_PAGE));
        resultIntent.putExtra(IS_LIST_EMPTY, bookMarkInfo == null || bookMarkInfo.isEmpty());
        setResult(RESULT_OK, resultIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        backAction();
    }
}
