package com.swufe.newapplication;
//自定义列表的界面
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);

        ListView listView=(ListView)findViewById(R.id.mylist);
        //获取列表控件，其id为mylist;
        //并转换

        String data[]={"111","2222"};
        //定义数据对象

        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        //adapter对象 (数据量较大，但列表可见的有限。adapter适配器用来处理数据和列表ListView的关系。我们提供数据给它，它来与列表适配）
        //需要3个对象：this(context，当前对象),(列表的布局)，（数据）
        //android.R.layout.simple_list_item_1 :安卓平台提供的布局
        //<string>:泛型，表示数据项是字符串的集合

        listView.setAdapter(adapter);
        //给当前控件设置adapter


        //列表内容只有一项时：里面存放String字符串

        //列表内容有多项时：  存放Map<Key键，Value值>,需要定义类型，比如Map<String,String>
        //TextView控件显示数据时，需要明确数据和控件的对应关系



}
}
