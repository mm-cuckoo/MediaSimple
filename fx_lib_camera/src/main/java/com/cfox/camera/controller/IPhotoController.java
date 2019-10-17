package com.cfox.camera.controller;


import com.cfox.camera.utils.FxRequest;

public interface IPhotoController extends IBaseController {

    void onCapture(FxRequest request);
}
