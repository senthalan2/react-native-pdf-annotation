package com.artifex.mupdfdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teachingsofswamidayananda.R;

public class BookMarkAdapter extends ArrayAdapter<BookMark> {

    List<BookMark> bookMarkList;
    ImageButton bookMarkDeleteButton;

    private static class ViewHolder {
        TextView textviewPageNo, textPageDesc;
        CheckBox chkBox;
    }

    public BookMarkAdapter(BookMarkActivity context, int resource, List<BookMark> bookMarkList) {
        super(context, resource, bookMarkList);
        this.bookMarkList = bookMarkList;
        this.bookMarkDeleteButton = (ImageButton) context.findViewById(R.id.bookMarkDeleteButton);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final BookMark item = getItem(position);
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.bookmark_list_item, parent, false);
            holder = new ViewHolder();
            holder.textPageDesc = (TextView) row.findViewById(R.id.pageDesc);
            holder.textviewPageNo = (TextView) row.findViewById(R.id.pageTitle);
            holder.chkBox = (CheckBox) row.findViewById(R.id.checkBoxBookMark);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.chkBox.setOnCheckedChangeListener(null); // Remove previous listener

        holder.textviewPageNo.setText("Page " + item.getPageNo());
        holder.textPageDesc.setText(item.getBookMarkDesc());
        holder.chkBox.setChecked(item.getIsChecked());

        holder.chkBox.setTag(item);
        holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setIsChecked(isChecked);
                updateDeleteButtonVisibility();
            }
        });

        return row;
    }

    private void updateDeleteButtonVisibility() {
        int checkedItemCount = 0;
        for (BookMark bookMark : bookMarkList) {
            if (bookMark.getIsChecked()) {
                checkedItemCount++;
            }
        }
        bookMarkDeleteButton.setVisibility(checkedItemCount > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    ArrayList<BookMark> getCheckedItems() {
        ArrayList<BookMark> checkedItems = new ArrayList<>();
        for (BookMark bookMark : bookMarkList) {
            if (bookMark.getIsChecked()) {
                checkedItems.add(bookMark);
            }
        }
        return checkedItems;
    }
}