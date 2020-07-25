package com.UCSDTripleC.XPloreUCSD;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.UCSDTripleC.XPloreUCSD.database.LandmarkDatabase;
import com.UCSDTripleC.XPloreUCSD.database.Landmark;
import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is the Places page which contains some places and a grid of landmarks.
 */
public final class PlacesPageFragment extends Fragment {

	/**
	 * Tracks if this page has been clicked; used to prevent multiple clicks.
	 */
	private ClickTracker clickTracker;

	/**
	 * A smarter {@code ImageView} that automatically resizes its internal upon size change.
	 */
	private final class SmartImageView extends androidx.appcompat.widget.AppCompatImageView {
		/**
		 * The last set width, defaults to 0 at first.
		 * Used to detect changes and decide when to resize.
		 */
		private int width;

		/**
		 * The path to the photo.
		 */
		private String photoPath;

		/**
		 * Constructs a {@code SmartImageView} with an image resource id and an aspect ratio.  It
		 * also resizes the parent to the height of this image view plus the height of the label.
		 *
		 * @param context the context used to create this instance
		 * @param photoPath the path of the image, if it is {@code null} the background is not set
		 * @param aspectRatio the desired aspect ratio (width / height)
		 * @param labelHeight the height of the label, in dp
		 */
		public SmartImageView(
			Context context,
			// TODO remove this from the constructor?
			String photoPath,
			double aspectRatio,
			int labelHeight
		) {
			super(context);

			this.photoPath = photoPath;

			this.getViewTreeObserver().addOnPreDrawListener(() -> {
				int new_width = getWidth();

				if (new_width != width) {
					ViewGroup.LayoutParams layout = getLayoutParams();

					layout.height = (int) (new_width / aspectRatio);
					width = new_width;

					if (this.photoPath != null) {
						InputStream ims = null;
						try {
							ims = context.getAssets().open(this.photoPath);
						} catch (IOException e) {
							// FIXME potential null pointer here?
							e.printStackTrace();
						}
						Bitmap src = BitmapFactory.decodeStream(ims);

						setBackground(resize(src, width, layout.height));
					}

					// set the size of the parent instead of self as parent doesn't know its height
					// should change as a result of changes in its children
					View parent = (View) getParent();
					ViewGroup.LayoutParams params = parent.getLayoutParams();
					params.height = layout.height + dpToXp(labelHeight);
					parent.setLayoutParams(params);

					requestLayout();
				}

				return true;
			});
		}

		/**
		 * Updates the path to the photo.
		 *
		 * @param photoPath the new path to the photo
		 */
		public void updatePhotoPath(String photoPath) {
			this.photoPath = photoPath;
			// FIXME actually update the photo
		}

		/**
		 * Get a resized image.
		 *
		 * @param src the source of the bitmap
		 * @param width the target width of the image
		 * @param height the target height of the image
		 * @return the resized image
		 */
		private Drawable resize(Bitmap src, int width, int height) {
			return new BitmapDrawable(
				getResources(),
				Bitmap.createScaledBitmap(src, width, height, true
			));
		}
	}

