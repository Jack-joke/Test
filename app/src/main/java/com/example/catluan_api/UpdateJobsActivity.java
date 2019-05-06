package com.example.catluan_api;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UpdateJobsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnUpdate;
    EditText edtProjectName, edtStartDate, edtFinishDate;
    Calendar cal;

    public static int RESULTCODEADD = 112;
    public static int RESULTCODEEDIT = 113;
    Jobs jobs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jobs);

        anhXa();

        // Sự kiện click EditText
        edtStartDate.setOnClickListener(this);
        edtFinishDate.setOnClickListener(this);

        if (getIntent().getSerializableExtra("job") != null) {
            btnUpdate.setText("Edit job");
            jobs = (Jobs) getIntent().getSerializableExtra("job");

            edtProjectName.setText(jobs.getProjectName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            edtStartDate.setText(sdf.format(jobs.getStartDate()));
            edtFinishDate.setText(sdf.format(jobs.getFinishDate()));
        } else {
            btnUpdate.setText("Add job");
        }

    }

    public void anhXa() {
        btnUpdate = (Button) findViewById(R.id.btn_Update);
        edtFinishDate = (EditText) findViewById(R.id.edt_FinishDate);
        edtProjectName = (EditText) findViewById(R.id.edt_ProjectName);
        edtStartDate = (EditText) findViewById(R.id.edt_StartDate);
    }

    public void showDatePickerDialog(final EditText edt){
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (dayOfMonth >= 10) {
                    if (month >= 9) {
                        edt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    } else {
                        edt.setText(dayOfMonth + "/0" + (month + 1) + "/" + year);
                    }
                } else {
                    if (month >= 9) {
                        edt.setText("0"+dayOfMonth + (month + 1) + "/" + year);
                    } else {
                        edt.setText("0"+dayOfMonth + "/0" + (month + 1) + "/" + year);
                    }
                }
            }
        };
        String date = "";
        if (!edt.getText().toString().isEmpty()) {
            date = edt.getText().toString();

        } else {
            cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            date = sdf.format(cal.getTime());
        }
        String strArrtmp[] = date.split("/");
        int year = Integer.parseInt(strArrtmp[2]);
        int month = Integer.parseInt(strArrtmp[1])-1;
        int day = Integer.parseInt(strArrtmp[0]);
        DatePickerDialog pic = new DatePickerDialog(UpdateJobsActivity.this,
                callback,year,month,day);
        pic.show();
    }

    public long convertDateToTimeStamp(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date =  sdf.parse(strDate);
            long timestamp = date.getTime()/1000L;

            return timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_StartDate: {
                showDatePickerDialog(edtStartDate);
                break;
            }
            case  R.id.edt_FinishDate: {
                showDatePickerDialog(edtFinishDate);
                break;
            }
        }
    }

    public void updateJob(View view) {
        if (edtProjectName.getText().toString().isEmpty()
                    || edtStartDate.getText().toString().isEmpty()
                    || edtStartDate.getText().toString().isEmpty()) {
            Toast.makeText(UpdateJobsActivity.this, "Không được để rỗng", Toast.LENGTH_LONG).show();
            return;
        }
        String strStartDate = edtStartDate.getText().toString();
        String strFinishDate = edtFinishDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = sdf.parse(strStartDate);
            Date finishDate = sdf.parse(strFinishDate);
            if (finishDate.getTime() >= startDate.getTime()){
                if (getIntent().getExtras().get("url") != null) {
                    String url = getIntent().getExtras().get("url").toString();

                    HashMap data = new HashMap();
                    long startDateTimeStamp = convertDateToTimeStamp(edtStartDate.getText().toString());
                    long finishDateTimeStamp = convertDateToTimeStamp(edtFinishDate.getText().toString());
                    data.put("projectName", edtProjectName.getText().toString());
                    data.put("startDate", startDateTimeStamp);
                    data.put("finishDate", finishDateTimeStamp);

                    if (getIntent().getSerializableExtra("job") == null) {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                                new JSONObject(data), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                setResult(RESULTCODEADD);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Update", "error\n" + error.getMessage());
                            }
                        });

                        RequestQueue queue = Volley.newRequestQueue(this);
                        queue.add(jsonObjectRequest);
                    } else {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                                url + "/" +jobs.getId(),
                                new JSONObject(data), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                setResult(RESULTCODEADD);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Update", "error\n" + error.getMessage());
                            }
                        });

                        RequestQueue queue = Volley.newRequestQueue(this);
                        queue.add(jsonObjectRequest);
                    }
                }

            } else {
                Toast.makeText(UpdateJobsActivity.this,
                        "Finish date must be larger start date",
                        Toast.LENGTH_LONG).show();
            }

        } catch (ParseException e) {
            Toast.makeText(UpdateJobsActivity.this,
                    "Field 'StartDate' or 'FinishDate' is not valid format",
                    Toast.LENGTH_LONG).show();
        }
    }
}
