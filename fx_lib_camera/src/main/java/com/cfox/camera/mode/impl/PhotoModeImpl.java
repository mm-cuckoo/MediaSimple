package com.cfox.camera.mode.impl;

import com.cfox.camera.EsException;
import com.cfox.camera.mode.IReaderHelper;
import com.cfox.camera.mode.ImageReaderHelper;
import com.cfox.camera.sessionmanager.PhotoSessionManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.utils.EsError;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsParams;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * 整理 request 和 surface 。 返回数据整理
 */
public class PhotoModeImpl extends BaseMode implements PhotoMode {
    private final PhotoSessionManager mPhotoSessionManager;
    private final IReaderHelper mImageReaderHelper;
    public PhotoModeImpl(PhotoSessionManager photoSessionManager) {
        super(photoSessionManager);
        mPhotoSessionManager = photoSessionManager;
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public void onRequestStop() {
        mImageReaderHelper.closeImageReaders();
    }

    @Override
    protected Observable<EsParams> applySurface(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                SurfaceManager manager = (SurfaceManager) esParams.getObj(Es.Key.SURFACE_MANAGER);
                if (manager.isAvailable()) {
                    manager.addReaderSurface(mImageReaderHelper.createImageReader(esParams).getSurface());
                    emitter.onNext(esParams);
                } else  {
                    emitter.onError(new EsException("surface isAvailable = false , check SurfaceProvider implement", EsError.ERROR_CODE_SURFACE_UN_AVAILABLE));
                }
            }
        });
    }

    @Override
    public Observable<EsParams> requestCapture(EsParams esParams) {
        EsLog.d("requestCapture: ......" + esParams);
        return mPhotoSessionManager.capture(esParams);
    }
}
