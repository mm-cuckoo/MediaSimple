package com.cfox.camera.camera.session;

import android.content.Context;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.SystemClock;
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

    private static final long PRE_CAPTURE_TIMEOUT_MS = 1000;

    private final Object mCaptureLock = new Object();
    private boolean mCaptured = false;
    private long mCaptureTime;
    private boolean mFirstFrameCompleted = false;

    public PhotoSession(Context context) {
        super(context);
    }


    @Override
    public Observable<FxResult> onSendRepeatingRequest(final FxRequest request) {
        Log.d(TAG, "onSendRepeatingRequest: .....");
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                onRepeatingRequest(request,null);
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
                mCaptured = false;

                if (previewCapture) {
                    mCaptureSession.capture(requestBuilder.build(), null, null);
                    emitter.onNext(new FxResult());
                    return;
                }

                mCaptureTime = SystemClock.elapsedRealtime();
                mCaptureCallback.setEmitter(emitter, true);
                mCaptureSession.capture(requestBuilder.build(),mCaptureCallback, null);
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
    public Observable<FxResult> onPreviewRepeatingRequest(final FxRequest request) {
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mCaptureCallback.setEmitter(emitter, false);
                onRepeatingRequest(request, mCaptureCallback);
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

    private CaptureCallback mCaptureCallback = new CaptureCallback();

    private class CaptureCallback extends CameraCaptureSession.CaptureCallback {

        private ObservableEmitter<FxResult> mEmitter;
        private boolean mIsCapture = false;

        void setEmitter(ObservableEmitter<FxResult> emitter, boolean isCapture) {
            this.mEmitter = emitter;
            this.mIsCapture = isCapture;
            if (!isCapture) {
                mFirstFrameCompleted = false;
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            onCapture(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            if (!mFirstFrameCompleted) {
                mFirstFrameCompleted = true;
                FxResult fxResult = new FxResult();
                fxResult.put(FxRe.Key.FIRST_FRAME_CALLBACK, FxRe.Value.OK);
                mEmitter.onNext(fxResult);
                Log.d(TAG, "mFirstFrameCompleted  onCaptureCompleted: .....");
            }

            onCapture(result);
        }

        void onCapture(CaptureResult result) {
            synchronized (mCaptureLock) {

                Integer aeState1 = result.get(CaptureResult.CONTROL_AE_STATE);
                Integer awbState1 = result.get(CaptureResult.CONTROL_AWB_STATE);
                Log.d(TAG, "onCapture: ae ---- :" + aeState1  + "   awb:" + awbState1);


                if (!mIsCapture) return;
                if (!isAutoFocusSupported() && !mCaptured) {
                    Log.d(TAG, "subscribe: no supported AF , capture");
                    mCaptured = true;
                    mEmitter.onNext(new FxResult());
                    return;
                }

                boolean readyCapture;

                Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                Log.d(TAG, "onCapture: af state   " + afState);

                if (afState == null) return;

                readyCapture = CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
                        || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState;

                Log.d(TAG, "onCapture: readyCaptur11111e:" + readyCapture);


                if (!isLegacyLocked()) {
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    Integer awbState = result.get(CaptureResult.CONTROL_AWB_STATE);
                    Log.d(TAG, "onCapture: ae  :" + aeState  + "   awb:" + awbState);
                    if (aeState == null || awbState == null) {
                        return;
                    }

                    readyCapture = readyCapture &&
                            aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED &&
                            awbState == CaptureResult.CONTROL_AWB_STATE_CONVERGED;
                }


                Log.d(TAG, "onCapture: readyCapture22222:" + readyCapture);

                if (!readyCapture && hitTimeoutLocked()) {
                    Log.w(TAG, "Timed out waiting for pre-capture sequence to complete.");
                    readyCapture = true;
                }

                if (readyCapture && !mCaptured) {
                    mCaptured = true;
                    mIsCapture = false;
                    mEmitter.onNext(new FxResult());
                }
            }
        }
    }

    private boolean hitTimeoutLocked() {
        return (SystemClock.elapsedRealtime() - mCaptureTime) > PRE_CAPTURE_TIMEOUT_MS;
    }
}