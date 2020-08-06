package com.UCSDTripleC.XPloreUCSD;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;

import java.io.IOException;
import java.io.InputStream;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.MyViewHolder> {

	private ClickTracker clickTracker;
	private String[] names;
	private String[] urls;
	private Context context;
	private int marginSize;
	private int dividerSize;

	/**
	 * Constructs this object with empty/no content.  To set the content, call {@code setContent()}.
	 * @param clickTracker the {@link ClickTracker} used
	 * @param marginSize the size of the margins
	 * @param dividerSize the size of the divider
	 * @param context the context to use
	 */
	public HorizontalRecyclerAdapter(
		ClickTracker clickTracker,
		int marginSize,
		int dividerSize,
		Context context)
	{
		this.clickTracker = clickTracker;
		this.marginSize = marginSize;
		this.dividerSize = dividerSize;
		this.context = context;
		this.names = new String[0];
		this.urls = new String[0];
	}

	/**
	 * Set the contents of this horizontal recycler.
	 *
	 * @param names the names to be displayed
	 * @param urls the URLs of the image to be displayed
	 */
	public void setContent(String[] names, String[] urls) {
		this.names = names;
		this.urls = urls;
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
		int item_size = (metrics.widthPixels - (int) ((2 * marginSize + dividerSize + 16) * metrics.density)) / 2;
		int text_height = (int) (50 * metrics.density);
		int image_height = item_size - text_height;
		int text_width = item_size - (int) (18 * metrics.density);
		adjustLayoutParam(holder.textView, text_width, text_height);
		holder.textView.setText(names[position]);
		holder.textView.setSelected(true);
		adjustLayoutParam(holder.imageView, item_size, image_height);
		if (urls != null) {
			// load image
			try {
				// get input stream
				InputStream ims = context.getAssets().open(urls[position]);
				// load image as Drawable
				Drawable d = Drawable.createFromStream(ims, null);
				// set image to ImageView
				holder.imageView.setImageDrawable(d);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
//		holder.imageView.setImageResource(images[position]);
		adjustLayoutParam(holder.cardView, item_size, item_size);
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

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		public CardView cardView;
		public TextView textView;
		public ImageView imageView;

		public MyViewHolder(ClickTracker clickTracker, CardView rootView) {
			super(rootView);
			cardView = rootView;
			textView = rootView.findViewById(R.id.sig_text);
			imageView = rootView.findViewById(R.id.sig_image);
			cardView.setOnClickListener(view -> {
				if (!clickTracker.isClicked()) {
					clickTracker.click();
					Context context = view.getContext();
					Intent intent = new Intent(context, LandmarkDetailsActivity.class);
					intent.putExtra("placeName", textView.getText());
					context.startActivity(intent);
				}
			});
		}
	}
}