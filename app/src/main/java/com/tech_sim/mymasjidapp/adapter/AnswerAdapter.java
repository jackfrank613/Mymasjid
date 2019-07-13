package com.tech_sim.mymasjidapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.activity.MasjidAnswer;
import com.tech_sim.mymasjidapp.activity.QuetionDetailActivity;
import com.tech_sim.mymasjidapp.model.MasjidAnswerModel;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnswerAdapter extends  RecyclerView.Adapter<AnswerAdapter.ViewHolder>{
    private ArrayList<MasjidAnswerModel> viewAudioModels;
    private LayoutInflater mInflater;
    private Context context;
    public AnswerAdapter(Context context, ArrayList<MasjidAnswerModel> audioModels) {
        this.mInflater = LayoutInflater.from(context);
        this.viewAudioModels=audioModels;
        this.context=context;
    }
    @NonNull
    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_audio_info, viewGroup, false);
        return new AnswerAdapter.ViewHolder(view);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final AnswerAdapter.ViewHolder holder, final int position) {
       final  MasjidAnswerModel masjidAnswerModel=viewAudioModels.get(position);
       if(masjidAnswerModel.getStatus().equals("1"))
       {
           holder.a_date.setText(masjidAnswerModel.getDate());
           holder.a_des.setText(masjidAnswerModel.getAnswer());
           holder.a_type.setText(masjidAnswerModel.getMasjid_name());
       }
       else {
           holder.card.setBackgroundResource(R.drawable.backgroundcolor);
           holder.a_date.setText(masjidAnswerModel.getDate());
           holder.a_date.setTextColor(Color.WHITE);
           holder.a_des.setText(masjidAnswerModel.getAnswer());
           holder.a_des.setTextColor(Color.WHITE);
           holder.a_type.setText(masjidAnswerModel.getMasjid_name());
           holder.a_type.setTextColor(Color.WHITE);
       }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(masjidAnswerModel.getAn_answer().equals(""))
                {
                    Toast.makeText(context,"There is no answer ,Please waiting...",Toast.LENGTH_SHORT).show();
                }
                else {

                     String answer=masjidAnswerModel.getAn_answer();
                     String masjid_name=masjidAnswerModel.getMasjid_name();
                     String date=masjidAnswerModel.getAn_date();
                     String audio=masjidAnswerModel.getAudio();
                     String state_id=masjidAnswerModel.getState_id();
                     sendRequest(state_id,answer,masjid_name,date,audio);

                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return viewAudioModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView a_type,a_des,a_date;
        ImageView imageView;
        CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            a_type=itemView.findViewById(R.id.type);
            a_des=itemView.findViewById(R.id.decription);
            card=itemView.findViewById(R.id.announced);
            a_date=itemView.findViewById(R.id.date);
        }

    }
    public void sendRequest(final String state_id, final String answer, final String masjid_name, final String date, final String audio)
    {
        String url="http://mymasjid.space/mobileApp/admin/answer_see_user";
        RequestQueue r = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PreferenceManager.setCount2(PreferenceManager.getCont2()-1);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false"))
                            {
                                Intent intent=new Intent(context, MasjidAnswer.class);
                                intent.putExtra("answer",answer);
                                intent.putExtra("masjidname",masjid_name);
                                intent.putExtra("date",date);
                                intent.putExtra("audio",audio);
                                context.startActivity(intent);

                            }

                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error",error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("answer", state_id);
                return params;
            }
        };
        r.add(stringRequest);
    }
    }
