package com.example.navucsd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.tour_overview_page_listview_layout,
                    null);
        }
        TextView containerTextViewTourOverviewPage = (TextView) view.findViewById
                (R.id.containerTextViewTourOverviewPage);

        String item = (String) items.get(i);
        containerTextViewTourOverviewPage.setText(item);
        return view;

    }
}
