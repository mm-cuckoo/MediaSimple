package com.cfox.camera.camera.session.helper;


import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IPhotoSessionHelper extends ICameraSessionHelper/*extends ISessionHelper*/ {
    Observable<FxResult> capture(FxRequest request);
    Observable<FxResult> captureStillPicture(FxRequest request);

}
