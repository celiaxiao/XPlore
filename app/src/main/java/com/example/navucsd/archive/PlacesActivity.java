package com.example.navucsd.archive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.navucsd.LandmarkDetailsActivity;
import com.example.navucsd.R;

import java.util.Objects;

/**
 * This is an places activity that contains some places and a grid of landmarks.
 */
public final class PlacesActivity extends AppCompatActivity {

	/**
	 * If the landmark list has been clicked, used to prevent multiple clicks.
	 */
	private boolean clicked;

	/**
	 * A landmark block, consisting of an image resource and a name.
	 */
	private static final class LandmarkInfo {
		/**
		 * The resource ID of the image of this landmark.
		 */
		private int resId;

		/**
		 * The name of this landmark.
		 */
		private String name;

		/**
		 * A convenient constructor.
		 *
		 * @param resId the resource id of the image
		 * @param name the name of this landmark
		 */
		public LandmarkInfo(int resId, String name) {
			this.resId = resId;
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
		 * @param resId the resouce id of the image, if {@code null} the background is not set
		 * @param aspectRatio the desired aspect ratio (width / height)
		 * @param labelHeight the height of the label, in dp
		 */
		public SmartImageView(int resId, double aspectRatio, int labelHeight) {
			super(PlacesActivity.this);

			setBackgroundResource(resId);

			this.getViewTreeObserver().addOnPreDrawListener(() -> {
				int new_width = getWidth();

				if (new_width != width) {
					ViewGroup.LayoutParams layout = getLayoutParams();

					layout.height = (int) (new_width / aspectRatio);
					width = new_width;
					setBackground(resize(resId, width, layout.height));

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
		 * @param resId the resource id of the image
		 * @param width the target width of the image
		 * @param height the target height of the image
		 * @return the resized image
		 */
		private Drawable resize(int resId, int width, int height) {
			return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(
					((BitmapDrawable) getResources().getDrawable(resId)).getBitmap(), width, height,
					true));
		}
	}

	/**
	 * Called on creation of this activity and sets up everything at once.
	 * @param savedInstanceState ignored in this class, passed to super class constructor
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);

//		findViewById(R.id.placesSearchBarMask).setOnClickListener(view -> {
//			if (!clicked) {
//				clicked = true;
//				startActivity(new Intent(PlacesActivity.this, SearchBarActivity.class));
//			}
//		});

		SearchView searchView = findViewById(R.id.placesSearchView);
		searchView.setInputType(InputType.TYPE_NULL);
		ImageView search_mag_icon = searchView.findViewById(
				androidx.appcompat.R.id.search_mag_icon
		);
		ViewGroup search_view_parent = (ViewGroup) search_mag_icon.getParent();
		// the parent is probably a linear layout, so remove and add
		// to put the icon at the end instead of the front
		search_view_parent.removeView(search_mag_icon);
		search_view_parent.addView(search_mag_icon);

		TableLayout landmarkTableLayout = findViewById(R.id.landmarkTableLayout);

		LandmarkInfo landmarks[] = {
			new LandmarkInfo(R.drawable.geisel, "Geisel Library"),
			new LandmarkInfo(R.drawable.peterson, "Peterson Hall"),
			new LandmarkInfo(R.drawable.mayer, "Mayer Hall"),
			new LandmarkInfo(R.drawable.price_center_east, "Price Center East"),
			new LandmarkInfo(R.drawable.rady, "Rady School of Mgt"),
			new LandmarkInfo(R.drawable.geisel, "Geisel Library"),
			new LandmarkInfo(R.drawable.peterson, "Peterson Hall"),
			new LandmarkInfo(R.drawable.mayer, "Mayer Hall"),
			new LandmarkInfo(R.drawable.price_center_east, "Price Center East"),
		};

		addLandmarks(landmarks);
	}

	/**
	 * Called on resume of this activity and resets the {@code clicked} attribute
	 */
	@Override
	protected void onResume() {
		super.onResume();
		clicked = false;
	}

	/**
	 * Adds the landmarks provided to the table.
	 *
	 * @param landmarks the landmarks
	 * @throws NullPointerException if {@code landmarks} is {@code null}, but the elements are not checked
	 */
	private void addLandmarks(LandmarkInfo[] landmarks) {
		Objects.requireNonNull(landmarks, "landmark must not be null");
		if (landmarks.length == 0) return;

		final int BOTTOM_MARGIN_DP = 10, VERY_BOTTOM_MARGIN_DP = 5, GAP_DP = 10;

		TableLayout landmarkTableLayout = findViewById(R.id.landmarkTableLayout);
		boolean odd = landmarks.length % 2 != 0;
		int length = odd ? landmarks.length - 1 : landmarks.length - 2;

		for (int i = 0; i < length; i += 2) {
			landmarkTableLayout.addView(getRow(BOTTOM_MARGIN_DP, GAP_DP, landmarks[i], landmarks[i + 1]));
		}

		if (odd) {
			landmarkTableLayout.addView(getRow(VERY_BOTTOM_MARGIN_DP, GAP_DP, landmarks[landmarks.length - 1]));
		} else {
			landmarkTableLayout.addView(
				getRow(
					VERY_BOTTOM_MARGIN_DP,
					GAP_DP,
					landmarks[landmarks.length - 2],
					landmarks[landmarks.length - 1]
				)
			);
			landmarkTableLayout.addView(
				getRow(
					VERY_BOTTOM_MARGIN_DP,
					GAP_DP,
					landmarks[landmarks.length - 2],
					landmarks[landmarks.length - 1]
				)
			);
		}
	}

	/**
	 * Returns a new TableRow composed of 1 to 2 landmark blocks.
	 * Behavior with a {@code null} or an incorrect number of {@code resId} is undefined.
	 * @param bottomMargin the bottom margin of this row, in dp
	 * @param gap the space in between the landmark blocks, in dp
	 * @param landmarks the landmarks, only 1 to 2 are supported
	 * @return the new TableRow
	 * @throws IllegalArgumentException when {@code gap} is not a multiple of 2 or
	 * either {@code bottomMargin} or {@code gap} is negative
	 */
	private TableRow getRow(final int bottomMargin, final int gap, LandmarkInfo... landmarks) {
		if (bottomMargin < 0 || gap < 0 || gap % 2 == 1) throw new IllegalArgumentException();

		final double ASPECT_RATIO = 5.0 / 4;
		final int LABEL_HEIGHT_DP = 30, SIDE_MARGIN_DP = 5, CORNER_RADIUS_DP = 5;
		final int TEXT_SIZE_SP = 15;

		TableRow row = new TableRow(this);

		TableRow.LayoutParams layout_start, layout_end;
		CardView.LayoutParams label_layout;

		layout_start = new TableRow.LayoutParams(
			TableRow.LayoutParams.WRAP_CONTENT,
			TableRow.LayoutParams.WRAP_CONTENT,
			1
		);
		layout_start.bottomMargin = dpToXp(bottomMargin);
		layout_start.setMarginStart(dpToXp(SIDE_MARGIN_DP));
		layout_start.setMarginEnd(dpToXp(gap / 2));

		layout_end = new TableRow.LayoutParams(
			TableRow.LayoutParams.WRAP_CONTENT,
			TableRow.LayoutParams.WRAP_CONTENT,
			1
		);
		layout_end.bottomMargin = dpToXp(bottomMargin);
		layout_end.setMarginStart(dpToXp(gap / 2));
		layout_end.setMarginEnd(dpToXp(SIDE_MARGIN_DP));

		label_layout = new CardView.LayoutParams(
			CardView.LayoutParams.MATCH_PARENT,
			dpToXp(LABEL_HEIGHT_DP)
		);
		label_layout.gravity = Gravity.BOTTOM;

		CardView card;
		TextView label;

		card = new CardView(this);
		card.setOnClickListener(getOnClickListener());
		card.setRadius(dpToXp(CORNER_RADIUS_DP));

		label = new TextView(this);
		label.setBackgroundColor(0xFFFFFFFF);
		label.setText(landmarks[0].name);
		label.setTextColor(0xFF162B46);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
		label.setTypeface(Typeface.DEFAULT_BOLD); // this avoids font weight and looks similar enough
		label.setGravity(Gravity.CENTER);
		label.setOnClickListener(getOnClickListener());

		card.addView(new SmartImageView(landmarks[0].resId, ASPECT_RATIO, LABEL_HEIGHT_DP));
		card.addView(label, label_layout);
		row.addView(card, layout_start);

		card = new CardView(this);
		if (landmarks.length >= 2) {
			card.setOnClickListener(getOnClickListener());
			card.setRadius(dpToXp(CORNER_RADIUS_DP));

			label = new TextView(this);
			label.setBackgroundColor(0xFFFFFFFF);
			label.setText(landmarks[1].name);
			label.setTextColor(0xFF162B46);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
			label.setTypeface(Typeface.DEFAULT_BOLD); // this avoids font weight and looks similar enough
			label.setGravity(Gravity.CENTER);
			label.setOnClickListener(getOnClickListener());

			card.addView(new SmartImageView(landmarks[1].resId, ASPECT_RATIO, LABEL_HEIGHT_DP));
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
	 * Get the @{code OnClickListener} that starts the {@code LandmarkDetailsActivity}.
	 * @return the @{code OnClickListener} that starts the {@code LandmarkDetailsActivity}.
	 */
	// TODO target argument
	private View.OnClickListener getOnClickListener() {
		return view -> {
			if (!clicked) {
				clicked = true;
				startActivity(new Intent(this, LandmarkDetailsActivity.class));
			}
		};
	}

	/**
	 * Converts the DP amount to XP.
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
