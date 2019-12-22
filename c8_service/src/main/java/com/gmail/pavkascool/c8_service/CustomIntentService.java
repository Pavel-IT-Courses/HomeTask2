package com.gmail.pavkascool.c8_service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CustomIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.gmail.pavkascool.c8_service.action.FOO";
    private static final String ACTION_BAZ = "com.gmail.pavkascool.c8_service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.gmail.pavkascool.c8_service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.gmail.pavkascool.c8_service.extra.PARAM2";

    public CustomIntentService() {
        super("CustomIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CustomIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CustomIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
//        }
        try {
            downloadFileByUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void downloadFileByUrl() throws Exception {
        String urlString = "https://download.nikonimglib.com/archive2/payY500AHbkQ02bXMhb14TZo4978/D3300_NT(En)02.pdf";
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        String fileName = getFileName(connection);

        File filesDir = getFilesDir();
        File file = new File(filesDir, fileName);
        if (file.exists()) {
            if (!file.delete()) {
                Log.d("MY TAG", "File is not deleted");
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int data;
        try {
            while ((data = inputStream.read()) != -1) ;
            fileOutputStream.write(data);

        } finally {
            fileOutputStream.close();
            inputStream.close();
        }
    }

    private String getFileName(URLConnection connection) {
        String headerString = connection.getHeaderField("Content-Disposition");
        if (headerString != null && !headerString.isEmpty()) {
            int index = headerString.indexOf("filename=");
            if (index >= 0) return headerString.substring(index + 10, headerString.length() - 1);
        }
        else {
            URL url = connection.getURL();
            String s = url.toString();
            String[] strs = s.split("/");
            return strs[strs.length - 1];
        }
        return "Noname";
    }
}
