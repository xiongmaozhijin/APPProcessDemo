package com.ibbgou.appprocessdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ibbgou.appprocessdemo.fpspro.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView txvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvInfo = findViewById(R.id.txvInfo);
        checkPermission();
    }

    public void onBtnExecCmd(View view) throws IOException {
        if (System.currentTimeMillis() > 0) {
            readFpsAndShow();
            return;
        }
        final File directory = Environment.getExternalStorageDirectory();
//        final File directory = getFilesDir();
        LogUtils.d(TAG, "directory=" + directory.getParent());
        final InputStream inputStream = getAssets().open("classes_fps.dex");

        final File file = new File(directory, "classes_fps.dex");
//        final File file = new File("/data/local/tmp/classes_fps.dex");
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
        }
        LogUtils.d(TAG, "file=" + file.getAbsolutePath());

        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4 * 1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.close();

        String[] pmPath = CmdUtils.safeExec("pm path com.ibbgou.appprocessdemo");
        LogUtils.d(TAG, Arrays.toString(pmPath));
        final String path = pmPath[0];
        final String hack1 = "app_process -Djava.class.path=" + path + " /system/bin com.ibbgou.appprocessdemo.HelloWorld";
        LogUtils.d(TAG, "hack=" + hack1);
        String[] strings = CmdUtils.safeExec(hack1);
        LogUtils.d(TAG, Arrays.toString(strings));


//        final String hack = "app_process -Djava.class.path=/storage/emulated/0/classes_fps.dex /system/bin com.ibbgou.appprocessdemo.HelloWorld";
//        LogUtils.d(TAG, "hack=" + hack);
//        String[] strings = CmdUtils.safeExec(hack);
//        LogUtils.d(TAG, Arrays.toString(strings));
    }

    private void readFpsAndShow() {
        try {
            LogUtils.d(TAG, "readFpsAndShow()");
            final File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, "fps.txt");
            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                LogUtils.d(TAG, "readFpsAndShow().line=" + readLine);
                txvInfo.setText(readLine);
                if (!isFinishing()) {
                    txvInfo.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readFpsAndShow();
                        }
                    }, 1_000L);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            Log.i("cbs", "isGranted == " + isGranted);
            if (!isGranted) {
                ((Activity) this).requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }

    }
}
