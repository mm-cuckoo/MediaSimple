package com.cfox.lib_audio.file;

public class FileEngine {

    private FileControl mFileControl;

    public FileEngine() {
        mFileControl = FileControl.getInstance();
    }

    public String getRecorderFilePath(String filePath, String fileName) {
        return mFileControl.getRecorderFilePath(filePath, fileName);
    }

    public boolean verifyFile(String filePath) {

        return mFileControl.verifyFile(filePath);
    }
}
