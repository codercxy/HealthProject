package com.nju.android.health.bswk;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.nju.android.health.R;


/**
 * Created by Administrator on 2016/3/16.
 */
public class Xuetangshiduan extends PopupWindow {

    private Button canqian, canhou, zaocanqian,zaocanhou,wucanqian,wucanhou,wancanqian,wancanhou,shuiqian,yejian,suiji;
    private View mMenuView;

    public Xuetangshiduan(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.xuetangshiduan, null);
//        btn_qingtili = (Button)mMenuView.findViewById(R.id.btn_qingtili);
//        btn_zhongtili = (Button)mMenuView.findViewById(R.id.btn_zhongtili);
//        btn_zhonggtili = (Button)mMenuView.findViewById(R.id.btn_zhonggtili);
        canqian= (Button) mMenuView.findViewById(R.id.canqian);
        canhou = (Button) mMenuView.findViewById(R.id.canhou);
        zaocanqian= (Button) mMenuView.findViewById(R.id.zaocanqian);
        zaocanhou = (Button) mMenuView.findViewById(R.id.zaocanhou);
        wucanqian = (Button) mMenuView.findViewById(R.id.wucanqian);
        wucanhou = (Button) mMenuView.findViewById(R.id.wucanhou);
        wancanqian= (Button) mMenuView.findViewById(R.id.wancanqian);
        wancanhou = (Button) mMenuView.findViewById(R.id.wancanhou);
        shuiqian= (Button) mMenuView.findViewById(R.id.shuiqian);
        yejian = (Button) mMenuView.findViewById(R.id.yejian);
        suiji= (Button) mMenuView.findViewById(R.id.suiji);

        canqian.setOnClickListener(itemsOnClick);
        canhou.setOnClickListener(itemsOnClick);
        zaocanqian.setOnClickListener(itemsOnClick);
        zaocanhou.setOnClickListener(itemsOnClick);
        wucanqian.setOnClickListener(itemsOnClick);
        wucanhou.setOnClickListener(itemsOnClick);
        wancanqian.setOnClickListener(itemsOnClick);
        wancanhou.setOnClickListener(itemsOnClick);
        shuiqian.setOnClickListener(itemsOnClick);
        yejian.setOnClickListener(itemsOnClick);
        suiji.setOnClickListener(itemsOnClick);


        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0ffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.linearLayoutXT).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