	/**
	 * Provides a binding from an app-specific data set to views that are displayed within a
	 * {@code RecyclerView}.
	 */
	private final class RecyclerAdapter
		extends RecyclerView.Adapter<RecyclerView.ViewHolder>
	{
		/**
		 * The type ID of a {@code PlaceOfTheDayHolder}.
		 */
		private final int TYPE_PLACE_OF_THE_DAY_HOLDER = 0;

		/**
		 * The type ID of a {@code CardViewHolder}.
		 */
		private final int TYPE_CARD_VIEW_HOLDER = 1;

		/**
		 * The data set.
		 */
		private Landmark[] landmarks;

		/**
		 * Describes the Place of the Day layout and metadata about its place within the
		 * {@code RecyclerView}.
		 */
		public class PlaceOfTheDayHolder extends RecyclerView.ViewHolder {

			/**
			 * The Place of the Day layout.
			 */
			public ConstraintLayout layout;

			/**
			 * Constructs a new {@code PlaceOfTheDayHolder}.
			 *
			 * @param layout the Place of the Day layout
			 */
			public PlaceOfTheDayHolder(ConstraintLayout layout) {
				super(layout);
				this.layout = layout;
			}
		}

		/**
		 * Describes a card view and metadata about its place within the {@code RecyclerView}.
		 */
		public class CardViewHolder extends RecyclerView.ViewHolder {

			/**
			 * The card.
			 */
			public CardView card;

			/**
			 * The image inside the card.
			 */
			public SmartImageView image;

			/**
			 * The label inside the card.
			 */
			public TextView label;

			/**
			 * Constructs a new {@code CardViewHolder}.
			 *
			 * @param card the card
			 * @param image the image inside the card
			 * @param label the label inside the card
			 */
			public CardViewHolder(CardView card, SmartImageView image, TextView label) {
				super(card);
				this.card = card;
				this.image = image;
				this.label = label;
			}
		}

		/**
		 * Constructs a new {@code RecyclerAdapter}.
		 *
		 * @param landmarks the {@code Landmark} array to display
		 */
		public RecyclerAdapter(Landmark[] landmarks) {
			this.landmarks = landmarks;
		}

		/**
		 * Called when {@code RecyclerView} needs a new {@code RecyclerView.ViewHolder} of the given
		 * type to represent an item.
		 *
		 * @param parent the {@code ViewGroup} into which the new {@code View} will be added after
		 * it is bound to an adapter position.
		 * @param viewType the view type of the new {@code View}
		 * @return a newly constructed {@code ViewHolder} that can represent the items of the given
		 * type
		 */
		// TODO support section title view type
		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			switch (viewType) {
				case TYPE_PLACE_OF_THE_DAY_HOLDER: return onCreatePlaceOfTheDayHolder(parent);
				case TYPE_CARD_VIEW_HOLDER: return onCreateCardViewHolder(parent);
				default: throw new IllegalStateException("view type must be valid");
			}
		}

		/**
		 * Called when {@code RecyclerView} needs a new {@code RecyclerAdapter.PlaceOfTheDayHolder}.
		 *
		 * @param parent the {@code ViewGroup} into which the new {@code View} will be added after
		 * it is bound to an adapter position.
		 * @return a newly constructed {@code CardViewHolder}
		 */
		private PlaceOfTheDayHolder onCreatePlaceOfTheDayHolder(@NonNull ViewGroup parent) {
			ConstraintLayout layout = (ConstraintLayout) LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.plages_page_place_of_the_day, parent, false);

			Landmark place = landmarks[hashDate(landmarks.length)];

			ImageView iv_restroom = layout.findViewById(R.id.places_page_place_of_the_day_restroom);
			ImageView iv_cafe = layout.findViewById(R.id.places_page_place_of_the_day_cafe);
			ImageView iv_restaurant = layout.findViewById(R.id.places_page_place_of_the_day_restaurant);
			ImageView iv_bus_stop = layout.findViewById(R.id.places_page_place_of_the_day_bus_stop);
			ImageView iv_parking = layout.findViewById(R.id.places_page_place_of_the_day_parking);
			ImageView iv_thumbnail = layout.findViewById(R.id.places_page_place_of_the_day_thumbnail);
			TextView tv_name = layout.findViewById(R.id.places_page_place_of_the_day_name);
			TextView tv_about = layout.findViewById(R.id.places_page_place_of_the_day_about);

			int color = ContextCompat.getColor(layout.getContext(), R.color.colorPrimaryDark);

			// FIXME proper error handling
			if (place.amenities != null) {
				Boolean result;
				result = place.amenities.get("restroom");
				if (result != null && result) iv_restroom.setColorFilter(color);
				result = place.amenities.get("cafe");
				if (result != null && result) iv_cafe.setColorFilter(color);
				result = place.amenities.get("restaurant");
				if (result != null && result) iv_restaurant.setColorFilter(color);
				result = place.amenities.get("busstop");
				if (result != null && result) iv_bus_stop.setColorFilter(color);
				result = place.amenities.get("parking");
				if (result != null && result) iv_parking.setColorFilter(color);
			}

