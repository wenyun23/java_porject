package com.example.online_photo;

//全局类，用于存一些全局静态变量
public class Tool {
    public static final int STATE_LOADING=0;//正在加载
    public static final int STATE_COMPLETE=1;//加载完成
    public static final int STATE_FAILURE=2;//加载异常，点击重试
}
