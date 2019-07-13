package com.tech_sim.mymasjidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.activity.AttendanceActivity;
import com.tech_sim.mymasjidapp.activity.ReportChildActivity;
import com.tech_sim.mymasjidapp.model.AttendanceDetailModel;
import com.tech_sim.mymasjidapp.model.ChildItem;
import com.tech_sim.mymasjidapp.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewChildrenAdapter extends  RecyclerView.Adapter<ViewChildrenAdapter.ViewHolder> {


    private ArrayList<ChildItem> viewAudioModels;
    private LayoutInflater mInflater;
    private Context context;
    private Constants constants;
    private ArrayList<AttendanceDetailModel> attendanceDetailModels;
    public ViewChildrenAdapter(Context context, ArrayList<ChildItem> audioModels) {
        this.mInflater = LayoutInflater.from(context);
        this.viewAudioModels=audioModels;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewChildrenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_audio_info, viewGroup, false);
        return new ViewChildrenAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewChildrenAdapter.ViewHolder viewHolder, int i) {

        final ChildItem childItem=viewAudioModels.get(i);
        viewHolder.a_date.setText(childItem.getDate());
        viewHolder.a_type.setText(childItem.getStudentName());
        viewHolder.a_des.setText(childItem.getFatherName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.child_id=childItem.getChildId();
                if(childItem.isCheck()) {
                    Intent intent = new Intent(context, ReportChildActivity.class);
                    intent.putExtra("name", childItem.getStudentName());
                    intent.putExtra("age", childItem.getStudentAge());
                    intent.putExtra("father", childItem.getFatherName());
                    intent.putExtra("contact", childItem.getFatherContectNO());
                    intent.putExtra("classdays", childItem.getClassDays());
                    intent.putExtra("pin_number",childItem.getPin());
                    context.startActivity(intent);
                }
                else {

                     Intent intent1=new Intent(context, AttendanceActivity.class);
                     intent1.putExtra("child",childItem.getChildId());
                     context.startActivity(intent1);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return viewAudioModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView a_type,a_des,a_date;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            a_type=itemView.findViewById(R.id.type);
            a_des=itemView.findViewById(R.id.decription);
            imageView=itemView.findViewById(R.id.arrow_image);
            a_date=itemView.findViewById(R.id.date);
        }

    }

}
