package com.cfox.camera.camera.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;

import androidx.annotation.NonNull;

import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.EsParams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class EsCameraDeviceImpl implements EsCameraDevice {

    private static EsCameraDevice sInstance;
    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private final CameraManager mCameraManager;
    private final Map<String, CameraDevice> mDeviceMap = new HashMap<>();

    private EsCameraDeviceImpl(Context context) {
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public static EsCameraDevice getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (EsCameraDeviceImpl.class) {
                if (sInstance == null) {
                    sInstance = new EsCameraDeviceImpl(context);
                }
            }
        }
        return sInstance;
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<EsParams> openCameraDevice(final EsParams esParams) {
        EsLog.d( "open camera device .params=====>:" + esParams);
        final String cameraId = esParams.get(EsParams.Key.CAMERA_ID);
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(final ObservableEmitter<EsParams> emitter) {
                try {
                    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                        throw new RuntimeException("Time out waiting to lock camera opening.");
                    }
                    mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            mDeviceMap.put(camera.getId(), camera);
                            EsLog.d( "camera device opened....camera id: "  + camera.getId());
                            esParams.put(EsParams.Key.CAMERA_DEVICE, camera);
                            esParams.put(EsParams.Key.OPEN_CAMERA_STATE, EsParams.Value.CAMERA_OPEN_SUCCESS);
                            emitter.onNext(esParams);
                            emitter.onComplete();
                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {
                            EsLog.d("onDisconnected: ");
                            closeCameraDevice(camera.getId()).subscribe(new CameraObserver<EsParams>());
                            EsParams esParams = new EsParams();
                            esParams.put(EsParams.Key.OPEN_CAMERA_STATE, EsParams.Value.CAMERA_DISCONNECTED);
                            emitter.onNext(esParams);
                            emitter.onComplete();
                        }

                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {
                            EsLog.d("onError: code:" + error);
                            closeCameraDevice(camera.getId()).subscribe(new CameraObserver<EsParams>());
                            EsParams esParams = new EsParams();
                            esParams.put(EsParams.Key.OPEN_CAMERA_STATE, EsParams.Value.CAMERA_OPEN_FAIL);
                            emitter.onNext(esParams);
                            emitter.onError(new Throwable("open camera error Code:" + error));
                            emitter.onComplete();
                        }
                    }, null);
                } catch (CameraAccessException | InterruptedException e) {
                    EsParams esParams = new EsParams();
                    esParams.put(EsParams.Key.OPEN_CAMERA_STATE, EsParams.Value.CAMERA_OPEN_FAIL);
                    emitter.onNext(esParams);
                    emitter.onError(new Throwable("open camera error Code:" + Arrays.toString(e.getStackTrace())));
                } finally {
                    mCameraOpenCloseLock.release();
                }
            }
        });
    }

    @Override
    public Observable<EsParams> closeCameraDevice(final String cameraId) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(ObservableEmitter<EsParams> emitter) {
                try {
                    EsLog.d("start close camera id " + cameraId);
                    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                        throw new RuntimeException("Time out waiting to lock camera opening.");
                    }

                    CameraDevice cameraDevice = null;
                    if (mDeviceMap.containsKey(cameraId)) {
                        cameraDevice = mDeviceMap.remove(cameraId);
                    }

                    if (cameraDevice != null) {
                        cameraDevice.close();
                    }
                    EsLog.d("close camera id:" + cameraId + "  end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mCameraOpenCloseLock.release();
                    EsParams esParams = new EsParams();
                    emitter.onNext(esParams);
                    emitter.onComplete();
                }
            }
        });
    }
}
