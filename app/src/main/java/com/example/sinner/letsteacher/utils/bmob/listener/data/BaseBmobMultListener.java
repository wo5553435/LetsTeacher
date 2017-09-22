package com.example.sinner.letsteacher.utils.bmob.listener.data;

import java.util.List;

/**
 * Created by win7 on 2016-12-30.
 */

public interface BaseBmobMultListener {

    void onSuccess();
    void onFailure(String errorcode,String errormsg);
    void onSuccess(List<Integer> errorcount);
}
