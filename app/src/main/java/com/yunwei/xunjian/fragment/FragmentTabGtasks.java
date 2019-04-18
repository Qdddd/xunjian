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
import android.widget.ListView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.activity.MainActivity;
import com.yunwei.xunjian.adapter.GtasksAdapter;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.GTASKS;
import static com.yunwei.xunjian.util.Constants.NULL_LISTVIEW;
import static com.yunwei.xunjian.util.Constants.UPDATE_LISTVIEW;

public class FragmentTabGtasks extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private ListView listView;
    private TextView textView_null_list;

    private List<Map<String, String>> list = new ArrayList<>();

    private FragmentTabGtasks.OnFragmentInteractionListener mListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LISTVIEW:
                    swipeRefresh.setVisibility(View.VISIBLE);
                 //   textView_null_list.setVisibility(View.GONE);
                    GtasksAdapter gtasksAdapter = new GtasksAdapter(getActivity(), R.layout.worklist_item, list);
                    listView.setAdapter(gtasksAdapter);
                    break;
                case NULL_LISTVIEW:
                    swipeRefresh.setVisibility(View.GONE);
                 //   textView_null_list.setVisibility(View.VISIBLE);
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
        View view = inflater.inflate(R.layout.fragment_tab_gtasks, container,false);

        TextView textView_title = view.findViewById(R.id.title);
        textView_title.setText("待办工单");

        listView = (ListView)view.findViewById(R.id.gtasksList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent intent = new Intent(getActivity(), );
                intent.putExtra("type", "old");
                intent.putExtra("userName", userName);
                intent.putExtra("areaCode", list.get(position).get("areaCode"));
                startActivity(intent);*/
            }
        });
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
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

        String URL = GTASKS + "?name=" + MainActivity.userName;
        HttpUtil.sendOkHttpRequest(URL, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                if(!responseData.equals("[]")) {
                    parseJSONWithJSONObject(responseData);

                    Message message = new Message();
                    message.what = UPDATE_LISTVIEW;
                    handler.sendMessage(message);
                }else{
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
            String workListNo, gtasks, lineName;
            JSONArray jsonArray = new JSONArray(jsonData);
            Log.d("workList", "Length of jason array is : " + jsonArray.length());
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                workListNo  = jsonObject.getString("workListNo");
                gtasks = jsonObject.getString("gtasks");
                lineName = jsonObject.getString("lineName");

                Map<String, String> map = new HashMap<>();
                map.put("workListNo", workListNo);
                map.put("gtasks", gtasks);
                map.put("lineName", lineName);

                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
