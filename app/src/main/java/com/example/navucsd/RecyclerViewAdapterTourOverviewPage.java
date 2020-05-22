package com.example.navucsd;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerViewAdapterTourOverviewPage extends RecyclerView.Adapter<RecyclerViewAdapterTourOverviewPage.ViewHolder> {
    private ArrayList<String> items;
    private Context context;

    public RecyclerViewAdapterTourOverviewPage(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    // ViewHolder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView placeNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = (TextView) itemView.findViewById(R.id.placeNameTextView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_overview_page_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.placeNameTextView.setText(item.toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
