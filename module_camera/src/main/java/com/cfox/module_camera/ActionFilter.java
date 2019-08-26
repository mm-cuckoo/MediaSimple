package com.cfox.module_camera;


public class ActionFilter {
    private long mStartTime;
    private long mActionTime = 2000;
    private ClickEvent mEvent;

    public ActionFilter(long actionTime, ClickEvent event) {
        this.mActionTime = actionTime;
        this.mEvent = event;
    }

    public void doEvent() {
        long eventTime = System.currentTimeMillis();
        if (mStartTime == 0 || (eventTime - mStartTime) > mActionTime) {
            mStartTime = eventTime;
            if (mEvent != null) mEvent.onClick();
        }
    }

    public interface ClickEvent {
        void onClick();
    }
}
