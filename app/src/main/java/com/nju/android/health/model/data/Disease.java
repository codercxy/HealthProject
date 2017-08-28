package com.nju.android.health.model.data;

import android.widget.TextView;

import com.nju.android.health.utils.CircleImg;

/**
 * Created by chy on 2017/6/26.
 */

public class Disease {
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getImgRes() {
        return imgRes;
    }

    public void setImgRes(Integer imgRes) {
        this.imgRes = imgRes;
    }

    private Integer imgRes;
    private String text;
}
