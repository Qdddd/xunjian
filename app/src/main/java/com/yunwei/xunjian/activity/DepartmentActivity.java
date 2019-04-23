package com.yunwei.xunjian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.adapter.AAComAdapter;
import com.yunwei.xunjian.adapter.AAViewHolder;
import com.yunwei.xunjian.bean.ContactItem;
import com.yunwei.xunjian.util.ItentButtonUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

//部门
public class DepartmentActivity  extends ItentButtonUtil {
   // private TextView title;
    private ImageView imageView_back;
    private String Organiz_code;
    private ListView listView;
    private List<ContactItem> list = new ArrayList<>();
    private AAComAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);
        imageView_back=findViewById(R.id.back);
        back(imageView_back);
        initView();
        //公司编号
       Organiz_code=getIntent().getExtras().getString("Organiz_code");
       listView=findViewById(R.id.listdepartment);
       adapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DepartmentActivity.this,list.get(position).getName(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DepartmentActivity.this,ContactUserActivity.class );
                intent.putExtra("Department_code", list.get(position).getOrganiz_code());
                //intent.putExtra("userName", userName);
                //intent.putExtra("areaCode", list.get(position).get("areaCode"));
                startActivity(intent);
            }
        });
    }
    public void initView(){
        ContactItem con=new ContactItem();
        con.setName("运维检修部");
        con.setORGANIZ_CODE("1000");
        list.add(con);
        ContactItem con1=new ContactItem();
        con1.setName("营销部");
        con1.setORGANIZ_CODE("1001");
        list.add(con1);
        ContactItem con2=new ContactItem();
        con2.setName("农电部");
        con2.setORGANIZ_CODE("1002");
        list.add(con2);

    }
    public void adapter(){
        bookAdapter =new AAComAdapter<ContactItem>(DepartmentActivity.this,R.layout.contact_list_item,list) {
            @Override
            public void convert(AAViewHolder holder, ContactItem mt) {
                TextView name = holder.getTextView(R.id.name);
                name.setText(mt.getName());
            }

        };
        listView.setAdapter(bookAdapter);
    }
}
