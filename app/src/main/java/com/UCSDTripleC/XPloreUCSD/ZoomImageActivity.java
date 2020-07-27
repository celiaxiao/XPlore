package com.UCSDTripleC.XPloreUCSD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        ImageView zoomImage=findViewById(R.id.zoomImage);
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

        zoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注意这里不使用finish
                ActivityCompat.finishAfterTransition(ZoomImageActivity.this);
            }
        });
    }
}