			try {
				InputStream ims = parent.getContext().getAssets().open(place.thumbnailPhoto);
				Drawable d = Drawable.createFromStream(ims, null);
				iv_thumbnail.setImageDrawable(d);
			} catch (IOException e) {
				// FIXME proper error handling
				e.printStackTrace();
			}

			tv_name.setText(place.name);
			tv_about.setText(place.about);

			View.OnClickListener listener = clickTracker.getOnClickListener(v -> {
				Context view_context = v.getContext();
				Intent intent = new Intent(view_context, LandmarkDetailsActivity.class);
				intent.putExtra("placeName", place.name);
				view_context.startActivity(intent);
			});

			layout
				.findViewById(R.id.cardViewPlaceOfTheDay)
				.setOnClickListener(listener);
			layout
				.findViewById(R.id.cardViewPlaceOfTheDayDescription)
				.setOnClickListener(listener);

			return new PlaceOfTheDayHolder(layout);
		}

		/**
		 * Called when {@code RecyclerView} needs a new {@code RecyclerAdapter.CardViewHolder}.
		 *
		 * @param parent the {@code ViewGroup} into which the new {@code View} will be added after
		 * it is bound to an adapter position.
		 * @return a newly constructed {@code CardViewHolder}
		 */
		private CardViewHolder onCreateCardViewHolder(@NonNull ViewGroup parent) {
			// TODO make this a constant too somehow
			final double ASPECT_RATIO = 5.0 / 4;
			// TODO make all these constants in dimen
			final int LABEL_HEIGHT_DP = 49;
			final int LABEL_SIDE_MARGIN_DP = 5;
			final int CORNER_RADIUS_DP = 5;
			final int TEXT_SIZE_SP = 15;

			CardView card;
			CardView.LayoutParams label_layout;
			TextView label;

			label_layout = new CardView.LayoutParams(
				CardView.LayoutParams.MATCH_PARENT,
				dpToXp(LABEL_HEIGHT_DP)
			);
			label_layout.gravity = Gravity.BOTTOM;
			label_layout.leftMargin = dpToXp(LABEL_SIDE_MARGIN_DP);
			label_layout.rightMargin = dpToXp(LABEL_SIDE_MARGIN_DP);

			card = new CardView(parent.getContext());
			card.setRadius(dpToXp(CORNER_RADIUS_DP));

			label = new TextView(parent.getContext());
			label.setBackgroundColor(0xFFFFFFFF);
			// TODO load this from color.xml?
			label.setTextColor(0xFF162B46);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
			// this avoids font weight and looks similar enough
			label.setTypeface(Typeface.DEFAULT_BOLD);
			label.setGravity(Gravity.CENTER);
			label.setMaxLines(2);
			label.setEllipsize(TextUtils.TruncateAt.END);

			// requires label
			card.setOnClickListener(clickTracker.getOnClickListener(view -> {
				Context context = view.getContext();
				Intent intent = new Intent(context, LandmarkDetailsActivity.class);
				intent.putExtra("placeName", label.getText());
				context.startActivity(intent);
			}));

			SmartImageView image = new SmartImageView(
				parent.getContext(),
				null,
				ASPECT_RATIO,
				LABEL_HEIGHT_DP
			);

			card.addView(image);
			card.addView(label, label_layout);

			return new CardViewHolder(card, image, label);
		}

		/**
		 * Called by {@code RecyclerView} to display the data at the specified position.  Updates
		 * the contents of the {@code RecyclerView.ViewHolder.itemView} to reflect the item at the
		 * given position.
		 *
		 * @param holder the {@code ViewHolder} to be updated to represent the contents of the item
		 * at the given position in the data set
		 * @param position the position of the item within the adapter's data set
		 */
		@Override
		public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
			// TODO consider doing resizing here?
			// do nothing for PlaceOfTheDayHolder
			if (holder instanceof CardViewHolder) {
				CardViewHolder card_view_holder = (CardViewHolder) holder;
				int index = position - 1;
				card_view_holder.image.updatePhotoPath(landmarks[index].thumbnailPhoto);
				card_view_holder.label.setText(landmarks[index].name);
			}
		}

		/**
		 * Return the view type of the item at position for the purposes of view recycling.
		 *
		 * @param position position to query
		 * @return integer value identifying the type of the view needed to represent the item at
		 * position (type codes need not be contiguous)
		 */
		@Override
		public int getItemViewType(int position) {
			if (position == 0) return TYPE_PLACE_OF_THE_DAY_HOLDER;
			return TYPE_CARD_VIEW_HOLDER;
		}

		/**
		 * Returns the total number of items in the data set held by the adapter.
		 *
		 * @return the total number of items in the data set held by the adapter
		 */
		@Override
		public int getItemCount() {
			// Place of the Day header and cards
			return 1 + landmarks.length;
		}

		/**
		 * Returns the {@code SpanSizeLookup} to use.
		 *
		 * @return the {@code SpanSizeLookup} to use.
		 */
		public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
			return new GridLayoutManager.SpanSizeLookup() {
				/**
				 * Returns the number of spans occupied by the item at {@code position}.
				 *
				 * @param position the adapter position of the item
				 * @return the number of spans occupied by the item at the provided position
				 */
				@Override
				public int getSpanSize(int position) {
					switch (getItemViewType(position)) {
						case TYPE_PLACE_OF_THE_DAY_HOLDER:
							return 2;
						case TYPE_CARD_VIEW_HOLDER:
							return 1;
						default:
							throw new IllegalStateException("invalid item view type");
					}
				}
			};
		}
	}

	/**
	 * Decorates the item by padding them.
	 */
	public static class MarginItemDecoration extends RecyclerView.ItemDecoration {

		/**
		 * The margin to pad the items by.
		 */
		private int margin;

		/**
		 * Constructs a new {@code MarginItemDecoration} with a {@code margin}.
		 * @param margin the margin to pad the items by
		 */
		public MarginItemDecoration(int margin) {
			this.margin = margin;
		}

		/**
		 * Retrieve any offsets for the given item.  Pads them by {@code margin} on left and right
		 * and by {@code margin * 2} on the bottom.  Pads the first item (Place of the Day) on the
		 * bottom only.
		 *
		 * @param outRect rect to receive the output
		 * @param view the child view to decorate
		 * @param parent the {@code RecyclerView} this ItemDecoration is decorating
		 * @param state the current state of the {@code RecyclerView}
		 */
		public void getItemOffsets(
			@NonNull Rect outRect,
			@NonNull View view,
			@NonNull RecyclerView parent,
			@NonNull RecyclerView.State state
		) {
			if (parent.getChildAdapterPosition(view) == 0) {
				outRect.left = 0;
				outRect.right = 0;
			} else {
				outRect.left = margin;
				outRect.right = margin;
			}
			outRect.bottom = margin * 2;
		}
	}

	/**
	 * Hash function to get the place of the day.
	 *
	 * @param size the size of the array, must be at least 1
	 * @return the hash value (the index into the array)
	 */
	private int hashDate(int size) {
		Date date = new Date(System.currentTimeMillis());
		// Get day-of-the-year (1 to 366)
		int day = Integer.parseInt(String.format("%tj", date));
		int year = Integer.parseInt(String.format("%tY", date));
		// Adjust value for leap year
		if (year % 4 == 0) {
			if (day == 60) {
				day = day - size / 2;
			} else if (day > 60) {
				day = day - 1;
			}
		}
		return day % size;
	}

	/**
	 * Called on creation of this view and inflates the page.
	 *
	 * @param inflater the inflater used to inflate the page
	 * @param container ignored in this class, passed to super class constructor
	 * @param savedInstanceState ignored
	 */
	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		clickTracker = new ClickTracker();
		return inflater.inflate(R.layout.fragment_places_page, container, false);
	}

	/**
	 * Called after creation of this view and puts everything together.
	 *
	 * @param view the created view
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final int MARGIN_DP = 7;

		view
			.findViewById(R.id.placesSearchBarMask)
			.setOnClickListener(clickTracker.getOnClickListener(SearchBarActivity.class));

		SearchView searchView = view.findViewById(R.id.placesSearchView);
		searchView.setInputType(InputType.TYPE_NULL);
		ImageView search_mag_icon = searchView.findViewById(
				androidx.appcompat.R.id.search_mag_icon
		);
		ViewGroup search_view_parent = (ViewGroup) search_mag_icon.getParent();
		// the parent is probably a linear layout, so remove and add
		// to put the icon at the end instead of the front
		search_view_parent.removeView(search_mag_icon);
		search_view_parent.addView(search_mag_icon);

		ArrayList<Landmark> landmarks = LandmarkDatabase.getLocations(getContext());
		// empty the page on error
		// FIXME proper error handling
		if (landmarks == null) return;

		RecyclerView landmark_recycler_view = view.findViewById(R.id.landmark_recycler_view);
		GridLayoutManager layout_manager = new GridLayoutManager(getContext(), 2);
		RecyclerAdapter adapter = new RecyclerAdapter(landmarks.toArray(new Landmark[0]));

		landmark_recycler_view.setHasFixedSize(true);
		layout_manager.setSpanSizeLookup(adapter.getSpanSizeLookup());
		landmark_recycler_view.setLayoutManager(layout_manager);
		landmark_recycler_view.setAdapter(adapter);
		landmark_recycler_view.addItemDecoration(new MarginItemDecoration(dpToXp(MARGIN_DP)));

		// FIXME delete dead code
		// addLandmarks(getContext(), view, landmarks.toArray(new LandmarkInfo[0]));
	}

	/**
	 * Called on resume of this fragment and resets the {@code clickTracker}.
	 */
	@Override
	public void onResume() {
		super.onResume();
		clickTracker.reset();
	}

	/**
	 * Adds the landmarks provided to the table.  No checks are included for {@code null}s.
	 *
	 * @param context the context used to create children
	 * @param view the view to put children in
	 * @param landmarks the landmarks
	 */
	/*
	private void addLandmarks(Context context, View view, LandmarkInfo[] landmarks) {
		if (landmarks.length == 0) return;

		Resources res = getResources();
		final int ROW_MARGIN = res.getDimensionPixelSize(
			 R.dimen.places_page_fragment_grid_row_margin
		);
		final int BOTTOM_MARGIN = res.getDimensionPixelSize(
			 R.dimen.places_page_fragment_grid_bottom_margin
		);
		final int GAP = res.getDimensionPixelSize(R.dimen.places_page_fragment_grid_gap_margin);

		boolean odd = landmarks.length % 2 != 0;
		int length = odd ? landmarks.length - 1 : landmarks.length - 2;

		for (int i = 0; i < length; i += 2) {
			landmark_recycler_view.addView(getRow(
				context,
				ROW_MARGIN,
				GAP,
				landmarks[i],
				landmarks[i + 1]
			));
		}

		if (odd) {
			landmark_recycler_view.addView(getRow(
				context,
				BOTTOM_MARGIN,
				GAP,
				landmarks[landmarks.length - 1]
			));
		} else {
			landmark_recycler_view.addView(getRow(
				context,
				BOTTOM_MARGIN,
				GAP,
				landmarks[landmarks.length - 2],
				landmarks[landmarks.length - 1]
			));
		}
	}
	// FIXME remove dead code
	*/

	/**
	 * Returns a new TableRow composed of 1 to 2 landmark blocks.
	 * Behavior with any {@code null} or an incorrect number of {@code resId} is undefined.
	 *
	 * @param context the context used when creating children
	 * @param bottomMargin the bottom margin of this row
	 * @param gap the space in between the landmark blocks
	 * @param landmarks the landmarks, only 1 to 2 are supported
	 * @return the new TableRow
	 * @throws IllegalArgumentException when either {@code bottomMargin} or {@code gap} is negative
	 */
	/*
	FIXME remove dead code
	private TableRow getRow(
		Context context,
		final int bottomMargin,
		final int gap,
		LandmarkInfo... landmarks
	) {
		if (bottomMargin < 0 || gap < 0) throw new IllegalArgumentException();

		// TODO make this a constant too somehow
		final double ASPECT_RATIO = 5.0 / 4;
		// TODO make all these constants in dimen
		final int LABEL_HEIGHT_DP = 49;
		final int LABEL_SIDE_MARGIN_DP = 5;
		final int SIDE_MARGIN_DP = 5;
		final int CORNER_RADIUS_DP = 5;
		final int TEXT_SIZE_SP = 15;
		final int LABEL_ID = 1;

		TableRow row = new TableRow(context);

		TableRow.LayoutParams layout_start, layout_end;
		CardView.LayoutParams label_layout;

		layout_start = new TableRow.LayoutParams(0, 0, 1);
		layout_start.bottomMargin = bottomMargin;
		layout_start.setMarginStart(dpToXp(SIDE_MARGIN_DP));
		layout_start.setMarginEnd(gap / 2);

		layout_end = new TableRow.LayoutParams(0, 0, 1);
		layout_end.bottomMargin = bottomMargin;
		layout_end.setMarginStart(gap / 2);
		layout_end.setMarginEnd(dpToXp(SIDE_MARGIN_DP));

		label_layout = new CardView.LayoutParams(
			CardView.LayoutParams.MATCH_PARENT,
			dpToXp(LABEL_HEIGHT_DP)
		);
		label_layout.gravity = Gravity.BOTTOM;
		label_layout.leftMargin = dpToXp(LABEL_SIDE_MARGIN_DP);
		label_layout.rightMargin = dpToXp(LABEL_SIDE_MARGIN_DP);

		CardView card;
		TextView label;

		View.OnClickListener listener = view -> {
			if (!clickTracker.isClicked()) {
				clickTracker.click();
				Context view_context = view.getContext();
				Intent intent = new Intent(view_context, LandmarkDetailsActivity.class);
				intent.putExtra("placeName", ((TextView) view.findViewById(LABEL_ID)).getText());
				view_context.startActivity(intent);
			}
		};

		card = new CardView(context);
		card.setOnClickListener(listener);
		card.setRadius(dpToXp(CORNER_RADIUS_DP));

		label = new TextView(context);
		label.setId(LABEL_ID);
		label.setBackgroundColor(0xFFFFFFFF);
		label.setText(landmarks[0].name);
		label.setTextColor(0xFF162B46);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
		// this avoids font weight and looks similar enough
		label.setTypeface(Typeface.DEFAULT_BOLD);
		label.setGravity(Gravity.CENTER);
		label.setMaxLines(2);
		label.setEllipsize(TextUtils.TruncateAt.END);

		card.addView(new SmartImageView(
			context,
			landmarks[0].photoPath,
			ASPECT_RATIO,
			LABEL_HEIGHT_DP
		));
		card.addView(label, label_layout);
		row.addView(card, layout_start);

		card = new CardView(context);
		if (landmarks.length >= 2) {
			card.setOnClickListener(listener);
			card.setRadius(dpToXp(CORNER_RADIUS_DP));

			label = new TextView(context);
			label.setId(LABEL_ID);
			label.setBackgroundColor(0xFFFFFFFF);
			label.setText(landmarks[1].name);
			label.setTextColor(0xFF162B46);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
			// this avoids font weight and looks similar enough
			label.setTypeface(Typeface.DEFAULT_BOLD);
			label.setGravity(Gravity.CENTER);
			label.setMaxLines(2);
			label.setEllipsize(TextUtils.TruncateAt.END);

			card.addView(new SmartImageView(
				context,
				landmarks[1].photoPath,
				ASPECT_RATIO,
				LABEL_HEIGHT_DP
			));
			card.addView(label, label_layout);
		}
		row.addView(card, layout_end);

		row.setLayoutParams(
			new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT,
				1
			)
		);

		return row;
	}
	*/

	/**
	 * Converts the DP amount to XP.
	 *
	 * @param dp the DP amount
	 * @return the xp amount equivalent to {@code dp}
	 */
	private int dpToXp(int dp) {
		return (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP,
			dp,
			getResources().getDisplayMetrics()
		);
	}
}
