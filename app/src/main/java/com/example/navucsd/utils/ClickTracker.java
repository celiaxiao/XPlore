package com.example.navucsd.utils;

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
}
