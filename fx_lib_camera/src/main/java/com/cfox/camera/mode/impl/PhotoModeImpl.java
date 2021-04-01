package com.cfox.camera.mode.impl;

import com.cfox.camera.EsException;
import com.cfox.camera.imagereader.ImageReaderManager;
import com.cfox.camera.imagereader.ImageReaderManagerImpl;
import com.cfox.camera.imagereader.ImageReaderProvider;
import com.cfox.camera.sessionmanager.PhotoSessionManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.utils.EsError;
import com.cfox.camera.EsParams;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * 整理 request 和 surface 。 返回数据整理
 */
public class PhotoModeImpl extends BaseMode implements PhotoMode {
    private final PhotoSessionManager mPhotoSessionManager;
    private final ImageReaderManager mImageReaderManager;
    public PhotoModeImpl(PhotoSessionManager photoSessionManager) {
        super(photoSessionManager);
        mPhotoSessionManager = photoSessionManager;
        mImageReaderManager = new ImageReaderManagerImpl();
    }

    @Override
    public void onRequestStop() {
        mImageReaderManager.closeImageReaders();
    }

    @Override
    protected Observable<EsParams> applySurface(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                SurfaceManager manager = esParams.get(EsParams.Key.SURFACE_MANAGER);
                List<ImageReaderProvider> imageReaderProviders = esParams.get(EsParams.Key.IMAGE_READER_PROVIDERS);
                if (manager.isAvailable()) {
                    for (ImageReaderProvider provider : imageReaderProviders) {
                        if (provider.getType() == ImageReaderProvider.TYPE.PREVIEW) {
                            manager.addPreviewSurface(mImageReaderManager.createImageReader(esParams, provider).getSurface());
                        } else if (provider.getType() == ImageReaderProvider.TYPE.CAPTURE) {
                            manager.addCaptureSurface(mImageReaderManager.createImageReader(esParams, provider).getSurface());
                        }
                    }
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
