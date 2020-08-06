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

import com.UCSDTripleC.XPloreUCSD.utils.ZoomImageView;

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
    ZoomImageView zoomImage;


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
        zoomImage.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                ActivityCompat.finishAfterTransition(ZoomImageActivity.this);
            }
        });

    }

}