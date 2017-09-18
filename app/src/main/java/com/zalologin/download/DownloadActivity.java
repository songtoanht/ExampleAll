package com.zalologin.download;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zalologin.R;
import com.zalologin.ebook.EbookActivity;
import com.zalologin.ebook.FileUtilsssssss;

import java.io.IOException;

/**
 * DownloadActivity
 * <p>
 * Created by HOME on 9/8/2017.
 */

public class DownloadActivity extends AppCompatActivity {
    private final static int REQUEST_CODE = 100;
    private final static int REQUEST_CODE_MANAGER = 101;
    private DownloadManager mDownloadManager;
    private long mDownloadId;

    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId == mDownloadId) {
                String filename = DownloadUtils.checkStatus(mDownloadManager, mDownloadId);
                Log.d("ttt", filename);
                String path = filename.substring(7, filename.length());
                Toast.makeText(DownloadActivity.this, path, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
//        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/EPUB/";
//        FileUtilsssssss.copyAssets(getApplicationContext(), path);

        findViewById(R.id.btnDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadView();
            }
        });

        findViewById(R.id.btnDownloadManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadManager();
            }
        });

        findViewById(R.id.btnRead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DownloadActivity.this, EbookActivity.class));
                finish();
            }
        });

        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        }

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.registerReceiver(mDownloadReceiver, filter);
    }

    public void downloadManager() {
        String link = "https://staging.mgift.vn/ebook/file?id=6";
        Uri uri = Uri.parse(link);
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/EPUB_MANAGER/";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Do you access storage")
                        .setTitle("Haha")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(DownloadActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                , Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                            }
                        });
                builder.show();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {

//            URLUtil.guessFileName(link, );
            mDownloadId = DownloadUtils.downloadData(this, mDownloadManager, uri, "haha.epub");
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mDownloadReceiver);
        super.onDestroy();
    }

    public void downloadView() {
        String link = "https://staging.mgift.vn/ebook/file?id=6";
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/EPUB/";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Do you access storage")
                        .setTitle("Haha")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(DownloadActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                , Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                            }
                        });
                builder.show();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
            downloadAsyncTask.execute(link, path);
        }
    }

    private class DownloadAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String log = "success";
            try {
                HttpDownloadUtility.downloadFile(params[0], params[1]);
            } catch (IOException e) {
                log = "error";
                e.printStackTrace();
            }
            return log;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(DownloadActivity.this, s, Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            String link = "https://staging.mgift.vn/ebook/file?id=6";
            String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/EPUB/";
            DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
            downloadAsyncTask.execute(link, path);
        }

        if (requestCode == REQUEST_CODE_MANAGER) {
            String link = "https://staging.mgift.vn/ebook/file?id=6";
            Uri uri = Uri.parse(link);
            mDownloadId = DownloadUtils.downloadData(this, mDownloadManager, uri, "haha.epub");
        }
    }
}
