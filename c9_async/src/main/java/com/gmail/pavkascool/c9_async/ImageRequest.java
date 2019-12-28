package com.gmail.pavkascool.c9_async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

public class ImageRequest {
    private OkHttpClient client = new OkHttpClient();
    private String url;

    public ImageRequest(String url) {
        this.url = url;
    }

    public Bitmap getImage() throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) throw new IOException("File Not Downloaded");
        InputStream inputStream = response.body().byteStream();
        return BitmapFactory.decodeStream(inputStream);
    }
}

