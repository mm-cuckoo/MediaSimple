package com.cfox.module_camera;

import android.graphics.SurfaceTexture;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.SurfaceProvider;

public class SurfaceProviderImpl implements SurfaceProvider {
    private final Object obj = new Object();
    private AutoFitTextureView mTextureView;
    private Size mPreviewSize;
    private Surface mSurface;

    public SurfaceProviderImpl(AutoFitTextureView textureView) {
        this.mTextureView = textureView;
        this.mTextureView.setSurfaceTextureListener(mTextureListener);
    }

    public Surface getSurface() {
        if (mSurface == null) {
            mSurface = new Surface(mTextureView.getSurfaceTexture());
        }

        if (!mSurface.isValid()) {
            EsLog.e("==>mSurface isValid false ");
        }
        return mSurface;
    }

    public boolean isAvailable() {
        EsLog.d("subscribe: ..........");
        if (!mTextureView.isAvailable()) {
            synchronized (obj) {
                if (!mTextureView.isAvailable()) {
                    try {
                        obj.wait(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!mTextureView.isAvailable()) {
                    return false;
                }
            }
        }

        EsLog.d("SurfaceTexture isAvailable width:" + mTextureView.getWidth()  + "  height:" + mTextureView.getHeight());
        return true;
    }


    @Override
    public Class getPreviewSurfaceClass() {
        return SurfaceTexture.class;
    }

    @Override
    public void setAspectRatio(Size size) {
        EsLog.d("setAspectRatio: size width:" + size.getWidth() + "  height:" + size.getHeight());
        mPreviewSize = size;
        mTextureView.setAspectRatio(size.getHeight(), size.getWidth());
    }

    private TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            EsLog.d("onSurfaceTextureAvailable: .......width:" + width  + "   height:" + height  + "     mPreviewSize:" + mPreviewSize);
            mTextureView.getSurfaceTexture().setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            sendNotify();

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            EsLog.d("onSurfaceTextureSizeChanged: ....");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            EsLog.d("onSurfaceTextureDestroyed: ,,,,,,,");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//            EsLog.d("onSurfaceTextureUpdated: ,,,,,,,,");

        }
    };

    private void sendNotify() {
        synchronized (obj) {
            obj.notifyAll();
        }
    }
}
