package com.wwengine.hw;

import android.content.Context;

public class WWHandWrite {

    static {
    	System.loadLibrary("dwEngineHw");
    }
    /***
        #define WWHW_RANGE_NUMBER				0x1         // 识别范围：数字
        #define WWHW_RANGE_LOWER_CHAR			0x2         // 识别范围：小写字母
        #define WWHW_RANGE_UPPER_CHAR			0x4         // 识别范围：大写字母 
        #define WWHW_RANGE_ASC_SYMBOL           0x8         // 识别范围：半角标点符号 
        #define WWHW_RANGE_GB2312				0x8000      // 识别范围：GB2312汉字 
        #define WWHW_RANGE_BIG5					0x200       // 识别范围：BIG5汉字   
        #define WWHW_RANGE_CHN_SYMBOL		    0x800       // 识别范围：中文标点符号         
    ***/

    /*************************************************************************************
     *
     * 注意： 注意： 购买了绑定版本的用户，初始化前，请先调用这里！
     * 
     * param - in, App 的 Context
     *
     *************************************************************************************/
    public static native int apkBinding(Context param);

    
    /*************************************************************************************
     *
     * 注意： 购买了通用版本的用户，初始化前要调用这里！！
     * 
     * name - in, 通常是公司名称
     *
     *************************************************************************************/
    public static native int Authorization(char[] name);
    
    
    /*************************************************************************************
     *
     * 初始化
     * 
     * data - 数据库，一般是内置数据，转null即可
     * 
     * param - 保留
     * 
     *************************************************************************************/
    public static native int hwInit(byte[] data, int param);
    
    
    /*************************************************************************************
     *
     * 单字识别
     * 
     * tracks - in, 笔迹数据
     * result - out, 返回的结果
     * candNum - in, 要求返回汉字个数
     * option - in, 识别范围
     * 
     *************************************************************************************/
    public static native int hwRecognize(short[] tracks, char[] result, int candNum, int option);


    /*************************************************************************************
     *
     * 多字识别，未实现，请勿使用
     * 
     *************************************************************************************/
    public static native int hwRecognizeMulti(short[] tracks, char[] result);
}

