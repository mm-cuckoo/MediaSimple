package com.cfox.camera.camera.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.HashMap;
import java.util.Map;
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
    private Map<String, CameraDevice> mDeviceMap = new HashMap<>();

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
    public Observable<FxResult> openCameraDevice(final FxRequest request) {
        final String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        Log.d(TAG, "open camera start .....camera id:" + cameraId);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                try {
                    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                        throw new RuntimeException("Time out waiting to lock camera opening.");
                    }

                    mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            mDeviceMap.put(camera.getId(), camera);
                            Log.d(TAG, "camera device opened....: ");
//                            mCameraDevice= camera;
                            FxResult result = new FxResult();
                            result.put(FxRe.Key.CAMERA_DEVICE, camera);
                            result.put(FxRe.Key.OPEN_CAMERA_STATUS, FxRe.Value.OPEN_SUCCESS);
                            emitter.onNext(result);
                            emitter.onComplete();
                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {
                            Log.d(TAG, "onDisconnected: ");
                            closeCameraDevice(camera.getId());
                            emitter.onComplete();
                        }

                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {
                            Log.d(TAG, "onError: code:" + error);
                            closeCameraDevice(camera.getId());
                            emitter.onError(new Throwable("open camera error Code:" + error));
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
    public Observable<FxResult> closeCameraDevice(final String cameraId) {
        Log.d(TAG, "close  Camera   id:" + cameraId);

        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                try {
                    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                        throw new RuntimeException("Time out waiting to lock camera opening.");
                    }

                    CameraDevice cameraDevice = null;
                    if (mDeviceMap.containsKey(cameraId)) {
                        Log.d(TAG, "closeCameraDevice :: subscribe: has camera device id:" + cameraId);
                        cameraDevice = mDeviceMap.remove(cameraId);
                    } else {
                        Log.d(TAG, "closeCameraDevice:  no camera device camera id: " + cameraId);
                    }

                    if (cameraDevice != null) {
                        cameraDevice.close();
                    }
                    FxResult result = new FxResult();
                    emitter.onNext(result);
                    emitter.onComplete();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mCameraOpenCloseLock.release();
                }
            }
        });
    }
}
