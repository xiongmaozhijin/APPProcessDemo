package com.ibbgou.appprocessdemo.fpspro;

import android.os.Handler;
import android.view.Surface;

public interface VirtualDisplayFactory {
    VirtualDisplay createVirtualDisplay(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Surface paramSurface, Handler paramHandler);
}
