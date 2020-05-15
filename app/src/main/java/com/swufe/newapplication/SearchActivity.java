package com.swufe.newapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener{

    EditText input;//输入控件
    String TAG="TAG",updateDate="";
    Handler handler;
    String NR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        input=(EditText)findViewById(R.id.inputin);//引入输入控件

        SharedPreferences SP=getSharedPreferences("Time", Activity.MODE_PRIVATE);
        updateDate=SP.getString("update_rate","");
        Date today= Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd");
        final String today_sdr=sdf.format(today);

        Log.i(TAG,"oncreate:需要更新的时间"+updateDate);
        Log.i(TAG,"oncreate:当前时间"+today_sdr);

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE,7);
        today = ca.getTime();
        final String enddate = sdf.format(today);
        if(!today_sdr.equals(updateDate)){
            Thread t=new Thread(this);
            t.start();
            Log.i(TAG,"oncreate:需要更新");
        }
        else{
            Log.i(TAG,"oncreate:不需要更新");
        }
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    SharedPreferences SP=getSharedPreferences("Time",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=SP.edit();
                    editor.putString("update_rate",enddate);
                    Log.i(TAG,"oncreate:更新时间"+enddate);
                    Toast.makeText(SearchActivity.this,"已更新",Toast.LENGTH_LONG).show();;
                }
                super.handleMessage(msg);
            }


        };



    }
    public void onClick(View btn) {
        NR=input.getText().toString();
        if (NR.length() > 0) {
            Log.i("TAG","run="+NR);

            if(btn.getId()==R.id.select){  //引入搜索控件
                SharedPreferences sp=getSharedPreferences("Title", Activity.MODE_PRIVATE);
                int m=sp.getInt("I",100);
                String data[]=new String[m+1];
                for(int i=0,x=0;i<=m;i++){
                    String I= String.valueOf(i);
                    String v=sp.getString(I,"  ");
                    if(v.contains(NR)){
                        data[x]=v;
                        Log.i("TAG","run="+v);
                        x++;
                    }
                }
                if(data[0]==null){
                    Toast.makeText(this, "没有查到哦", Toast.LENGTH_SHORT).show();
                    String erry[]={"空"};
                    ListView listView=findViewById(R.id.list);  //引入列表
                    ListAdapter adapter=new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_list_item_1,erry);
                    listView.setAdapter(adapter);                }
                else{
                    ListView listView=findViewById(R.id.list);
                    ListAdapter adapter=new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_list_item_1,data);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(this);}
            }
        } else {
            Toast.makeText(this, "请输入查询关键词", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document doc=null;
        try {
            doc= Jsoup.connect("https://it.swufe.edu.cn/index/tzgg.htm").get();
            Log.i("TAG","run="+doc.title());
            Elements tables=doc.getElementsByTag("li");//一直报错 原因查明：包的引用出bug
            //解析网页


            SharedPreferences sp=getSharedPreferences("Title",Activity.MODE_PRIVATE);//获取SP里保存的数据
            SharedPreferences.Editor editor=sp.edit();
            for(int j=65;j<85;j++){
                String A = tables.get(j).getElementsByTag("a").attr("href");
                String B = "https://it.swufe.edu.cn/".concat(A.substring(2));
                Log.i(TAG,"run: url["+j+"]=" + B);
                String J=String.valueOf(j);
                editor.putString(J,B);
            }
            Elements divs=doc.getElementsByTag("span");
            Log.i("TAG","run="+divs);

            int I=0;
            for(int i=9;i< divs.size()-3;i+=2){
                Element td=divs.get(i);
                String val=td.text();//text标红
                String II=String.valueOf(I);
                Log.i("TAG","run="+val+II);
                editor.putString(II,val);
                I++;
            }

            editor.putInt("I",I);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Message msg=handler.obtainMessage(5);
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sp=getSharedPreferences("Title", Activity.MODE_PRIVATE);
        String Position=String.valueOf(position+65);
        String URL=sp.getString(Position,"");
        Log.i("TAG","run="+URL);
        Intent web=new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(web);
    }
}