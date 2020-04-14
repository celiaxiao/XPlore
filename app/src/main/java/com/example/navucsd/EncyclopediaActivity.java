package com.example.navucsd;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This is an encyclopedia activity that contains a grid of landmarks.
 */
public final class EncyclopediaActivity extends AppCompatActivity {

	/**
	 * If the encyclopedia landmark list has been clicked, used to prevent multiple clicks.
	 */
	private boolean clicked;

	//search bar activity instances
	ArrayAdapter<String> LocationListAdapter;
	ArrayList<String> mustGoArrayList;
	SearchView searchBar;
	/**
	 * A smarter {@code ImageView} that automatically resizes its internal upon size change.
	 */
	private final class SmartImageView extends androidx.appcompat.widget.AppCompatImageView {
		/**
		 * The desired aspect ratio (width / height).
		 */
		private double aspectRatio;
		/**
		 * The last set width, defaults to 0 at first.
		 * Used to detect changes and decide when to resize.
		 */
		private int width;

		/**
		 * Constructs a {@code SmartImageView} with an image resource id and an aspect ratio.
		 * @param resId the resouce id of the image, if {@code null} the background is not set
		 * @param aspectRatio the desired aspect ratio (width / height)
		 */
		public SmartImageView(Integer resId, double aspectRatio) {
			super(EncyclopediaActivity.this);

			if (resId != null) setBackgroundResource(resId);

			this.getViewTreeObserver().addOnPreDrawListener(() -> {
				int new_width = getWidth();

				if (new_width != width) {
					ViewGroup.LayoutParams layout = getLayoutParams();

					layout.height = (int) (new_width / aspectRatio);
					width = new_width;
					if (resId != null) setBackground(resize(resId, width, layout.height));
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
		/*
		//implement search bar activity
		//set up the string list that appears in the search bar hint
		String[] mustGoList = getResources( ).getStringArray(R.array.list_of_must_go);
		mustGoArrayList = new ArrayList<>( );
		for (String i : mustGoList) {
			mustGoArrayList.add(i);
		}
		LocationListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mustGoList);

		ListView searchHintList = (ListView) findViewById(R.id.searchHintList);
		searchHintList.setBackgroundColor(Color.WHITE);
		searchHintList.setAdapter(LocationListAdapter);

		searchBar = (SearchView) findViewById(R.id.search_bar);
	}
		//first hide the suggestion listview
		searchHintList.setVisibility(View.GONE);



		searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
			// Override onQueryTextSubmit method
			// which is call
			// when submitquery is searched

			@Override
			public boolean onQueryTextSubmit(String query) {
				// If the list contains the search query
				// than filter the adapter
				// using the filter method
				// with the query as its argument
				if (mustGoArrayList.contains(query)) {
					LocationListAdapter.getFilter( ).filter(query);
					//TODO: set to intent if needed
				} else {
					// Search query not found in List View

					alertDialog();
				}
				return false;
			}

			// This method is overridden to filter
			// the adapter according to a search query
			// when the user is typing search
			@Override
			public boolean onQueryTextChange(String s) {
				if(s.isEmpty()){
					searchHintList.setVisibility(View.GONE);
				}
				else searchHintList.setVisibility(View.VISIBLE);
				LocationListAdapter.getFilter( ).filter(s);

				//set up clike item functionality
				searchHintList.setOnItemClickListener(new ListView.OnItemClickListener( ) {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

						//if user clicks the suggestion, auto complete the search bar
						searchBar.setQuery(LocationListAdapter.getItem(i), true);
						//hide the listview
						searchHintList.setVisibility(View.GONE);
						//TODO: set to intent if needed
					}
				});
				return false;
			}
		});

	}
	private void alertDialog() {
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setMessage("Sorry we can't find your input location. Explore our main page?");
		dialog.setTitle("Location not found");
		dialog.setPositiveButton("Comfirm",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int which) {

					}
				});

		AlertDialog alertDialog=dialog.create();
		alertDialog.show();

	}
*/
	/**
	 * Called on resume of this activity and resets the {@code clicked} attribute
	 */
	@Override
	protected void onResume() {
		super.onResume();
		clicked = false;
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
	 * Returns a new TableRow composed of 1 to 2 images.
	 * Behavior with a {@code null} or an incorrect number of {@code resId} is undefined.
	 * @param resId the resource ids of the images, only 1 to 2 are supported
	 * @return the new TableRow
	 */
	private TableRow getImageRow(int... resId) {
		final double ASPECT_RATIO = 5.0 / 4;
		final int HALF_MARGIN_DP = halfMargin();

		TableRow row = new TableRow(this);

		TableRow.LayoutParams layout_start, layout_end;

		// set the start layout
		layout_start = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT, // XXX 0?
				0,
				1
		);
		layout_start.setMarginEnd(dpToXp(HALF_MARGIN_DP));

		// set the end layout
		layout_end = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT, // XXX 0?
				0,
				1
		);
		layout_end.setMarginStart(dpToXp(HALF_MARGIN_DP));

		SmartImageView image;

		// add start column image
		image = new SmartImageView(resId[0], ASPECT_RATIO);
		image.setOnClickListener(getOnClickListener());
		row.addView(image, layout_start);

		// add end column image
		image = new SmartImageView(resId.length == 2 ? resId[1] : null, ASPECT_RATIO);
		image.setOnClickListener(getOnClickListener());
		row.addView(image, layout_end);

		// set row layout
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
		label.setOnClickListener(getOnClickListener());
		label_row.addView(label, layout_start);

		// add the second label
		label = new TextView(this);
		if (names.length >= 2) {
			label.setBackgroundColor(0xFFEBECED);
			label.setText(names[1]);
			label.setTextColor(0xFF000000);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
			label.setGravity(Gravity.CENTER);
			label.setOnClickListener(getOnClickListener());
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

	/**
	 * Gets the constant for half of the margin.
	 * @return the half margin constant
	 */
	private int halfMargin() {
		return 10 / 2;
	}
}