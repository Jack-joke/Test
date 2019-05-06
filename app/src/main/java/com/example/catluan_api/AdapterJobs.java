package com.example.catluan_api;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterJobs extends ArrayAdapter<Jobs> implements View.OnClickListener {
    int idLayout;
    Context context;
    ArrayList<Jobs> arrayList;

    public AdapterJobs(@NonNull Context context, int resource, @NonNull ArrayList<Jobs> objects) {
        super(context, resource, objects);
        this.idLayout = resource;
        this.context = context;
        this.arrayList = objects;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final int clickPosition = (int) v.getTag();
                builder.setTitle("Are you sure want to delete?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                                MainActivity.url + "/" + arrayList.get(clickPosition).getId(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        arrayList.remove(clickPosition);
                                        notifyDataSetChanged();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(stringRequest);
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
                break;
            }
            case R.id.btn_Edit: {
                Intent intent = new Intent(v.getContext(),UpdateJobsActivity.class);
                intent.putExtra("url",MainActivity.url);
                int clickPosition = (int) v.getTag();
                intent.putExtra("job",arrayList.get(clickPosition));
                ((Activity)context).startActivityForResult(intent,MainActivity.REQUESTCODEEDIT);
                break;
            }
        }
    }

    public class ViewHolder {
        TextView tvProjectName, tvStartDate, tvFinishDate;
        Button btnEdit, btnDelete;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_custom_listview, null);

            holder = new ViewHolder();
            holder.tvProjectName = (TextView) convertView.findViewById(R.id.tv_ProjectName);
            holder.tvStartDate = (TextView) convertView.findViewById(R.id.tv_StartDate);
            holder.tvFinishDate = (TextView) convertView.findViewById(R.id.tv_FinishDate);

            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_Delete);
            holder.btnEdit = (Button) convertView.findViewById(R.id.btn_Edit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = sdf.format(arrayList.get(position).getStartDate());
        String finishDate = sdf.format(arrayList.get(position).getFinishDate());

        holder.btnDelete.setTag(position);
        holder.btnEdit.setTag(position);

        holder.tvStartDate.setText(startDate);
        holder.tvFinishDate.setText(finishDate);
        holder.tvProjectName.setText(arrayList.get(position).getProjectName().toString());

        holder.btnDelete.setOnClickListener(this);
        holder.btnEdit.setOnClickListener(this);


        return convertView;    }
}
