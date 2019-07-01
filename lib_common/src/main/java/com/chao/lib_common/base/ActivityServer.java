package com.chao.lib_common.base;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActivityServer {

    private static Map<String ,Activity> activityMap = new HashMap<String ,Activity>();

    private static ActivityServer server = new ActivityServer();

    private String mActivityName = null;

    private Activity mActivity;

    public static synchronized ActivityServer init(Activity activity){
        server.setActivityName(activity);
        return server;
    }

    private void setActivityName(Activity activity){
        mActivity = activity;
        mActivityName = activity.getClass().getName();
    }

    public void addActivity(){
        activityMap.put(mActivityName, mActivity);
    }

    public void removeActivity(){
        activityMap.remove(mActivityName);
    }

    public void finshActivity(){
        Activity act = activityMap.get(mActivityName);
        if(act != null){
            act.finish();
        }
    }

    public static void finshAllActivity(){

        Set<String> keys = activityMap.keySet();
        for(String key : keys){
            Activity act = activityMap.get(key);
            if(act != null){
                act.finish();
            }
        }
    }

    /**
     * finsh 除本身activity 的其余activity
     * @param activity
     */
    public static void finshOtherActivitys(Activity activity){
        finshOtherActivitys(activity.getClass());
    }

    /**
     * finsh 除本身activity 的其余activity
     * @param cls
     */
    public static void finshOtherActivitys(Class cls){

        String activityName = cls.getName();
        Set<String> keys = activityMap.keySet();
        for(String key : keys){
            if(activityName.equals(key))
                continue;
            Activity act = activityMap.get(key);
            if(act != null){
                act.finish();
            }
        }
    }

    /**
     * finsh 指定activity
     * @param activity
     */
    public static void finshActivity(Activity activity){
        finshActivity(activity.getClass());
    }

    /**
     * finsh 指定activity
     * @param cls
     */
    public static void finshActivity(Class cls){
        String activityName = cls.getName();
        Set<String> keys = activityMap.keySet();
        for(String key : keys){
            if(activityName.equals(key)){
                Activity act = activityMap.get(key);
                if(act != null){
                    act.finish();
                }

                break;
            }
        }
    }
}