package com.example.navucsd;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Objects;

/**
 * This is an encyclopedia activity that contains a grid of landmarks.
 */
public class EncyclopediaActivity extends AppCompatActivity {

	// TODO support fixed aspect ratio
	/**
	 * Called on creation of this activity and sets up everything at once.
	 * @param savedInstanceState ignored in this class, passed to super class constructor
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encyclopedia);

		TableLayout landmarkTableLayout = findViewById(R.id.landmarkTableLayout);

		int res_ids[] = {
				R.drawable.geisel,
				R.drawable.peterson,
				R.drawable.mayer,
				R.drawable.price_center_east,
				R.drawable.rady,
				R.drawable.geisel,
				R.drawable.peterson,
				R.drawable.mayer,
				R.drawable.price_center_east,
		};
		String names[] = {
				"Geisel Library",
				"Peterson Hall",
				"Mayer Hall",
				"Price Center East",
				"Rady School of Mgt",
				"Geisel Library",
				"Peterson Hall",
				"Mayer Hall",
				"Price Center East",
		};

		addLandmarks(res_ids, names);
	}

	/**
	 * Adds the landmarks provided to the encyclopedia table.
	 * If {@code resId} and {@code names} are {@code null}, nothing will be added.
	 * @param resId the resource ids of the images
	 * @param names the names of the landmarks
	 * @throws IllegalArgumentException if {@code resId} and {@code names} are of different size or
	 * one of them is null
	 * @throws NullPointerException if {@code names} contains any {@code null} element
	 */
	private void addLandmarks(int[] resId, String[] names) {
		if (resId == null && names == null) return;
		if (resId == null || names == null) {
			throw new IllegalArgumentException("one of resId and names is null");
		}
		if (resId.length != names.length) {
			throw new IllegalArgumentException("resId and names must have the same size");
		}
		if (resId.length == 0) return;

		TableLayout landmarkTableLayout = findViewById(R.id.landmarkTableLayout);
		boolean odd = resId.length % 2 != 0;
		int length = odd ? resId.length - 1 : resId.length - 2;

		for (int i = 0; i < length; i += 2) {
			Objects.requireNonNull(names[i], "names contains a null element at index " + i);
			landmarkTableLayout.addView(getImageRow(resId[i], resId[i + 1]));
			landmarkTableLayout.addView(getLabelRow(true, names[i], names[i + 1]));
		}

		if (odd) {
			landmarkTableLayout.addView(getImageRow(resId[resId.length - 1]));
			landmarkTableLayout.addView(getLabelRow(false, names[names.length - 1]));
		} else {
			landmarkTableLayout.addView(
				getImageRow(
					resId[resId.length - 2],
					resId[resId.length - 1]
				)
			);
			landmarkTableLayout.addView(
				getLabelRow(
					false,
					names[names.length - 2],
					names[names.length - 1]
				)
			);
		}
	}

	/**
	 * Returns a new TableRow composed of 1 to 2 {@code ImageView}s.
	 * Behavior with a {@code null} or an incorrect number of {@code resId} is undefined.
	 * @param resId the resource ids of the images, only 1 to 2 are supported
	 * @return the new TableRow
	 */
	private TableRow getImageRow(int... resId) {
		final int IMAGE_HEIGHT_DP = 135;
		final int HALF_MARGIN_DP = halfMargin();

		TableRow row = new TableRow(this);
		ImageView image;

		TableRow.LayoutParams layout_start, layout_end;

		// sets the start layout
		layout_start = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT, // XXX 0?
				dpToXp(IMAGE_HEIGHT_DP),
				1
		);
		layout_start.setMarginEnd(dpToXp(HALF_MARGIN_DP));

		// sets the end layout
		layout_end = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT, // XXX 0?
				dpToXp(IMAGE_HEIGHT_DP),
				1
		);
		layout_end.setMarginStart(dpToXp(HALF_MARGIN_DP));

		// add the first image
		image = new ImageView(this);
		image.setBackgroundResource(resId[0]);
		row.addView(image, layout_start);

		// add the second image
		image = new ImageView(this);
		if (resId.length >= 2) image.setBackgroundResource(resId[1]);
		row.addView(image, layout_end);

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
	 * Returns a new TableRow composed of 1 to 2 {@code TextView}s.
	 * Behavior with a {@code null} or an invalid number of arguments is undefined.
	 * @param bottomMargin to add bottom margin to separate rows or not
	 * @param names the names of the images displayed on the labels
	 * @return the new TableRow
	 */
	private TableRow getLabelRow(boolean bottomMargin, String... names) {
		final int LABEL_HEIGHT_DP = 30;
		final int TEXT_SIZE_SP = 14;
		final int HALF_MARGIN_DP = halfMargin();

		TableRow label_row = new TableRow(this);
		TextView label;

		TableRow.LayoutParams layout_start, layout_end;

		// sets the start layout
		layout_start = new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				dpToXp(LABEL_HEIGHT_DP),
				1
		);
		layout_start.setMarginEnd(dpToXp(HALF_MARGIN_DP));

		// sets the end layout
		layout_end = new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				dpToXp(LABEL_HEIGHT_DP),
				1
		);
		layout_end.setMarginStart(dpToXp(HALF_MARGIN_DP));

		// add the first label
		label = new TextView(this);
		label.setBackgroundColor(0xFFEBECED);
		label.setText(names[0]);
		label.setTextColor(0xFF000000);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
		label.setGravity(Gravity.CENTER);
		label_row.addView(label, layout_start);

		// add the second label
		label = new TextView(this);
		if (names.length >= 2) {
			label.setBackgroundColor(0xFFEBECED);
			label.setText(names[1]);
			label.setTextColor(0xFF000000);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
			label.setGravity(Gravity.CENTER);
		}
		label_row.addView(label, layout_end);

		TableLayout.LayoutParams row_layout;
		row_layout = new TableLayout.LayoutParams(
			TableLayout.LayoutParams.MATCH_PARENT,
			TableLayout.LayoutParams.WRAP_CONTENT
		);
		if (bottomMargin) row_layout.bottomMargin = dpToXp(halfMargin() * 2);

		label_row.setLayoutParams(row_layout);

		return label_row;
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

	/**
	 * Gets the constant for half of the margin.
	 * @return the half margin constant
	 */
	private int halfMargin() {
		return 10 / 2;
	}
}
