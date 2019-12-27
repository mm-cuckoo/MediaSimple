package com.cfox.camera.camera.session.impl;

import com.cfox.camera.camera.session.IPhotoRequestBuilderManager;
import com.cfox.camera.camera.session.helper.ICameraHelper;

public class PhtotoRequestBuilderManger extends RequestBuilderManager implements IPhotoRequestBuilderManager {
    public PhtotoRequestBuilderManger(ICameraHelper cameraHelper) {
        super(cameraHelper);
    }
}
