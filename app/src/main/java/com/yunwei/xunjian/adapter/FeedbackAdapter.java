package com.yunwei.xunjian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.bean.HistoryFeedback;
import com.yunwei.xunjian.util.Tools;

import java.util.List;

public class FeedbackAdapter extends ArrayAdapter<HistoryFeedback> {
    private int resourceId;
    private Context myContext;

    public FeedbackAdapter(Context context, int textViewResourceId, List<HistoryFeedback> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        myContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HistoryFeedback historyFeedback = getItem(position);

        View view;
        final ViewHolder viewHolder;
        if (convertView == null) {
//            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, null);
            viewHolder = new ViewHolder();
            viewHolder.timeStamp = (TextView)view.findViewById(R.id.datetime);
            viewHolder.feedback = (TextView)view.findViewById(R.id.content);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        String dateTime = Tools.stampToDate(historyFeedback.getFeedbackDateTime());
        viewHolder.timeStamp.setText(dateTime);
        viewHolder.feedback.setText(historyFeedback.getFeedbackContent());

        return view;
    }

    class ViewHolder {
        TextView timeStamp;
        TextView feedback;
    }
}
