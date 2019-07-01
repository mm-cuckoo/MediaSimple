package com.chao.lib_common.widegts;

import android.text.InputFilter;
import android.text.Spanned;

import java.io.UnsupportedEncodingException;

public class TextFilter implements InputFilter {
    private ITextChangeCall mCall;              //监听回调
    private int mVerifLength;                   //设置监听阈值
    private String mMsg;                        //回调消息
    private boolean isStopInput = false;        //是否不停止进行输入，如果true 当输入字符到达监听长度将不在允许输入，如果false 将进行提示，但可以输入


    public interface ITextChangeCall{
        public void textResult(String text);
    }

    public TextFilter(int textByteLength, String showMsg, ITextChangeCall changeCall){
        this.mCall = changeCall;
        this.mVerifLength = textByteLength;
        this.mMsg = showMsg;
    }

    public TextFilter(int textByteLength, String showMsg, ITextChangeCall changeCall,boolean isStopInput){
        this.mCall = changeCall;
        this.mVerifLength = textByteLength;
        this.mMsg = showMsg;
        this.isStopInput = isStopInput;
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {

        try {
            if((charSequence.toString().getBytes("GBK").length + spanned.toString().getBytes("GBK").length) > mVerifLength){
                mCall.textResult(mMsg);
                if (isStopInput){
                    return "";
                }else {
                    return null;
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}