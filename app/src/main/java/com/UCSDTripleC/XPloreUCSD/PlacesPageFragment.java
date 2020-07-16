package com.UCSDTripleC.XPloreUCSD;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
	 * A landmark block, consisting of an image resource and a name.
	 */
	private static final class LandmarkInfo {
		/**
		 * The resource ID of the image of this landmark.
		 */
		private String photoPath;

		/**
		 * The name of this landmark.
		 */
		private String name;

		/**
		 * A convenient constructor.
		 *
		 * @param photoPath the path to the image
		 * @param name the name of this landmark
		 */
		@SuppressWarnings("WeakerAccess")
		public LandmarkInfo(String photoPath, String name) {
			this.photoPath = photoPath;
			this.name = name;
		}
	}

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
		 * Constructs a {@code SmartImageView} with an image resource id and an aspect ratio.  It
		 * also resizes the parent to the height of this image view plus the height of the label.
		 *
		 * @param context the context used to create this instance
		 * @param photoPath the path of the image, if {@code null} the background is not set
		 * @param aspectRatio the desired aspect ratio (width / height)
		 * @param labelHeight the height of the label, in dp
		 */
		public SmartImageView(
			Context context,
			String photoPath,
			double aspectRatio,
			int labelHeight
		) {
			super(context);

			this.getViewTreeObserver().addOnPreDrawListener(() -> {
				int new_width = getWidth();

				if (new_width != width) {
					ViewGroup.LayoutParams layout = getLayoutParams();

					layout.height = (int) (new_width / aspectRatio);
					width = new_width;

					InputStream ims = null;
					try {
						ims = context.getAssets().open(photoPath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Bitmap src = BitmapFactory.decodeStream(ims);

					setBackground(resize(src, width, layout.height));

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
		 * Get a resized image.
		 *
		 * @param src the source of the bitmap
		 * @param width the target width of the image
		 * @param height the target height of the image
		 * @return the resized image
		 */
		private Drawable resize(Bitmap src, int width, int height) {
			return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(src, width, height, true));
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

		ArrayList<Landmark> locations = LandmarkDatabase.getLocations(getContext());
		if (locations == null) return;
		Landmark place = locations.get(hashDate(locations.size()));

		ImageView iv_restroom = view.findViewById(R.id.places_page_place_of_the_day_restroom);
		ImageView iv_cafe = view.findViewById(R.id.places_page_place_of_the_day_cafe);
		ImageView iv_restaurant = view.findViewById(R.id.places_page_place_of_the_day_restaurant);
		ImageView iv_bus_stop = view.findViewById(R.id.places_page_place_of_the_day_bus_stop);
		ImageView iv_parking = view.findViewById(R.id.places_page_place_of_the_day_parking);
		ImageView iv_thumbnail = view.findViewById(R.id.places_page_place_of_the_day_thumbnail);
		TextView tv_name = view.findViewById(R.id.places_page_place_of_the_day_name);
		TextView tv_about = view.findViewById(R.id.places_page_place_of_the_day_about);

		int color = ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark);

		if (place.amenities.get("restroom")) iv_restroom.setColorFilter(color);
		if (place.amenities.get("cafe")) iv_cafe.setColorFilter(color);
		if (place.amenities.get("restaurant")) iv_restaurant.setColorFilter(color);
		if (place.amenities.get("busstop")) iv_bus_stop.setColorFilter(color);
		if (place.amenities.get("parking")) iv_parking.setColorFilter(color);

		try {
			InputStream ims = getContext().getAssets().open(place.thumbnailPhoto);
			Drawable d = Drawable.createFromStream(ims, null);
			iv_thumbnail.setImageDrawable(d);
		} catch(IOException e) {
			e.printStackTrace();
		}

		tv_name.setText(place.name);
		tv_about.setText(place.about);

		View.OnClickListener listener = v -> {
			if (!clickTracker.isClicked()) {
				clickTracker.click();
				Context view_context = v.getContext();
				Intent intent = new Intent(view_context, LandmarkDetailsActivity.class);
				intent.putExtra("placeName", place.name);
				view_context.startActivity(intent);
			}
		};

		view
			.findViewById(R.id.cardViewPlaceOfTheDay)
			.setOnClickListener(listener);
		view
			.findViewById(R.id.cardViewPlaceOfTheDayDescription)
			.setOnClickListener(listener);

		ArrayList<LandmarkInfo> landmarks = new ArrayList<>(locations.size());

		for (Landmark loc : locations) {
			 landmarks.add(new LandmarkInfo(loc.thumbnailPhoto, loc.name));
		}

		addLandmarks(getContext(), view, landmarks.toArray(new LandmarkInfo[0]));
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

		TableLayout landmarkTableLayout = view.findViewById(R.id.landmarkTableLayout);
		boolean odd = landmarks.length % 2 != 0;
		int length = odd ? landmarks.length - 1 : landmarks.length - 2;

		for (int i = 0; i < length; i += 2) {
			landmarkTableLayout.addView(getRow(
				context,
				ROW_MARGIN,
				GAP,
				landmarks[i],
				landmarks[i + 1]
			));
		}

		if (odd) {
			landmarkTableLayout.addView(getRow(
				context,
				BOTTOM_MARGIN,
				GAP,
				landmarks[landmarks.length - 1]
			));
		} else {
			landmarkTableLayout.addView(getRow(
				context,
				BOTTOM_MARGIN,
				GAP,
				landmarks[landmarks.length - 2],
				landmarks[landmarks.length - 1]
			));
		}
	}

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
