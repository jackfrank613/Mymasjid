package com.tech_sim.mymasjidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.PagerModel;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {
    Context context;
    List<PagerModel> pagerArr;
    LayoutInflater inflater;

    public SliderPagerAdapter(Context context, List<PagerModel> pagerArr) {
        this.context = context;
        this.pagerArr = pagerArr;

        inflater = ((Activity) context).getLayoutInflater();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.activity_introduction, container, false);

        TextView description1 = view.findViewById(R.id.descriptionOne);
        TextView description2 = view.findViewById(R.id.descriptionTwo);
        TextView description3 = view.findViewById(R.id.descriptionThree);
        view.setTag(position);
        ((ViewPager) container).addView(view);
        PagerModel model = pagerArr.get(position);
        description1.setText(model.getDescriptionOne());
        description2.setText(model.getDescriptionTwo());
        description3.setText(model.getDescriptionThree());

        return view;
    }

    @Override
    public int getCount() {
        return pagerArr.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
