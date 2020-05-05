package com.example.navucsd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapterTourOverViewPage extends BaseAdapter {
    Object[] items;
    Context context;

    public ListViewAdapterTourOverViewPage(Context context, Object[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
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

        String operator = (String) items[i];
        containerTextViewTourOverviewPage.setText(operator);
        return view;

    }
}
