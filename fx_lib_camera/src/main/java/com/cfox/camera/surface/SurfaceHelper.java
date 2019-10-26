package com.cfox.camera.surface;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.cfox.camera.AutoFitTextureView;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SurfaceHelper implements ISurfaceHelper {
    private static final String TAG = "SurfaceHelper";
    private final Object obj = new Object();
    private AutoFitTextureView mTextureView;
    private List<Surface> mSurfaces;
    private Size mPreviewSize;

    public SurfaceHelper(AutoFitTextureView textureView) {
        this.mTextureView = textureView;
        this.mTextureView.setSurfaceTextureListener(mTextureListener);
        this.mSurfaces = new ArrayList<>();
    }

    public TextureView getTextureView() {
        return mTextureView;
    }

    public Surface getSurface() {
        return new Surface(mTextureView.getSurfaceTexture());
    }

    public SurfaceTexture getSurfaceTexture() {
        return mTextureView.getSurfaceTexture();
    }

    public Observable<FxResult> isAvailable() {
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                Log.d(TAG, "subscribe: ..........");
                if (!mTextureView.isAvailable()) {
                    synchronized (obj) {
                        if (!mTextureView.isAvailable()) {
                            obj.wait(10 * 1000);
                        }
                        if (!mTextureView.isAvailable()) {
                            throw new RuntimeException("Surface create error wait 10 s");
                        }
                    }
                }

                Log.d(TAG, "SurfaceTexture isAvailable width:" + mTextureView.getWidth()  + "  height:" + mTextureView.getHeight());
                mSurfaces.add(getSurface());
                emitter.onNext(new FxResult());
            }
        }).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getLooper()));
    }

    @Override
    public List<Surface> getSurfaces() {
        return mSurfaces;
    }

    @Override
    public void addSurface(Surface surface) {
        Log.d(TAG, "addSurface: "  + (!mSurfaces.contains(surface)));
        if (!mSurfaces.contains(surface)) {
            Log.d(TAG, "addSurface: .......");
            mSurfaces.add(surface);
        }
    }

    @Override
    public Class getSurfaceClass() {
        return SurfaceTexture.class;
    }

    @Override
    public void setAspectRatio(Size size) {
        Log.d(TAG, "setAspectRatio: size width:" + size.getWidth() + "  height:" + size.getHeight());
        mPreviewSize = size;
        mTextureView.setAspectRatio(size.getHeight(), size.getWidth());
    }

    private TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable: .......width:" + width  + "   height:" + height  + "     mPreviewSize:" + mPreviewSize);
            mTextureView.getSurfaceTexture().setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            sendNotify();

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureSizeChanged: ....");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d(TAG, "onSurfaceTextureDestroyed: ,,,,,,,");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//            Log.d(TAG, "onSurfaceTextureUpdated: ,,,,,,,,");

        }
    };

    private void sendNotify() {
        synchronized (obj) {
            obj.notifyAll();
        }
    }
}
