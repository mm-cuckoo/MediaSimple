package com.cfox.camera.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.utils.FxReq;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxRes;
import com.cfox.camera.utils.FxResult;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class FxCameraDevice implements IFxCameraDevice {

    private static final String TAG = "FxCameraDeviceImpl";
    private static IFxCameraDevice sInstance;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;

    private FxCameraDevice(Context context) {
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }


    public static IFxCameraDevice getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (FxCameraDevice.class) {
                if (sInstance == null) {
                    sInstance = new FxCameraDevice(context);
                }
            }
        }
        return sInstance;
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<FxResult> openCameraDevice(final FxRequest fxRequest) {

        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                String cameraId = fxRequest.getString(FxReq.Key.CAMERA_ID);
                Log.d(TAG, "subscribe: cameraId:----->" + cameraId);
                try {
                    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                        throw new RuntimeException("Time out waiting to lock camera opening.");
                    }

                    mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            mCameraDevice = camera;
                            Log.d(TAG, "onOpened: ");
                            FxResult result = new FxResult();
                            result.put(FxRes.Key.CAMERA_DEVICE, camera);
                            result.put(FxRes.Key.OPEN_CAMERA_STATUS, FxRes.Value.OPEN_SUCCESS);
                            emitter.onNext(result);
//                            emitter.onComplete();
                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {
                            Log.d(TAG, "onDisconnected: ");

                        }

                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {
                            Log.d(TAG, "onError: ");

                        }
                    }, null);
                } catch (CameraAccessException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mCameraOpenCloseLock.release();
                }
            }
        });
    }

    @Override
    public Observable<FxResult> closeCameraDevice(FxRequest request) {
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                closeCameraDevice();
            }
        });
    }

    private void closeCameraDevice() {
        try {
            mCameraOpenCloseLock.acquire();
            if (mCameraDevice != null) {
                mCameraDevice.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mCameraOpenCloseLock.release();
        }
    }
}
