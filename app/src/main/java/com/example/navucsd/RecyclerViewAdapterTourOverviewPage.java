package com.example.navucsd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerViewAdapterTourOverviewPage extends RecyclerView.Adapter<RecyclerViewAdapterTourOverviewPage.ViewHolder> {
    private ArrayList<Object> items;
    private Context context;

    public RecyclerViewAdapterTourOverviewPage(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    // ViewHolder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FloatingActionButton fabDeleteButton;
        public CardView cardView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fabDeleteButton = (FloatingActionButton) itemView.findViewById(R.id.floating_action_delete_button_tour_overview_page);
            cardView = (CardView) itemView.findViewById(R.id.cardview_tour_overview_page);
            textView = (TextView) itemView.findViewById(R.id.textView_in_cardview_tour_overview_page);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_detail_tour_overview_page, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = items.get(position);
        holder.textView.setText(item.toString());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
