package com.example.catluan_api;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    ListView listView1;
    ArrayList<Jobs> arrayList;
    AdapterJobs adapter;

    public static String url = "http://5cbd169fecded20014c20435.mockapi.io/api/business";
    public static int REQUESTCODEADD = 111;
    public static int REQUESTCODEEDIT = 211;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhXa();
        loadDataToListView();
    }

    //Khai báo các item có trên menu:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Tạo hành động cho menu - nhấn vào item add thì chuyển tới trang thêm:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_Add: {
                Intent intent = new Intent(MainActivity.this, UpdateJobsActivity.class);
                intent.putExtra("url", url);
                startActivityForResult(intent, REQUESTCODEADD);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Hàm trả về kq sau khi thêm xong:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODEADD && resultCode == UpdateJobsActivity.RESULTCODEADD) {
            loadDataToListView();
        } else {
            loadDataToListView();
        }
    }

    public void anhXa(){
        listView1 = (ListView)findViewById(R.id.listView1);
    }

    //Hàm load Data lên listview và custom lại listview:
    public void loadDataToListView(){
        arrayList = getDataAll();
        adapter = new AdapterJobs(this, R.layout.layout_custom_listview,arrayList);
        listView1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Hàm định dạng ngày giờ:
    public Date convertTimeStampToDate(long time){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        sdf.setTimeZone(tz);
        String localTime = sdf.format(new Date(time * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //Hàm mock api và định dạng json vào listview:
    public ArrayList<Jobs> getDataAll(){
        final ArrayList<Jobs> list =new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Date startDate = convertTimeStampToDate(jsonObject.getLong("startDate"));
                        Date finishDate = convertTimeStampToDate(jsonObject.getLong("finishDate"));

                        Jobs jobs = new Jobs(jsonObject.getString("id"),
                                jsonObject.getString("projectName"),
                                startDate,
                                finishDate);

                        list.add(jobs);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AAA","error\n" + error.toString());

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        return list;
    }
}
