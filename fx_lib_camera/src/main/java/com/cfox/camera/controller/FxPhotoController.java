package com.cfox.camera.controller;


import android.content.Context;

import com.cfox.camera.model.CameraModule;
import com.cfox.camera.surface.ISurfaceHelper;

public class FxPhotoController extends AbsController {


    public FxPhotoController(Context context, ISurfaceHelper surfaceHelper) {
        super(context, surfaceHelper, CameraModule.ModuleFlag.MODULE_PHOTO);
    }
}
