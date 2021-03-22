package com.cfox.camera.surface;

import android.graphics.SurfaceTexture;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.cfox.camera.AutoFitTextureView;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.EsResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SurfaceHelper implements ISurfaceHelper {
    private final Object obj = new Object();
    private AutoFitTextureView mTextureView;
    private List<Surface> mSurfaces;
    private Size mPreviewSize;
    private Surface mSurface;

    public SurfaceHelper(AutoFitTextureView textureView) {
        this.mTextureView = textureView;
        this.mTextureView.setSurfaceTextureListener(mTextureListener);
        this.mSurfaces = new ArrayList<>();
    }

    public Surface getSurface() {
        if (mSurface == null) {
            mSurface = new Surface(mTextureView.getSurfaceTexture());
        }
        return mSurface;
    }

    public Observable<EsResult> isAvailable() {
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(ObservableEmitter<EsResult> emitter) throws Exception {
                EsLog.d("subscribe: ..........");
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

                EsLog.d("SurfaceTexture isAvailable width:" + mTextureView.getWidth()  + "  height:" + mTextureView.getHeight());
                mSurfaces.add(getSurface());
                emitter.onNext(new EsResult());
                emitter.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getLooper()));
    }

    @Override
    public List<Surface> getAllSurfaces() {
        return mSurfaces;
    }

    @Override
    public List<Surface> getCaptureSurfaces() {
        mSurfaces.remove(getSurface());
        return mSurfaces;
    }

    @Override
    public void addCaptureSurface(Surface surface) {
        EsLog.d("addSurface: "  + (!mSurfaces.contains(surface)));
        if (!mSurfaces.contains(surface)) {
            EsLog.d("addSurface: .......");
            mSurfaces.add(surface);
        }
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
