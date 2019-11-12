package com.cfox.camera.camera.session;

import android.content.Context;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class VideoSession extends CameraSession implements IVideoSession {
    public VideoSession(Context context) {
        super(context);
    }

    @Override
    public Observable<FxResult> onPreviewRepeatingRequest(FxRequest request) {
        return null;
    }

    @Override
    public IBuilderPack getBuilderPack() {
        return null;
    }

    @Override
    public int createPreviewTemplate() {
        return 0;
    }
}
