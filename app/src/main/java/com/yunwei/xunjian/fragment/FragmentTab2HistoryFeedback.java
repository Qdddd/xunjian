package com.yunwei.xunjian.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.activity.FeedbackDetailActivity;
import com.yunwei.xunjian.activity.FinishedWorkDetailActivity;
import com.yunwei.xunjian.activity.MainActivity;
import com.yunwei.xunjian.adapter.FeedbackAdapter;
import com.yunwei.xunjian.adapter.FinishedAdapter;
import com.yunwei.xunjian.bean.HistoryFeedback;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.StatusBarUtil;
import com.yunwei.xunjian.util.Tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.yunwei.xunjian.activity.MainActivity.userName;
import static com.yunwei.xunjian.util.Constants.FINISHED_LIST;
import static com.yunwei.xunjian.util.Constants.GET_FEEDBACK;
import static com.yunwei.xunjian.util.Constants.NULL_LISTVIEW;
import static com.yunwei.xunjian.util.Constants.UPDATE_LISTVIEW;
/**
* FileName: FragmentTab2HistoryFeedback.java
* Description:历史反馈的选项卡2 ：已回复历史反馈
* @author  Monica J
* @Date    2019/4/30 0030  15:42
* @version 1.00
*/

public class FragmentTab2HistoryFeedback extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private ListView listView;
    private View null_data;

    private List<HistoryFeedback> list = new ArrayList<>();

    private FragmentTabFinished.OnFragmentInteractionListener mListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LISTVIEW:
                    swipeRefresh.setVisibility(View.VISIBLE);
                    null_data.setVisibility(View.GONE);
                    FeedbackAdapter feedbackAdapter = new FeedbackAdapter(getActivity(), R.layout.feedback_item, list);
                    listView.setAdapter(feedbackAdapter);
                    break;
                case NULL_LISTVIEW:
                    swipeRefresh.setVisibility(View.GONE);
                    null_data.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("", "onCreate: ");

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(getActivity(), false, R.color.lan);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_feedback_history, container,false);

        listView = (ListView)view.findViewById(R.id.history_feedback_list);
        null_data = view.findViewById(R.id.null_data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FeedbackDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("historyFeedback",list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeResources(R.color.lan);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });

       /* textView_null_list = (TextView)view.findViewById(R.id.null_list);
        textView_null_list.setVisibility(View.GONE);
*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d("", "onActivityCreated: ");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("", "onStart: ");
    }

    @Override
    public void onResume(){
        super.onResume();
        initView();
        Log.d("", "onResume: ");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initView() {
        String URL = GET_FEEDBACK + "?feedbackPerson=" + userName+"&feedbackState=2";//0：全部,1：未回复的,2：回复了的
        HttpUtil.sendOkHttpRequest(URL, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.i("###:",responseData+"##############");
                parseJSONWithJSONObject(responseData);
                if (list.size() > 0) {
                    Message message = new Message();
                    message.what = UPDATE_LISTVIEW;
                    handler.sendMessage(message);
                }else{
                    Log.i("responseData","==========="+responseData+"================");
                    Message message = new Message();
                    message.what = NULL_LISTVIEW;
                    handler.sendMessage(message);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

    }

    private void parseJSONWithJSONObject(String jsonData) {
        list.clear();
        try {
            JSONObject jsonOb= new JSONObject(jsonData);
            Log.i("###:",jsonOb.get("extend").toString()+"##############");
            Log.d("workList", "Length of jason array is : " + jsonOb.length());
            String a=  jsonOb.get("extend").toString();
            if (jsonOb.get("msg").equals("处理成功")) {
                jsonOb = new JSONObject(a);
                a = jsonOb.get("feedbackInfo").toString();
                JSONArray jsonArray = new JSONArray(a);
                for (int i=0; i<jsonArray.length(); i++) {
                    HistoryFeedback historyFeedback=new HistoryFeedback();
                    JSONObject jsonObject=new JSONObject(jsonArray.get(i).toString());
                    historyFeedback.setFeedbackDateTime(Tools.stampToDate(Long.valueOf(jsonObject.getString("feedback_DATE_TIME"))/1000));
                    historyFeedback.setFeedbackContent(jsonObject.getString("feedback_CONTENT"));
                    historyFeedback.setFeedbackPerson(jsonObject.getString("feedback_PERSON"));
                    if(!jsonObject.getString("feedback_PIC_PATH").equals("null")){
                        historyFeedback.setFeedbackPicPath(jsonObject.getString("feedback_PIC_PATH"));
                    }
                    historyFeedback.setReply(jsonObject.getString("revert"));
                    if(historyFeedback.getReply().equals("1")) {
                        historyFeedback.setReplyDateTime(Tools.stampToDate(Long.valueOf(jsonObject.getString("revert_DATE_TIME")) / 1000));
                        historyFeedback.setReplyPerson(jsonObject.getString("revert_PERSON"));
                        historyFeedback.setReplyContent(jsonObject.getString("revert_CONTENT"));
                    }

                    list.add(historyFeedback);
                }
            }else{
                Log.i("FTab1HistoryFeedback","msg!=处理成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
