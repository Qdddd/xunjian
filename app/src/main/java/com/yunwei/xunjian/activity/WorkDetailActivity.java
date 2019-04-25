package com.yunwei.xunjian.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.fragment.FragmentTabBook;
import com.yunwei.xunjian.util.ItentButtonUtil;

public class WorkDetailActivity extends ItentButtonUtil {
    private TextView title;
    private ImageView imageView_back;
    private Button soso;
    private TextView end;;
    Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail);
        title=findViewById(R.id.title);
        imageView_back=findViewById(R.id.back);
        end=(TextView)findViewById(R.id.end);
        //end.setBackgroundResource(R.mipmap.add);
        drawable = getResources().getDrawable(R.mipmap.add);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        end.setCompoundDrawables(drawable,null,null,null);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(WorkDetailActivity.this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.main, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        switch (item.getItemId()) {
                            case R.id.ganta:
                                Intent intent = new Intent(WorkDetailActivity.this,TaskDispose.class);
                                startActivity(intent);
                                break;
                            case R.id.kaiguan:
                                Intent intent2 = new Intent(WorkDetailActivity.this,SwitchActivity.class);
                                startActivity(intent2);
                                break;
                            case R.id.bianyaqi:
                                Intent intent3 = new Intent(WorkDetailActivity.this,TransformerActivity.class);
                                startActivity(intent3);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                //显示(这一行代码不要忘记了)
                popup.show();

            }
        });
        //end.setText("123");
        //back(end);
        title.setText("工单详情");
        imageView_back.setImageResource(R.mipmap.return1);
        back(imageView_back);
        soso=findViewById(R.id.soso);
        IntentNull(WorkDetailActivity.this,soso, FragmentTabBook.class);
    }
}
