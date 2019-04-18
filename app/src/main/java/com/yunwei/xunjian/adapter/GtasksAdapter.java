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

public class GtasksAdapter extends ArrayAdapter<Map<String, String>> {

    private Context context;
    private int resourceId;

    public GtasksAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup patent){
        Map<String, String> myMap = getItem(position);

        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder.textView1 = convertView.findViewById(R.id.workListNum);
            viewHolder.textView2 = convertView.findViewById(R.id.gtask);
            viewHolder.textView3 = convertView.findViewById(R.id.lineName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView1.setText(myMap.get("workListNo"));
        viewHolder.textView2.setText(myMap.get("gtasks"));
        viewHolder.textView3.setText(myMap.get("lineName"));
        return convertView;
    }

    class ViewHolder{
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
    }
}
