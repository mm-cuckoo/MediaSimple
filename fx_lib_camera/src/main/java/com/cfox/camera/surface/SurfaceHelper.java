package com.cfox.camera.surface;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.cfox.camera.utils.FxRequest;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class SurfaceHelper implements ISurfaceHelper {
    private static final String TAG = "SurfaceHelper";
    private final Object obj = new Object();
    private TextureView mTextureView;

    public SurfaceHelper(TextureView textureView) {
        this.mTextureView = textureView;
        this.mTextureView.setSurfaceTextureListener(mTextureListener);
    }

    public TextureView getTextureView() {
        return mTextureView;
    }

    public void setTextureView(TextureView mTextureView) {
        this.mTextureView = mTextureView;
        this.mTextureView.setSurfaceTextureListener(mTextureListener);
    }

    public Surface getSurface() throws InterruptedException {
        if (mTextureView == null || !mTextureView.isAvailable()) {
            synchronized (obj) {
                if (mTextureView == null || !mTextureView.isAvailable()) {
                    obj.wait(3000);
                }
            }
        }

        if (mTextureView == null) {
            return null;
        }

        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        return new Surface(texture);
    }

    public SurfaceTexture getSurfaceTexture() throws InterruptedException {
        if (mTextureView == null || !mTextureView.isAvailable()) {
            synchronized (obj) {
                if (mTextureView == null || !mTextureView.isAvailable()) {
                    obj.wait(3000);
                }
            }
        }
        return mTextureView != null ? mTextureView.getSurfaceTexture() : null;
    }

    public Observable<FxRequest> isAvailable() {
        return Observable.create(new ObservableOnSubscribe<FxRequest>() {
            @Override
            public void subscribe(ObservableEmitter<FxRequest> emitter) throws Exception {
                Log.d(TAG, "subscribe: ..........");
                FxRequest request = new FxRequest();
                emitter.onNext(request);
//                emitter.onComplete();

            }
        });
    }

    private TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            sendNotify();

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private void sendNotify() {
        synchronized (obj) {
            obj.notifyAll();
        }
    }
}
