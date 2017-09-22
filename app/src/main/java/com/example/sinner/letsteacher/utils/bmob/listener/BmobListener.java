package com.example.sinner.letsteacher.utils.bmob.listener;

import com.example.sinner.letsteacher.utils.bmob.listener.data.BaseBmobListener;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by win7 on 2016-12-28.
 */

public abstract class BmobListener<T> implements BaseBmobListener {

    private BmobException exception;
    private T t;


}
