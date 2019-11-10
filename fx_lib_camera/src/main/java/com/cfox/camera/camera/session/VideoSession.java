package com.cfox.camera.camera.session;

import android.content.Context;

public class VideoSession extends CameraSession implements IVideoSession {
    public VideoSession(Context context) {
        super(context);
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
