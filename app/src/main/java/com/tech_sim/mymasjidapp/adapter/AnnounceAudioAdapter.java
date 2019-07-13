package com.tech_sim.mymasjidapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
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
import com.tech_sim.mymasjidapp.activity.Announcement;
import com.tech_sim.mymasjidapp.activity.AnnouncementDetail;
import com.tech_sim.mymasjidapp.model.ViewAudioModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnounceAudioAdapter extends  RecyclerView.Adapter<AnnounceAudioAdapter.ViewHolder> {

    private ArrayList<ViewAudioModel> viewAudioModels;
    private LayoutInflater mInflater;
    private Context context;
    public AnnounceAudioAdapter(Context context, ArrayList<ViewAudioModel> audioModels) {
        this.mInflater = LayoutInflater.from(context);
        this.viewAudioModels=audioModels;
        this.context=context;
    }
    @NonNull
    @Override
    public AnnounceAudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_audio_info, viewGroup, false);
        return new AnnounceAudioAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final AnnounceAudioAdapter.ViewHolder holder, final int position) {
        final ViewAudioModel audioes=viewAudioModels.get(position);
            holder.a_type.setText(audioes.getA_type());
            holder.a_date.setText(audioes.getA_date());
            if(audioes.getA_type().equals("Youtube"))
            {
                holder.a_des.setText("Masjid is live");

            }
            else {
                holder.a_des.setText(audioes.getA_description());
            }


        holder.card_announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(audioes.getA_type().equals("Youtube"))
                    {
                        try {
                            if (!URLUtil.isValidUrl(audioes.getA_description())) {
                                Toast.makeText(context, " This is not a valid link", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(audioes.getA_description()));
                                context.startActivity(intent);
                            }
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Intent intent = new Intent(context, AnnouncementDetail.class);
                        intent.putExtra("type", audioes.getA_type());
                        intent.putExtra("description", audioes.getA_description());
                        intent.putExtra("date", audioes.getA_date());
                        intent.putExtra("audio", audioes.getA_file());
                        context.startActivity(intent);
                    }
                }


        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return viewAudioModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView a_type,a_des,a_date;
        ImageView imageView;
        CardView card_announce;

        ViewHolder(View itemView) {
            super(itemView);
            a_type=itemView.findViewById(R.id.type);
            a_des=itemView.findViewById(R.id.decription);
            imageView=itemView.findViewById(R.id.arrow_image);
            a_date=itemView.findViewById(R.id.date);
            card_announce=itemView.findViewById(R.id.announced);
        }

    }

}
