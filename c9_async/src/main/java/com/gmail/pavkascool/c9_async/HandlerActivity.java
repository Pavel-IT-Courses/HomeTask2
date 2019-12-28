package com.gmail.pavkascool.c9_async;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

public class HandlerActivity extends BaseActivity {

    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_ERROR = 2;
    private static final int STATUS_START = 3;

    private Handler handler;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MessageHandler(this);
    }

    @Override
    protected void getImage() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(STATUS_START);
                String url = "https://cnet2.cbsistatic.com/img/hEHxbI1Pphmvod56-B5ATDBmlwY=/0x0:2438x1440/980x551/2019/10/16/db85bfaa-b206-4e8e-a468-d878d80d46cc/newc-yongqing-bao-wildlife-photographer-of-the-year.jpg";
                ImageRequest request = new ImageRequest(url);
                try {
                    Bitmap bitmap = request.getImage();
                    Message message = handler.obtainMessage(STATUS_SUCCESS, bitmap);
                    handler.sendMessage(message);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(STATUS_ERROR);
                }
            }
        });
        thread.start();
    }

    private static class MessageHandler extends Handler {

        private WeakReference<BaseActivity> weakBaseActivity;

        public MessageHandler(BaseActivity activity) {
            this.weakBaseActivity = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case STATUS_ERROR:
                    changeProgressVisibility(View.GONE);
                    break;
                case STATUS_START:
                    changeProgressVisibility(View.VISIBLE);
                    break;
                case STATUS_SUCCESS:
                    changeProgressVisibility(View.GONE);
                    Object obj = msg.obj;
                    if(obj != null && obj instanceof Bitmap) {
                        showImage((Bitmap)obj);
                    }
                    else Log.d("Strange situation", "not image");
                    break;
            }
        }

        private void changeProgressVisibility(int visibility) {
            BaseActivity baseActivity = weakBaseActivity.get();
            if(baseActivity != null) {
                baseActivity.progressBar.setVisibility(visibility);
            }
        }

        private void showImage(Bitmap image) {
            BaseActivity baseActivity = weakBaseActivity.get();
            if(baseActivity != null) {
                baseActivity.imageView.setImageBitmap(image);
            }
        }
    }
}
