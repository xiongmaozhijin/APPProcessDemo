package com.ibbgou.appprocessdemo;

import com.ibbgou.appprocessdemo.fpspro.FPSMeter;

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
            if (i++ >= 1000) break;
            System.out.println("fps:" + fps);
            Thread.sleep(1_000L);
        }
        mFpsMeter.stopMonitor();

        System.out.println("Hello Android end");
    }
}
