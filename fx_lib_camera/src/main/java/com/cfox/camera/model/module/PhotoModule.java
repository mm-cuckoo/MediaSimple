package com.cfox.camera.model.module;

import android.util.Log;

import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.IPhotoSessionHelper;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class PhotoModule extends BaseModule {
    private static final String TAG = "PhotoModule";
    private IPhotoSessionHelper mPhotoSessionHelper;
    public PhotoModule(IFxCameraDevice cameraDevice, IPhotoSessionHelper photoSessionHelper) {
        super(cameraDevice, photoSessionHelper);
        mPhotoSessionHelper = photoSessionHelper;
    }

    @Override
    public Observable<FxResult> onCapture(FxRequest request) {
        Log.d(TAG, "onCapture: ......");
        return mPhotoSessionHelper.capture(request);
    }
}
