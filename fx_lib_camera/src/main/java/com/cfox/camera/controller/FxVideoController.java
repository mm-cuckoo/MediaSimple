package com.cfox.camera.controller;

import android.content.Context;

import com.cfox.camera.model.CameraModule;

public class FxVideoController extends AbsController {
    public FxVideoController(Context context) {
        super(context, CameraModule.ModuleFlag.MODULE_VIDEO);
    }
}
