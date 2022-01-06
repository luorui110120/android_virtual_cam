package com.example.vcam;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


public class MainActivity extends Activity {
    // Storage Permissions
    public static String s_camera_dir =Environment.getExternalStorageDirectory() + "/DCIM/Camera1/";
    public static String s_virtual_path =s_camera_dir + "virtual.mp4";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @SuppressLint("WorldReadableFiles")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button select_file_btn = findViewById(R.id.select_file_btn);
        Button act_hijacked_btn = findViewById(R.id.act_hijacked_btn);
        Button dis_hijacked_btn = findViewById(R.id.dis_hijacked_btn);
        Button act_voices_btn = findViewById(R.id.act_voices_btn);
        Button dis_voices_btn = findViewById(R.id.dis_voices_btn);
        verifyStoragePermissions(this);


        select_file_btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                openFileSelector();
            }
        });
        act_hijacked_btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if(Utils.deleteSingleFile( s_camera_dir + "disable.jpg")){
                    Toast.makeText(v.getContext(), "激活劫持成功!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(v.getContext(), "激活劫持失败!", Toast.LENGTH_LONG).show();
                }
            }
        });
        dis_hijacked_btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if(Utils.makeFilePath( s_camera_dir + "disable.jpg")){
                    Toast.makeText(v.getContext(), "禁用劫持成功!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(v.getContext(), "禁用劫持失败!", Toast.LENGTH_LONG).show();
                }
            }
        });
        act_voices_btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if(Utils.makeFilePath( s_camera_dir + "no-silent.jp")){
                    Toast.makeText(v.getContext(), "激活声音成功!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(v.getContext(), "激活声音失败!", Toast.LENGTH_LONG).show();
                }
            }
        });
        dis_voices_btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if(Utils.deleteSingleFile( s_camera_dir + "no-silent.jp")){
                    Toast.makeText(v.getContext(), "禁用声音成功!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(v.getContext(), "禁用声音失败!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    /**
     * 打开本地文件器
     */
    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.setType("video/*"); //选择视频
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filepath = Utils.getUriToFilePath(MainActivity.this, uri);

            if(null != filepath){
                Utils.createDir(s_camera_dir);
                if(Utils.copyFile(filepath, s_virtual_path)){
                    Toast.makeText(this, "视频文件设置成功!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "视频文件设置失败!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "选择文件失败, 文件路径："+ "不存在!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


