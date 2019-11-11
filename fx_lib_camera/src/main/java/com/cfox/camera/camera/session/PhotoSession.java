package com.cfox.camera.camera.session;

import android.content.Context;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class PhotoSession extends CameraSession implements IPhotoSession {
    private static final String TAG = "PhotoSession";

    public PhotoSession(Context context) {
        super(context);
    }

    @Override
    public Observable<FxResult> onSendRepeatingRequest(FxRequest request) {
        Log.d(TAG, "onSendRepeatingRequest: .....");
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.REQUEST_BUILDER);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mCaptureSession.setRepeatingRequest(requestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        Log.d(TAG, "onCaptureCompleted: .....");
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        super.onCaptureFailed(session, request, failure);
                        Log.d(TAG, "onCaptureFailed: ...." + failure.toString());
                    }
                }, null);
                emitter.onNext(new FxResult());
            }
        });
    }

    @Override
    public Observable<FxResult> onCapture(final FxRequest request) {
        Log.d(TAG, "capture: ......3333...");
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.REQUEST_BUILDER);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                boolean previewCapture = request.getBoolean(FxRe.Key.PREVIEW_CAPTURE, false);
                Log.d(TAG, "subscribe: capture: ......3333...");

                if (previewCapture) {
                    mCaptureSession.capture(requestBuilder.build(), null, null);
                    emitter.onNext(new FxResult());
                    return;
                }

                if (!isAutoFocusSupported()) {
                    Log.d(TAG, "subscribe: no supported AF , capture");
                    emitter.onNext(new FxResult());
                    return;
                }

                mCaptureSession.capture(requestBuilder.build(), new CameraCaptureSession.CaptureCallback() {

                    void onCapture(CaptureResult result) {
                        Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                        Log.d(TAG, "onCapture: af state   " + afState);
                        if (afState == null) {
                            Log.d(TAG, "onCapture: .......next");
                            emitter.onNext(new FxResult());
                        } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
                                || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState){
                            Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                            Log.d(TAG, "onCapture: .....ae state    " + aeState);
                            if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                                Log.d(TAG, "onCapture: ..... next 1111");
                                emitter.onNext(new FxResult());
                            }
                        }
                    }
//                    @Override
//                    public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
//                        onCapture(partialResult);
//
//                    }

                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        onCapture(result);
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        Log.d(TAG, "onCaptureFailed: .....");
                    }
                }, null);
            }
        });
    }

    @Override
    public Observable<FxResult> onCaptureStillPicture(FxRequest request) {
        Log.d(TAG, "captureStillPicture: .......");
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.REQUEST_BUILDER);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                mCaptureSession.stopRepeating();
//                mCaptureSession.abortCaptures();
                mCaptureSession.capture(requestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                   @NonNull CaptureRequest request,
                                                   @NonNull TotalCaptureResult result) {
                        emitter.onNext(new FxResult());
                        Log.d(TAG, "onCaptureCompleted: pic success .....");
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session,
                                                @NonNull CaptureRequest request,
                                                @NonNull CaptureFailure failure) {
                        Log.d(TAG, "onCaptureFailed: ........." +failure);
                    }
                }, null);
            }
        });
    }

    @Override
    public int createStillCaptureTemplate() {
        return CameraDevice.TEMPLATE_STILL_CAPTURE;
    }

    @Override
    public IBuilderPack getBuilderPack() {
        return new PhotoBuilderPack(this);
    }

    @Override
    public int createPreviewTemplate() {
        return CameraDevice.TEMPLATE_PREVIEW;
    }
}