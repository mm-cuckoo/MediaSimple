package debug;

import android.os.Environment;

import com.cfox.rpaudio.file.FileControl;
import com.cfox.lib_common.base.BaseApplication;

public class PlayerApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        String pathBase = Environment.getExternalStorageDirectory().getPath();
        FileControl control = FileControl.getInstance();
        control.setFileExtRule(new FileControl.FileExtRule() {
            @Override
            public String rule() {
                return "-" + 123;
            }
        });

        control.setRecorderFilePath(pathBase + "/MyRecorder001/");
        control.setRecorderFileName("myRecorderName");

    }
}
