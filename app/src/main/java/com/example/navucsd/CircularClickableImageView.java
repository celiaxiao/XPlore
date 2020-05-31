package com.example.navucsd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * An image view but with a circular clickable area.
 */
public final class CircularClickableImageView extends androidx.appcompat.widget.AppCompatImageView {

	/**
	 * The view that the touch event is dispatched to when clicked outside of the circle.
	 */
	private View view;

	/**
	 * Constructor that just delegates to super.
	 * @see androidx.appcompat.widget.AppCompatImageView
	 */
	public CircularClickableImageView(Context context) {
		super(context);
	}

	/**
	 * Constructor that just delegates to super.
	 * @see androidx.appcompat.widget.AppCompatImageView
	 */
	public CircularClickableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructor that just delegates to super.
	 * @see androidx.appcompat.widget.AppCompatImageView
	 */
	public CircularClickableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	/**
	 * Sets the "next in line" view.
	 * @param view the view that the touch event is dispatched to when clicked outside of the circle
	 */
	public void setView(View view) {
		this.view = view;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_UP) return true;
		float radius = Math.min(getWidth(), getHeight()) * 0.5f;
		float dx = event.getX() - radius;
		float dy = event.getY() - radius;
		if (Math.sqrt(dx * dx + dy * dy) <= radius) {
			performClick();
			return true;
		}
		// FIXME: this doesn't seem to work at the moment
		return view.dispatchTouchEvent(event);
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}
}