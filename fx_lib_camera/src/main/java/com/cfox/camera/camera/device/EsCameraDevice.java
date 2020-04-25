package com.cfox.camera.camera.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;

import androidx.annotation.NonNull;

import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class EsCameraDevice implements IEsCameraDevice {

    private static IEsCameraDevice sInstance;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private CameraManager mCameraManager;
    private Map<String, CameraDevice> mDeviceMap = new HashMap<>();

    private EsCameraDevice(Context context) {
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public static IEsCameraDevice getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (EsCameraDevice.class) {
                if (sInstance == null) {
                    sInstance = new EsCameraDevice(context);
                }
            }
        }
        return sInstance;
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<EsResult> openCameraDevice(final EsRequest request) {
        final String cameraId = request.getString(Es.Key.CAMERA_ID);
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                EsLog.d( "open camera start .....camera id:" + cameraId);

                try {
                    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                        throw new RuntimeException("Time out waiting to lock camera opening.");
                    }

                    mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            mDeviceMap.put(camera.getId(), camera);
                            EsLog.d( "camera device opened....: ");
                            EsResult result = new EsResult();
                            result.put(Es.Key.CAMERA_DEVICE, camera);
                            result.put(Es.Key.OPEN_CAMERA_STATUS, Es.Value.OPEN_SUCCESS);
                            emitter.onNext(result);
                            emitter.onComplete();
                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {
                            EsLog.d("onDisconnected: ");
                            closeCameraDevice(camera.getId());
                            emitter.onComplete();
                        }

                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {
                            EsLog.d("onError: code:" + error);
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
    public Observable<EsResult> closeCameraDevice(final String cameraId) {
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(ObservableEmitter<EsResult> emitter) throws Exception {
                try {
                    EsLog.d("start close camera id " + cameraId);
                    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                        throw new RuntimeException("Time out waiting to lock camera opening.");
                    }

                    CameraDevice cameraDevice = null;
                    if (mDeviceMap.containsKey(cameraId)) {
                        EsLog.d( "closeCameraDevice :: subscribe: has camera device id:" + cameraId);
                        cameraDevice = mDeviceMap.remove(cameraId);
                    } else {
                        EsLog.d( "closeCameraDevice:  no camera device camera id: " + cameraId);
                    }

                    if (cameraDevice != null) {
                        cameraDevice.close();
                    }
                    EsResult result = new EsResult();
                    emitter.onNext(result);
                    emitter.onComplete();
                    EsLog.d("close camera id:" + cameraId + "  end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mCameraOpenCloseLock.release();
                }
            }
        });
    }
}
