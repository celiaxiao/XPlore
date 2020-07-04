package com.UCSDTripleC.XPloreUCSD.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * A tracker of clicks to prevent multiple clicks.
 */
public final class ClickTracker {
	/**
	 * The variable tracking if clicked or not.
	 */
	private boolean clicked = false;

	/**
	 * Marks this tracker as clicked.
	 */
	public void click() {
		clicked = true;
	}

	/**
	 * Resets the clicked state.
	 */
	public void reset() {
		clicked = false;
	}

	/**
	 * Checks if the user has clicked.
	 * @return the current clicked state
	 */
	public boolean isClicked() {
		return clicked;
	}

	/**
	 * Get a {@code OnClickListener} that starts a specified activity if this is the first click.
	 *
	 * @param target the activity to be started
	 * @return a {@code OnClickListener} that starts {@code target}
	 */
	public View.OnClickListener getOnClickListener(Class<?> target) {
		return view -> {
			if (!clicked) {
				clicked = true;
				Context context = view.getContext();
				context.startActivity(new Intent(context, target));
			}
		};
	}

	/**
	 * Get a {@code OnClickListener} that starts a specified activity if this is the first click.
	 * Note that this creates the intent eagerly.  For large amounts of different intents, use lazy
	 * loading if possible (define your own lambda).
	 *
	 * @param intent the intent to start new activity
	 * @return a {@code OnClickListener} that uses the passed-in intent to start new activity
	 */
	public View.OnClickListener getOnClickListener(Intent intent) {
		return view -> {
			if (!clicked) {
				clicked = true;
				Context context = view.getContext();
				context.startActivity(intent);
			}
		};
	}
}
