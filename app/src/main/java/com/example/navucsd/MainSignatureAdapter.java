package com.example.navucsd;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MainSignatureAdapter extends RecyclerView.Adapter<MainSignatureAdapter.MyViewHolder> {

    private static int NUMBER_COL = 3;
    private String[] nameSet = {"Fallen Star", "Sun God", "Geisel", "Vice and Virtues", "Stone Bear", "Biomedical Library"};
    private int[] pictures = {R.drawable.fallen_star, R.drawable.sun_god, R.drawable.geisel, R.drawable.vice_and_virtues, R.drawable.stone_bear, R.drawable.biomed_lib};

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardViewTop, cardViewBottom;
        public TextView textViewTop, textViewBottom;
        public ImageView imageViewTop, imageViewBottom;
        public MyViewHolder(ConstraintLayout constraintLayout) {
            super(constraintLayout);
            cardViewTop = constraintLayout.findViewById(R.id.column_top);
            cardViewTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
                            v.getContext().startActivity(intent);
                }
            });
            cardViewBottom = constraintLayout.findViewById(R.id.column_bottom);
            cardViewBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
            textViewTop = constraintLayout.findViewById(R.id.column_top_text);
            textViewBottom = constraintLayout.findViewById(R.id.column_bottom_text);
            imageViewTop = constraintLayout.findViewById(R.id.column_top_image);
            imageViewBottom = constraintLayout.findViewById(R.id.column_bottom_image);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainSignatureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sig_column_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.textViewTop.setText(mDataset[position]);
        holder.textViewTop.setText(nameSet[position]);
        holder.textViewBottom.setText(nameSet[position + NUMBER_COL]);
        holder.imageViewTop.setImageResource(pictures[position]);
        holder.imageViewBottom.setImageResource(pictures[position + NUMBER_COL]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return NUMBER_COL;
    }
}