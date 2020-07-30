package com.UCSDTripleC.XPloreUCSD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

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

        /*zoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return to previous activity
                if(isScaling) ActivityCompat.finishAfterTransition(ZoomImageActivity.this);
            }
        });*/
        zoomImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("IMAGE", "motion event: " + event.toString());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //zoomImage.setImageResource(R.drawable.button_hover);
                    }
                    case MotionEvent.ACTION_UP: {
                        //imageView.setImageResource(R.drawable.button);
                        if(!isScaling) ActivityCompat.finishAfterTransition(ZoomImageActivity.this);
                        isScaling=false;
                    }
                }
                return true;
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }
    /*@Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }*/
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