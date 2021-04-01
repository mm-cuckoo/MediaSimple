package com.cfox.camera.mode.impl;


import com.cfox.camera.EsException;
import com.cfox.camera.imagereader.ImageReaderManager;
import com.cfox.camera.imagereader.ImageReaderManagerImpl;
import com.cfox.camera.sessionmanager.VideoSessionManger;
import com.cfox.camera.mode.VideoMode;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.utils.EsError;
import com.cfox.camera.EsParams;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class VideoModeImpl extends BaseMode implements VideoMode {
    private final VideoSessionManger mVideoSessionManger;
    private final ImageReaderManager mImageReaderHelper;
    public VideoModeImpl(VideoSessionManger videoSessionManger) {
        super(videoSessionManger);
        mVideoSessionManger = videoSessionManger;
        mImageReaderHelper = new ImageReaderManagerImpl();
    }

    @Override
    protected Observable<EsParams> applySurface(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                SurfaceManager manager = esParams.get(EsParams.Key.SURFACE_MANAGER);
                if (manager.isAvailable()) {
//                    manager.addCaptureSurface(mImageReaderHelper.createImageReader(esParams).getSurface());
                    emitter.onNext(esParams);
                } else  {
                    emitter.onError(new EsException("surface isAvailable = false , check SurfaceProvider implement", EsError.ERROR_CODE_SURFACE_UN_AVAILABLE));
                }
            }
        });
    }
}
