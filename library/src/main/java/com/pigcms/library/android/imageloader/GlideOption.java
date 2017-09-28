package com.pigcms.library.android.imageloader;

import com.pigcms.library.utils.ImageResizer;

/**
 * Created by sinner on 2017-07-16.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class GlideOption {

    private int placeHolde=-1;//占位符
    private ImageResizer size=null;
    private int errorDrawable=-1;
    private boolean  isCrossFade=false;//这里的过渡 可能在封装imgageview和gif图出问题 所以建议关闭或自己处理
}
