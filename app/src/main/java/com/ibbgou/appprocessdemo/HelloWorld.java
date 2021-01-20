package com.ibbgou.appprocessdemo;

import android.os.Environment;

import com.ibbgou.appprocessdemo.fpspro.FPSMeter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class HelloWorld {

    private static FPSMeter mFpsMeter;
    public static int fps = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello Android start");
        Thread.sleep(3_000L);
        mFpsMeter = new FPSMeter(800, 1020, 300, 0);
        mFpsMeter.start();

        System.out.println("fps meter start");
        Thread.sleep(2_000L);
        System.out.println("fps meter start monitor");
        mFpsMeter.startMonitor();

        int i = 0;
        while (true) {
            if (i++ >= 60) break;
            System.out.println("fps-:" + fps);
            writeFps(fps);

            Thread.sleep(1_000L);
        }
        mFpsMeter.stopMonitor();

        System.out.println("Hello Android end");
    }

    private static void writeFps(int fps) {
        try {
            final File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, "fps.txt");
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(fps));
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Hello Android error:" + e.getLocalizedMessage());
        }
    }
}
