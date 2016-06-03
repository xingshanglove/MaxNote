package com.wwengine.hw;

import android.content.Context;

public class WWHandWrite {

    static {
    	System.loadLibrary("dwEngineHw");
    }
    /***
        #define WWHW_RANGE_NUMBER				0x1         // ʶ��Χ������
        #define WWHW_RANGE_LOWER_CHAR			0x2         // ʶ��Χ��Сд��ĸ
        #define WWHW_RANGE_UPPER_CHAR			0x4         // ʶ��Χ����д��ĸ 
        #define WWHW_RANGE_ASC_SYMBOL           0x8         // ʶ��Χ����Ǳ����� 
        #define WWHW_RANGE_GB2312				0x8000      // ʶ��Χ��GB2312���� 
        #define WWHW_RANGE_BIG5					0x200       // ʶ��Χ��BIG5����   
        #define WWHW_RANGE_CHN_SYMBOL		    0x800       // ʶ��Χ�����ı�����         
    ***/

    /*************************************************************************************
     *
     * ע�⣺ ע�⣺ �����˰󶨰汾���û�����ʼ��ǰ�����ȵ������
     * 
     * param - in, App �� Context
     *
     *************************************************************************************/
    public static native int apkBinding(Context param);

    
    /*************************************************************************************
     *
     * ע�⣺ ������ͨ�ð汾���û�����ʼ��ǰҪ���������
     * 
     * name - in, ͨ���ǹ�˾����
     *
     *************************************************************************************/
    public static native int Authorization(char[] name);
    
    
    /*************************************************************************************
     *
     * ��ʼ��
     * 
     * data - ���ݿ⣬һ�����������ݣ�תnull����
     * 
     * param - ����
     * 
     *************************************************************************************/
    public static native int hwInit(byte[] data, int param);
    
    
    /*************************************************************************************
     *
     * ����ʶ��
     * 
     * tracks - in, �ʼ�����
     * result - out, ���صĽ��
     * candNum - in, Ҫ�󷵻غ��ָ���
     * option - in, ʶ��Χ
     * 
     *************************************************************************************/
    public static native int hwRecognize(short[] tracks, char[] result, int candNum, int option);


    /*************************************************************************************
     *
     * ����ʶ��δʵ�֣�����ʹ��
     * 
     *************************************************************************************/
    public static native int hwRecognizeMulti(short[] tracks, char[] result);
}

