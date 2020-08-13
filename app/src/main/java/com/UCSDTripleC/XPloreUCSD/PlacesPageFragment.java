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
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * This is the Places page which contains some places and a grid of landmarks.
 */
public final class PlacesPageFragment extends Fragment {

	/**
	 * The type ID of a {@code PlaceOfTheDayHolder}.
	 */
	private static final byte TYPE_PLACE_OF_THE_DAY = 1;

	/**
	 * The type ID of a {@code AlphabetHolder}.
	 */
	private static final byte TYPE_ALPHABET = 2;

	/**
	 * The type ID of a {@code CardViewHolder}.
	 */
	private static final byte TYPE_CARD = 3;

	/**
	 * Tracks if this page has been clicked; used to prevent multiple clicks.
	 */
	private ClickTracker clickTracker;

	/**
	 * Caches the images loaded from disk to prevent lag spikes when scrolling.
	 */
	private HashMap<String, SoftReference<Drawable>> imageCache;

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
		 * If {@code photoPath} has been updated.
		 */
		private boolean photoPathDirty;

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

				if (new_width != width || photoPathDirty) {
					ViewGroup.LayoutParams layout = getLayoutParams();

					layout.height = (int) (new_width / aspectRatio);
					width = new_width;

					if (this.photoPath != null) {
						SoftReference<Drawable> ref = imageCache.get(this.photoPath);
						Drawable image = ref != null ? ref.get() : null;
						if (image == null) {
							image = load(context, this.photoPath, width, layout.height);
						}
						// FIXME proper error handling
						if (image != null) setBackground(image);
					}

					// set the size of the parent instead of self as parent doesn't know its height
					// should change as a result of changes in its children
					View parent = (View) getParent();
					ViewGroup.LayoutParams params = parent.getLayoutParams();
					params.height = layout.height + dpToXp(labelHeight);
					parent.setLayoutParams(params);

					photoPathDirty = false;

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
			this.photoPathDirty = true;
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
		 * The data set.
		 */
		private Landmark[] landmarks;

		/**
		 * The total number of elements.
		 */
		private int size;

		/**
		 * The array mapping position to type.
		 */
		private byte[] types;

		/**
		 * Maps the position of a landmark to the index.
		 */
		private int[] indices;

		/**
		 * Maps the position of a given alphabet section to its letter.
		 */
		private char[] letters;

		/**
		 * The letter of the alphabet separating ranges of cards.
		 */
		public class AlphabetHolder extends RecyclerView.ViewHolder {

			/**
			 * The letter.
			 */
			public TextView letter;

			/**
			 * Constructs a new {@code AlphabetHolder}.
			 *
			 * @param letter the letter
			 */
			public AlphabetHolder(TextView letter) {
				super(letter);
				this.letter = letter;
			}
		}

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

			Arrays.sort(landmarks);

			// place of the day, #, A-Z, landmarks
			int capacity = 1 + 1 + 26 + landmarks.length;

			types = new byte[capacity];
			letters = new char[capacity];
			indices = new int[capacity];

			int index = 0;
			char last = '\0';

			types[0] = TYPE_PLACE_OF_THE_DAY;
			size = 1;

			for (Landmark landmark: landmarks) {
				// assume the names are not null and have at least 1 character
				// and assume they can only be 0-9A-Za-z
				char c = landmark.getName().charAt(0);
				if (c >= '0' && c <= '9') {
					c = '#';
				} else if (c >= 'a' && c <= 'z') {
					c -= 'a' + 'A';
				}
				if (c != last) {
					last = c;
					types[size] = TYPE_ALPHABET;
					letters[size] = c;
					++size;
				}
				types[size] = TYPE_CARD;
				indices[size] = index++;
				++size;
			}
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
				case TYPE_PLACE_OF_THE_DAY: return onCreatePlaceOfTheDayHolder(parent);
				case TYPE_ALPHABET: return onCreateAlphabetHolder(parent);
				case TYPE_CARD: return onCreateCardViewHolder(parent);
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

			ImageView restroom = layout.findViewById(R.id.places_page_place_of_the_day_restroom);
			ImageView cafe = layout.findViewById(R.id.places_page_place_of_the_day_cafe);
			ImageView restaurant =
				layout.findViewById(R.id.places_page_place_of_the_day_restaurant);
			ImageView bus_stop = layout.findViewById(R.id.places_page_place_of_the_day_bus_stop);
			ImageView parking = layout.findViewById(R.id.places_page_place_of_the_day_parking);
			ImageView thumbnail = layout.findViewById(R.id.places_page_place_of_the_day_thumbnail);
			TextView name = layout.findViewById(R.id.places_page_place_of_the_day_name);
			TextView about = layout.findViewById(R.id.places_page_place_of_the_day_about);

			int color = ContextCompat.getColor(layout.getContext(), R.color.colorPrimaryDark);

			// FIXME proper error handling
			if (place.getAmenities() != null) {
				Boolean result;
				result = place.getAmenities().get("restroom");
				if (result != null && result) restroom.setColorFilter(color);
				result = place.getAmenities().get("cafe");
				if (result != null && result) cafe.setColorFilter(color);
				result = place.getAmenities().get("restaurant");
				if (result != null && result) restaurant.setColorFilter(color);
				result = place.getAmenities().get("busstop");
				if (result != null && result) bus_stop.setColorFilter(color);
				result = place.getAmenities().get("parking");
				if (result != null && result) parking.setColorFilter(color);
			}

			try {
				InputStream ims = parent.getContext().getAssets().open(place.getThumbnailPhoto());
				Drawable d = Drawable.createFromStream(ims, null);
				thumbnail.setImageDrawable(d);
			} catch (IOException e) {
				// FIXME proper error handling
				e.printStackTrace();
			}

			name.setText(place.getName());
			about.setText(place.getAbout());

			View.OnClickListener listener = clickTracker.getOnClickListener(v -> {
				Context view_context = v.getContext();
				Intent intent = new Intent(view_context, LandmarkDetailsActivity.class);
				intent.putExtra("placeName", place.getName());
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
		 * Called when {@code RecyclerView} needs a new {@code RecyclerAdapter.AlphabetHolder}.
		 *
		 * @param parent the {@code ViewGroup} into which the new {@code View} will be added after
		 * it is bound to an adapter position
		 * @return a newly constructed {@code AlphabetHolder}
		 */
		private AlphabetHolder onCreateAlphabetHolder(@NonNull ViewGroup parent) {
			// TODO make all these constants in dimen
			final int TEXT_SIZE_SP = 18;

			TextView letter;

			letter = new TextView(parent.getContext());
			letter.setBackgroundColor(0xFFFFFFFF);
			// TODO load this from color.xml?
			letter.setTextColor(0xFF162B46);
			letter.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
			// this avoids font weight and looks similar enough
			letter.setTypeface(Typeface.DEFAULT_BOLD);

			return new AlphabetHolder(letter);
		}

		/**
		 * Called when {@code RecyclerView} needs a new {@code RecyclerAdapter.CardViewHolder}.
		 *
		 * @param parent the {@code ViewGroup} into which the new {@code View} will be added after
		 * it is bound to an adapter position
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
			// do nothing for PlaceOfTheDayHolder
			if (holder instanceof CardViewHolder) {
				CardViewHolder card_view_holder = (CardViewHolder) holder;
				Landmark landmark = landmarks[indices[position]];
				card_view_holder.image.updatePhotoPath(landmark.getThumbnailPhoto());
				card_view_holder.label.setText(landmark.getName());
			} else if (holder instanceof AlphabetHolder) {
				AlphabetHolder alphabet_holder = (AlphabetHolder) holder;
				alphabet_holder.letter.setText(String.valueOf(letters[position]));
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
			return types[position];
		}

		/**
		 * Returns the total number of items in the data set held by the adapter.
		 *
		 * @return the total number of items in the data set held by the adapter
		 */
		@Override
		public int getItemCount() {
			// Place of the Day header, letters, and cards
			return size;
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
						case TYPE_ALPHABET:
						case TYPE_PLACE_OF_THE_DAY:
							return 2;
						case TYPE_CARD:
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
	public class MarginItemDecoration extends RecyclerView.ItemDecoration {

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
		 * and by {@code margin * 2} on the bottom.  Pads Place of the Day on the bottom only.
		 * Pads the letters on the left, top, and bottom.
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
			// TODO make it a constant in XML
			final int LABEL_MARGIN_DP = 16;
			// adapter must not be null
			//noinspection ConstantConditions
			switch (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(view))) {
				case TYPE_PLACE_OF_THE_DAY:
					outRect.left = 0;
					outRect.right = 0;
					outRect.bottom = margin * 2;
					break;
				case TYPE_ALPHABET:
					outRect.left = margin;
					outRect.top = dpToXp(LABEL_MARGIN_DP);
					outRect.bottom = dpToXp(LABEL_MARGIN_DP);
					break;
				case TYPE_CARD:
					outRect.left = margin;
					outRect.right = margin;
					outRect.bottom = margin * 2;
					break;
				default:
					throw new IllegalStateException("view type must be valid");
			}
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
		imageCache = new HashMap<>();
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
	 * Get a resized image.
	 *
	 * @param src the source of the bitmap, must not be {@code null}
	 * @param width the target width of the image
	 * @param height the target height of the image
	 * @return the resized image
	 */
	private Drawable resize(@NonNull Bitmap src, int width, int height) {
		return new BitmapDrawable(
			getResources(),
			Bitmap.createScaledBitmap(src, width, height, true
		));
	}

	/**
	 * Loads an image from disk.
	 *
	 * @param context the context to use, must not be {@code null}
	 * @param photoPath the path to the image, must not be {@code null}
	 * @param width the width of the image to use
	 * @param height the height of the image to use
	 * @return the image, or {@code null} on error
	 */
	public Drawable load(
		@NonNull Context context,
		@NonNull String photoPath,
		int width,
		int height
	) {
		Drawable image = null;
		try {
			InputStream input = context.getAssets().open(photoPath);
			Bitmap src = BitmapFactory.decodeStream(input);
			image = resize(src, width, height);
			imageCache.put(photoPath, new SoftReference<>(image));
		} catch (IOException e) {
			// ignore the error and return null
		}
		return image;
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
