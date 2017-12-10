package com.checker.yousef.mytext;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.checker.yousef.mytext.library.Url;
import com.checker.yousef.mytext.ocr.PermissionUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String CLOUD_VISION_API_KEY = "YOUR_API_KEY";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView(){
        findViewById(R.id.text).setOnClickListener(new clickListener());
        findViewById(R.id.scan).setOnClickListener(new clickListener());
        findViewById(R.id.checklist).setOnClickListener(new clickListener());
        findViewById(R.id.history).setOnClickListener(new clickListener());
        findViewById(R.id.tutorial).setOnClickListener(new clickListener());
        findViewById(R.id.donate).setOnClickListener(new clickListener());
        findViewById(R.id.aboutapp).setOnClickListener(new clickListener());
        findViewById(R.id.aboutme).setOnClickListener(new clickListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.info:

                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void AlertBuilder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setMessage(R.string.dialog_select_prompt)
                .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser();
                    }
                })
                .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera();
                    }
                });
        builder.create().show();
    }

    public class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 押下時の処理
            switch (v.getId()){
                case R.id.text:
                    Intent input = new Intent(MainActivity.this, InputActivity.class);
                    startActivity(input);
                    break;
                case R.id.scan:
                    AlertBuilder();
//                    Intent scan = new Intent(MainActivity.this, ScanActivity.class);
//                    startActivity(scan);
                    break;
                case R.id.checklist:
                    Toast.makeText(MainActivity.this, "You can visit here in the next version.", Toast.LENGTH_LONG).show();
//                    Intent checklist = new Intent(MainActivity.this, CheckListActivity.class);
//                    startActivity(checklist);
                    break;
                case R.id.history:
                    Toast.makeText(MainActivity.this, "You can visit here in the next version.", Toast.LENGTH_LONG).show();
//                    Intent history = new Intent(MainActivity.this, HistoryActivity.class);
//                    startActivity(history);
                    break;
                case R.id.donate:
                    Intent donate = new Intent(MainActivity.this, DonateActivity.class);
                    startActivity(donate);
                    break;
                case R.id.tutorial:
                    Intent tutorial = new Intent(MainActivity.this, BrowserActivity.class);
                    tutorial.putExtra("Url", Url.tutorial);
                    tutorial.putExtra("Title","Tutorial");
                    startActivity(tutorial);
                    break;
                case R.id.aboutapp:
                    Intent aboutApp = new Intent(MainActivity.this, BrowserActivity.class);
                    aboutApp.putExtra("Url", Url.aboutapp);
                    aboutApp.putExtra("Title","About App");
                    startActivity(aboutApp);
                    break;
                case R.id.aboutme:
                    Intent aboutMe = new Intent(MainActivity.this, BrowserActivity.class);
                    aboutMe.putExtra("Url", Url.aboutme);
                    aboutMe.putExtra("Title","About Me");
                    startActivity(aboutMe);
                    break;
            }
        }
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(this, CAMERA_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
            //カメラ画像を保存するディレクトリ

            if(!mediaStorageDir.exists()){
                if(!mediaStorageDir.mkdirs())
                   return;
            }
            String timeStamp = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date());

            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".png");
            //カメラを起動して実際に保存される画像ファイル名
            imageUri = Uri.fromFile(mediaFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));

            startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            preview(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
           preview(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void preview(Uri uri) {
        Intent preview = new Intent(MainActivity.this, PreviewActivity.class);
        preview.putExtra("imageUri", uri.toString());
        startActivity(preview);
    }
}
