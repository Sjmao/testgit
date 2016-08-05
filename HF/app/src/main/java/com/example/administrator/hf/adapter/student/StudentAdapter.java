package com.example.administrator.hf.adapter.student;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.hf.R;
import com.example.administrator.hf.adapter.base.SimpleOneViewHolderBaseAdapter;

import test.greenDAO.bean.Entity;

/**
 * Created by Administrator on 2016/8/3.
 */
public class StudentAdapter extends SimpleOneViewHolderBaseAdapter<Entity> {

    public StudentAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewHolder holder) {
        Entity entity=getItem(position);
        TextView textView=(TextView)convertView.findViewById(R.id.student_id);
        EditText age=(EditText)convertView.findViewById(R.id.student_age);
        EditText name=(EditText)convertView.findViewById(R.id.student_name);
        EditText score=(EditText)convertView.findViewById(R.id.student_score);
        textView.setText(entity.getId()+"");
        age.setText(entity.getAge()+"");
        name.setText(entity.getName());
        score.setText(entity.getScore()+"");
        return convertView;
    }

    @Override
    public int getItemResource() {
        return R.layout.student_item;
    }
}
