package com.cfox.camera.camera.session.impl;

import com.cfox.camera.camera.session.IPhotoRequestBuilderManager;
import com.cfox.camera.camera.session.helper.ICameraHelper;

public class PhotoRequestBuilderManger extends RequestBuilderManager implements IPhotoRequestBuilderManager {
    public PhotoRequestBuilderManger(ICameraHelper cameraHelper) {
        super(cameraHelper);
    }
}
