package com.example.docpro_version2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LottieAnimationView file_lottie;
    LottieAnimationView paste_link;
    EditText link;
    Button get;

    int REQUEST_CODE = 1;
    int REQUEST_CODE_LINK = 2;

    int REQUEST_FILE_CODE=3;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        file_lottie = findViewById(R.id.files_lottie);
        paste_link = findViewById(R.id.paste_Link);
        link = findViewById(R.id.enter_link);
        get = findViewById(R.id.get);
        PRDownloader.initialize(getApplicationContext());


        file_lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Open_FromFiles();
            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url = link.getText().toString();

                checkOfflineMode();

            }
        });


    }

    private void checkOfflineMode() {

        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()) {
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }


//        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/offlinetab_PAL/PALiDream.txt");
//        Util.setOfflineMode(context,file.exists());

        try {
            downloadPdf();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkPermission() {

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            downloadPdf();
       }
        else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_FILE_CODE);

       }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_FILE_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Both permissions granted
                downloadPdf();
            }else {
                // Permissions not granted
                Toast.makeText(MainActivity.this, "Please allow storage permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadPdf() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        File file = new File(Environment.getExternalStorageDirectory() + "/Download/");

        String finaName=String.valueOf(System.currentTimeMillis()+".pdf");


        PRDownloader.download(url, file.getPath(),finaName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        long per = progress.currentBytes * 100 / progress.totalBytes;
                        progressDialog.setMessage("Downloading :" + per + "%");

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent=new Intent(MainActivity.this,Link_Activity.class);
                        String path=Environment.getExternalStorageDirectory() + "/Download/"+finaName;
                        intent.putExtra("link",path);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(Error error) {

                        Toast.makeText(MainActivity.this, "Erorr Downloading the File", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                });

    }


    public void Open_FromFiles() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                Uri pdfuri = data.getData();
                if (pdfuri != null) {
                    Intent intent = new Intent(MainActivity.this, Files_Activity.class);
                    intent.putExtra("data", pdfuri);
                    startActivity(intent);
                }
            }
        }
    }


}
