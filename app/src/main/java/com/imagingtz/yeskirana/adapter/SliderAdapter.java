package com.imagingtz.yeskirana.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.imagingtz.yeskirana.R;
import java.util.ArrayList;

/**
 * Created by Mishra on 1/24/2016.
 */
public class SliderAdapter extends PagerAdapter {
    private ArrayList<Integer> IMAGES;
    private Context context;
    private LayoutInflater mLayoutinflater;

    public SliderAdapter(Context context, ArrayList<Integer> IMAGES) {

        this.context = context;
        this.IMAGES = IMAGES;
        mLayoutinflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = mLayoutinflater.inflate(R.layout.slider_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView)imageLayout.findViewById(R.id.imageSlide);
        imageView.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
