package com.UCSDTripleC.XPloreUCSD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MotionEventCompat;
import android.widget.LinearLayout.LayoutParams;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * @source https://www.jianshu.com/p/c46020080034
 * this activity handles Landmark details page clicks image and display the image full screen
 * functionality. Could be also used to display other zoom image feature
 * usage: Intent intent = new Intent(view.getContext(),ZoomImageActivity.class);
 *        intent.putExtra("imageResource", ""+//the file path to find the resource is asset folder);
 *        view.getContext().startActivity(
 *                 intent,
 *                 ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), view,
 *                 //value of android:transitionName).toBundle());
 */
public class ZoomImageActivity extends AppCompatActivity {
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    ImageView zoomImage;
    private boolean isScaling=false;
//    private RectF mCurrentViewport =
//            new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);
//    private Rect mContentRect;



    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX,mPosY;
    // The 'active pointer' is the one currently moving our object.
    private LayoutParams layoutParams;
    private boolean isDraging;
    private ViewGroup mRrootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        zoomImage=findViewById(R.id.zoomImage);
        Bundle extras = getIntent().getExtras();
        String fileName = extras.getString("imageResource");
        InputStream ims = null;
        try {
            ims = getAssets().open(fileName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            zoomImage.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace( );
        }
        mRrootLayout = (ViewGroup) findViewById(R.id.zoomRoot);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        scaleGestureDetector.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if(!isScaling && !isDraging) ActivityCompat.finishAfterTransition(ZoomImageActivity.this);
            isScaling=false;
            isDraging=false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        LayoutParams layoutParams = (LayoutParams) zoomImage.getLayoutParams();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                //Remember where we started (for imageview location)
                mPosX=zoomImage.getX();
                mPosY=zoomImage.getY();
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                isDraging=true;
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;
                //modify to new position
                zoomImage.setX(mPosX);
                zoomImage.setY(mPosY);


                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }

        mRrootLayout.invalidate();
        return true;

        }



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            Log.i("touch","true");
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            zoomImage.setScaleX(mScaleFactor);
            zoomImage.setScaleY(mScaleFactor);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //return super.onScaleBegin(detector);
            isScaling=true;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //super.onScaleEnd(detector);
        }
    }
}