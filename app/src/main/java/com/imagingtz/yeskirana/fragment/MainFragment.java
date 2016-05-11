package com.imagingtz.yeskirana.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sked.core.model.Product;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.activity.TopCategories;
import com.imagingtz.yeskirana.adapter.ProductListAdapter;
import com.imagingtz.yeskirana.adapter.SliderAdapter;
import com.imagingtz.yeskirana.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class MainFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Integer[] IMAGES = {R.drawable.banner, R.drawable.banner1, R.drawable.banner2,
            R.drawable.banner3};
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private RecyclerView productList;
    private List<Product> productItem;
    private ProductListAdapter adapter;
    private AutoScrollViewPager viewPager;
    private ImageView banner;
    private TextView beverages;
    private TextView brandedFoods;
    private TextView personalCare;
    private TextView fruitsVegetables;
    private TextView groceryStaples;
    private TextView houseHold;
    private ArrayList<Integer> ImagesArray = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static com.imagingtz.yeskirana.fragment.MainFragment newInstance(int position) {
        com.imagingtz.yeskirana.fragment.MainFragment fragment = new com.imagingtz.yeskirana.fragment.MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        beverages = (TextView) rootView.findViewById(R.id.beverages);
        brandedFoods = (TextView) rootView.findViewById(R.id.brandedFoods);
        personalCare = (TextView) rootView.findViewById(R.id.personalCare);
        fruitsVegetables = (TextView) rootView.findViewById(R.id.fruitsVegetables);
        groceryStaples = (TextView) rootView.findViewById(R.id.groceryStaples);
        houseHold = (TextView) rootView.findViewById(R.id.houseHold);
        viewPager = (AutoScrollViewPager) rootView.findViewById(R.id.viewPager);
        // banner = (ImageView) rootView.findViewById(R.id.banner);
        beverages.setOnClickListener(this);
        brandedFoods.setOnClickListener(this);
        personalCare.setOnClickListener(this);
        fruitsVegetables.setOnClickListener(this);
        groceryStaples.setOnClickListener(this);
        houseHold.setOnClickListener(this);
        viewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));
        //   banner.setViewPager(viewPager);
        viewPager.setCycle(true);
        viewPager.startAutoScroll();
        init();

        return rootView;
    }

    @Override
    public void onPause() {
        viewPager.stopAutoScroll();
        super.onPause();
    }

    @Override
    public void onResume() {
        viewPager.startAutoScroll();
        super.onPause();
    }

    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);
        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new SliderAdapter(getActivity(), ImagesArray));
        // viewPager.setPageTransformer(true, new RotateUpTransformer());

      /*  CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);*/

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        //  indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
       /* indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.beverages:
                Intent intent=new Intent(getActivity(),TopCategories.class);
                intent.putExtra("categoryId", 26);
                startActivity(intent);
                break;
            case R.id.personalCare:
                Intent personal=new Intent(getActivity(),TopCategories.class);
                personal.putExtra("categoryId", 36);
                startActivity(personal);
                break;
            case R.id.brandedFoods:
                Intent branded=new Intent(getActivity(),TopCategories.class);
                branded.putExtra("categoryId", 12);
                startActivity(branded);
                break;
            case R.id.fruitsVegetables:
                Intent fruits=new Intent(getActivity(),TopCategories.class);
                fruits.putExtra("categoryId", 1);
                startActivity(fruits);
                break;
            case R.id.groceryStaples:
                Intent grocery=new Intent(getActivity(),TopCategories.class);
                grocery.putExtra("categoryId", 4);
                startActivity(grocery);
                break;
            case R.id.houseHold:
                Intent house=new Intent(getActivity(),TopCategories.class);
                house.putExtra("categoryId", 48);
                startActivity(house);
                break;
        }
    }

    /*private void syncProductList() {
        Utils.show(getActivity());
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "products_by_parent_id.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        Utils.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            getProductList(jsonArray);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.toString());
                Utils.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("parent_id", "4");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsObjRequest);
    }

    private void getProductList(JSONArray jsonArray) {
        productItem.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                productItem.add(new Product().getProductList(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
