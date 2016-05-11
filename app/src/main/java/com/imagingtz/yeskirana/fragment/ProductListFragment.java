package com.imagingtz.yeskirana.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.adapter.ViewPagerAdapter;
import com.imagingtz.yeskirana.java.Utils;
import com.imagingtz.yeskirana.tabs.SlidingTabLayout;


public class ProductListFragment extends Fragment {

    private static final String ARG_PARAM = "param";

    private OnFragmentInteractionListener mListener;
    private SlidingTabLayout tabs;
    private ViewPager viewPager;

    // TODO: Rename and change types and number of parameters
    public static com.imagingtz.yeskirana.fragment.ProductListFragment newInstance(int position) {
        ProductListFragment fragment = new ProductListFragment();
        Utils.categoryId = position + "";
        return fragment;
    }

    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));
        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        tabs.setCustomTabView(R.layout.cst_tabs, R.id.tvTab);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getActivity().getResources().getColor(R.color.colorAccent);
            }

            @Override
            public int getDividerColor(int position) {
                return getActivity().getResources().getColor(R.color.colorAccent);
            }
        });
        tabs.setViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}
