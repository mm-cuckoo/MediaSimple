package com.cfox.camera.camera.session;



public interface ISessionManager  {

    void getCameraSession(int count);
    boolean hasSession();
    ICameraSession getCameraSession();
}
