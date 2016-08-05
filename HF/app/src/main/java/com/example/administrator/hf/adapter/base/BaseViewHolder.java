package com.example.administrator.hf.adapter.base;

import android.view.View;

/**
 * ViewHolder 基类
 * Created by hjhrq1991 on 15/1/31.
 */
public abstract class BaseViewHolder {

    public BaseViewHolder(View convertView) {
//        ButterKnife.bind(this, convertView);
        convertView.setTag(this);
    }

    public abstract void render(int position, View convertView);
}
