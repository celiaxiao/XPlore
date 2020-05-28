package com.example.navucsd;

import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navucsd.utils.ClickTracker;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.MyViewHolder> {

	private ClickTracker clickTracker;
	private String[] names;
	private int[] images;
	private int marginSize;
	private int dividerSize;

	/**
	 * The constructor.
	 *
	 * @param clickTracker the {@link ClickTracker} used
	 * @param names the names to display
	 * @param images the images to display
	 * @param marginSize the size of the margins
	 * @param dividerSize the size of the divider
	 */
	public HorizontalRecyclerAdapter(
		ClickTracker clickTracker,
		String[] names,
		int[] images,
		int marginSize,
		int dividerSize
	) {
		this.clickTracker = clickTracker;
		this.names = names;
		this.images = images;
		this.marginSize = marginSize;
		this.dividerSize = dividerSize;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public HorizontalRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
																	 int viewType) {
		// create a new view
		CardView v = (CardView) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.signature_item, parent, false);
		MyViewHolder vh = new MyViewHolder(clickTracker, v);
		return vh;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		holder.textView.setText(names[position]);
		adjustLayoutParam(holder.imageView, (metrics.widthPixels - (int) ((2 * marginSize + dividerSize) * metrics.density)) / 2,
				(metrics.widthPixels - (int) ((2 * marginSize + dividerSize) * metrics.density)) / 2 - (int) (35 * metrics.density));
		holder.imageView.setImageResource(images[position]);
		adjustLayoutParam(holder.cardView, (metrics.widthPixels - (int) ((2 * marginSize + dividerSize) * metrics.density)) / 2,
				(metrics.widthPixels - (int) ((2 * marginSize + dividerSize) * metrics.density)) / 2);
	}

	private void adjustLayoutParam(View v, int width, int height) {
		ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		v.setLayoutParams(layoutParams);
	}

	private void adjustLayoutParam(View v, int size) {
		ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
		layoutParams.width = size;
		layoutParams.height = size;
		v.setLayoutParams(layoutParams);
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return names.length;
	}

	public void setContent() {

	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		public CardView cardView;
		public TextView textView;
		public ImageView imageView;

		public MyViewHolder(ClickTracker clickTracker, CardView rootView) {
			super(rootView);
			cardView = rootView;
			textView = rootView.findViewById(R.id.sig_text);
			imageView = rootView.findViewById(R.id.sig_image);
			cardView.setOnClickListener(v -> {
				if (clickTracker.isClicked()) return;
				clickTracker.click();
				Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
				v.getContext().startActivity(intent);
			});
		}
	}

}