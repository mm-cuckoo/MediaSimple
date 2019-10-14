package com.cfox.camera.controller;


import android.content.Context;

import com.cfox.camera.model.CameraModule;

public class FxPhotoController extends AbsController {


    public FxPhotoController(Context context) {
        super(context, CameraModule.ModuleFlag.MODULE_PHOTO);
    }
}
