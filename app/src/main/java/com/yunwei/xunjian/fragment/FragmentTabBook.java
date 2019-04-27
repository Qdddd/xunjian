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
import com.yunwei.xunjian.activity.DepartmentActivity;
import com.yunwei.xunjian.adapter.AAComAdapter;
import com.yunwei.xunjian.adapter.AAViewHolder;
import com.yunwei.xunjian.bean.ContactItem;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.GET_Orgnizetion;
import static com.yunwei.xunjian.util.Constants.NULL_LISTVIEW;
import static com.yunwei.xunjian.util.Constants.UPDATE_LISTVIEW;

public class FragmentTabBook extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private ListView listView;
    private TextView textView_null_list;
    private List<ContactItem> list = new ArrayList<>();
    private FragmentTabFinished.OnFragmentInteractionListener mListener;
    private AAComAdapter bookAdapter;
    private TextView title;
    private ImageView imageView_back;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LISTVIEW:
                        //  swipeRefresh.setVisibility(View.VISIBLE);
                     //     textView_null_list.setVisibility(View.GONE);
                    Log.i("list==","size"+list.size());
                   bookAdapter =new AAComAdapter<ContactItem>(getContext(), R.layout.contact_list_item,list) {
                        @Override
                        public void convert(AAViewHolder holder, ContactItem mt) {
                            TextView name = holder.getTextView(R.id.name);
                            name.setText(mt.getName());
                        }

                    };
                    listView.setAdapter(bookAdapter);
                    break;
                case NULL_LISTVIEW:
                   // swipeRefresh.setVisibility(View.GONE);
                      textView_null_list.setVisibility(View.VISIBLE);
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
       // initView();
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(getActivity(), false, R.color.lan);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book, container,false);
        listView = (ListView)view.findViewById(R.id.listbook);
        title=view.findViewById(R.id.title);
        title.setText("通讯录");
        initView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getActivity(),list.get(position).getORGANIZ_CODE(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), DepartmentActivity.class);
                intent.putExtra("ORGANIZ_CODE", list.get(position).getORGANIZ_CODE());
                //intent.putExtra("userName", userName);
                //intent.putExtra("areaCode", list.get(position).get("areaCode"));
                startActivity(intent);
            }
        });

       /*textView_null_list = (TextView)view.findViewById(R.id.null_list);
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


    //初始化视图
    private void initView() {

       String URL = GET_Orgnizetion;
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
             /*   getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    //    swipeRefresh.setRefreshing(false);
                    }
                });*/

            }

           @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
             /*   getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  swipeRefresh.setRefreshing(false);
                    }
                });*/
            }
        });
     /*   Message message = new Message();
        message.what = UPDATE_LISTVIEW;
        handler.sendMessage(message);*/
    }

    private void parseJSONWithJSONObject(String jsonData) {
        list.clear();
        Log.d("count", "Length of list count is : " + list.size());
        try {
            JSONObject jsonOb= new JSONObject(jsonData);
            String a=  jsonOb.get("extend").toString();
            if (!a.equals("{}")) {
                jsonOb = new JSONObject(a);
                a = jsonOb.get("organizInfo").toString();
            }
            JSONArray jsonArray = new JSONArray(a);
            Log.d("organizInfo", "Length of jason array is : " + jsonArray.length());
            for (int i=0; i<jsonArray.length(); i++) {
                String ORGANIZ_CODE, name;
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ORGANIZ_CODE  = jsonObject.getString("ORGANIZ_CODE");
                name = jsonObject.getString("ORGANIZ_POST_NAME");
                ContactItem con=new ContactItem();
                con.setName(name);
                con.setORGANIZ_CODE(ORGANIZ_CODE);
                list.add(con);
            }
            Log.i("list112==","size"+list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
