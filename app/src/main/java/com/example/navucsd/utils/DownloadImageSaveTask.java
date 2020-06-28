package com.example.navucsd.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class DownloadImageSaveTask extends AsyncTask<String, Void, Bitmap> {

    private HashMap<String, Bitmap> hashMap;
    private String inputUrl;
    private ImageView bmImage;

    public DownloadImageSaveTask(ImageView bmImage, HashMap<String, Bitmap> hashMap) {
        this.bmImage = bmImage;
        this.hashMap = hashMap;
    }

    @Override
    protected Bitmap doInBackground(String... imageUrl) {
        inputUrl = imageUrl[0];
        URL url = null;
        Bitmap bmp = null;
        try {
            if (imageUrl[0].startsWith("http:")) {
                imageUrl[0] = imageUrl[0].replace("http:", "https:");
            }
            url = new URL(imageUrl[0]);
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        hashMap.put(inputUrl, result);
        bmImage.setImageBitmap(result);
    }
}