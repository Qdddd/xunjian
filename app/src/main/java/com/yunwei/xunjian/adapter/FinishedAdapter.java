package com.yunwei.xunjian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yunwei.xunjian.R;

import java.util.List;
import java.util.Map;

public class FinishedAdapter extends ArrayAdapter<Map<String, String>> {

    private Context context;
    private int resourceId;

    public FinishedAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup patent){
        Map<String, String> myMap = getItem(position);

        FinishedAdapter.ViewHolder viewHolder = new FinishedAdapter.ViewHolder();
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder.workNum = convertView.findViewById(R.id.workNum);
            viewHolder.workName = convertView.findViewById(R.id.workName);
            viewHolder.line = convertView.findViewById(R.id.line);
            viewHolder.sendOrderPerson = convertView.findViewById(R.id.sendOrderPerson);
            viewHolder.sendOrderTime = convertView.findViewById(R.id.sendOrderTime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (FinishedAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder.workNum.setText(myMap.get("workNum"));
        viewHolder.workName.setText(myMap.get("workName"));
        viewHolder.line.setText(myMap.get("line"));
        viewHolder.sendOrderPerson.setText(myMap.get("sendOrderPerson"));
        viewHolder.sendOrderTime.setText(myMap.get("sendOrderTime"));
        return convertView;
    }

    class ViewHolder{
        private TextView workNum;
        private TextView workName;
        private TextView line;
        private TextView sendOrderPerson;
        private TextView sendOrderTime;
    }
}
