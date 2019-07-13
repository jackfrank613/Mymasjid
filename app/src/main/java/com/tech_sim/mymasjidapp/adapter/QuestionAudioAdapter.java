package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.tech_sim.mymasjidapp.activity.ManageAnsQuestionPanel;
import com.tech_sim.mymasjidapp.activity.QuetionDetailActivity;
import com.tech_sim.mymasjidapp.model.UserQuestionModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionAudioAdapter extends RecyclerView.Adapter<QuestionAudioAdapter.ViewHolder> {
    private ArrayList<UserQuestionModel> viewAudioModels;
    private LayoutInflater mInflater;
    private Context context;
    public QuestionAudioAdapter(Context context, ArrayList<UserQuestionModel> audioModels) {
        this.mInflater = LayoutInflater.from(context);
        this.viewAudioModels=audioModels;
        this.context=context;
    }

    @NonNull
    @Override
    public QuestionAudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_audio_info, viewGroup, false);
        return new QuestionAudioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestionAudioAdapter.ViewHolder holder, final int position) {
        final UserQuestionModel audioes=viewAudioModels.get(position);
        String state=audioes.getStaus();
        final String question_id=audioes.getUser_id();
        if(audioes.getStaus().equals("1"))
        {
            holder.name.setText(audioes.getName());
            holder.a_date.setText(audioes.getDate());
            holder.question.setText(audioes.getQuestion());

        }
       else {
            holder.cardView.setBackgroundResource(R.drawable.backgroundcolor);
            holder.name.setText(audioes.getName());
            holder.name.setTextColor(Color.WHITE);
            holder.a_date.setText(audioes.getDate());
            holder.a_date.setTextColor(Color.WHITE);
            holder.question.setText(audioes.getQuestion());
            holder.question.setTextColor(Color.WHITE);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=audioes.getName();
                String date=audioes.getDate();
                String question=audioes.getQuestion();
                String audio=audioes.getAudio();
                String user=audioes.getUser_id();
                  sendRequest(question_id,name,date,question,audio,user);

            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return viewAudioModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,question,a_date;
        ImageView imageView;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.type);
            question=itemView.findViewById(R.id.decription);
            imageView=itemView.findViewById(R.id.arrow_image);
            a_date=itemView.findViewById(R.id.date);
            cardView=itemView.findViewById(R.id.announced);
        }

    }
    public void sendRequest(final String id, final String name, final String date, final String question, final String audio, final String user)
    {
        String url="http://mymasjid.space/mobileApp/admin/question_see_masjid";
        RequestQueue r = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PreferenceManager.setCount3(PreferenceManager.getCont3()-1);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false"))
                            {
                                Intent intent=new Intent(context, QuetionDetailActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("date",date);
                                intent.putExtra("question",question);
                                intent.putExtra("audio",audio);
                                intent.putExtra("user_id",user);
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
                params.put("question", id);
                return params;
            }
        };
        r.add(stringRequest);
    }
}
