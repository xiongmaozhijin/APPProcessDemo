package com.ibbgou.appprocessdemo.fpspro;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Surface;

import com.ibbgou.appprocessdemo.HelloWorld;

public class FPSMeter extends Thread {

    private Handler mHandler;

    private int mHeight;

    private ImageReader mImageReader;

    private int mRotation;

    private int mWidth;

    public FPSMeter(int width, int height, int dpi, int paramInt4) {
        this.mRotation = paramInt4;
        this.mWidth = width;
        this.mHeight = height;
        Init();
    }

    @SuppressLint("WrongConstant")
    private void createImageReader() {
        this.mImageReader = ImageReader.newInstance(600, 800, PixelFormat.RGBA_8888, 2);
    }

    public void Init() {
        createImageReader();
    }

    public int getFPS() {
        return 0;
    }

    public void run() {
        Looper.prepare();
        this.mHandler = new Handler();
        Looper.loop();
    }

    public void startMonitor() {
        LogUtils.w("FPSMeter", "startMonitor()");
        streamScreen();
        if (this.mImageReader != null)
            this.mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), this.mHandler);
    }

    private IBinder mIBinder;

    public void streamScreen() {
        LogUtils.w("FPSMeter", "streamScreen()");
        IBinder display = createDisplay();
        mIBinder = display;
        System.out.println("token is =" + display);
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        setDisplaySurface(display, mImageReader.getSurface(), rect, rect);
    }

    private static IBinder createDisplay() {
        return SurfaceControl.createDisplay("scrcpy", false);
    }

    private static void setDisplaySurface(IBinder display, Surface surface, Rect deviceRect, Rect displayRect) {
        SurfaceControl.openTransaction();
        try {
            SurfaceControl.setDisplaySurface(display, surface);
            SurfaceControl.setDisplayProjection(display, 0, deviceRect, displayRect);
            SurfaceControl.setDisplayLayerStack(display, 0);
        } finally {
            SurfaceControl.closeTransaction();
        }
    }

    public void stopMonitor() {
        if (mHandler != null) {
            mHandler.getLooper().quit();
        }

        if (this.mImageReader != null)
            this.mImageReader.setOnImageAvailableListener(null, null);

//        if (this.mVirtualDisplay != null) {
//            this.mVirtualDisplay.release();
//            this.mVirtualDisplay = null;
//        }
        if (mIBinder != null) {
            SurfaceControl.destroyDisplay(mIBinder);
        }
    }

    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        private ImageAvailableListener() {
        }

        public void onImageAvailable(ImageReader param1ImageReader) {
            calcFps(param1ImageReader);
        }
    }

    private boolean mResetCalc = true;
    private int mCalcFrameCnt = 0;
    private long mLastCalcTimeMs = 0L;

    private void calcFps(ImageReader imageReader) {
        // fps = frameCnt / useTime
        LogUtils.d("FpsMeter", "onImageAvailable()");

        if (mResetCalc) {
            mResetCalc = false;
            mLastCalcTimeMs = System.currentTimeMillis();
            mCalcFrameCnt = 0;
        }

        mCalcFrameCnt += 1;

        if (imageReader != null) {
            final Image latestImage = imageReader.acquireLatestImage();
            if (latestImage != null) {
                final long useTime = System.currentTimeMillis() - mLastCalcTimeMs;
                if (mCalcFrameCnt >= 50) {
                    mResetCalc = true;
                    int fps = (int) (1F * mCalcFrameCnt / (1F * useTime / 1_000L));
                    LogUtils.w("FpsMeter", "fps=" + fps + ", total time=" + useTime + ", frames=" + mCalcFrameCnt);
                    HelloWorld.fps = fps;
                }

                latestImage.close();
            }
        }
    }
}

