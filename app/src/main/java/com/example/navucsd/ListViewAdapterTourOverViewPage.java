package com.example.navucsd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapterTourOverViewPage extends BaseAdapter {
    ArrayList<Object> items;
    Context context;

    public ListViewAdapterTourOverViewPage(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.tour_overview_page_listview_layout,
                    null);
        }
        TextView containerTextViewTourOverviewPage = (TextView) view.findViewById
                (R.id.textView3);

        Button deleteButton = (Button) view.findViewById(R.id.deleteButtonTourOverviewPage);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(position);
                notifyDataSetChanged();
            }
        });
        String item = (String) items.get(position);
        containerTextViewTourOverviewPage.setText(item);
        return view;

    }
}
