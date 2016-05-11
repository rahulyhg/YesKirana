package com.imagingtz.yeskirana.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sked.core.model.Product;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.activity.MyBasketActivity;
import com.imagingtz.yeskirana.activity.ProductListDetails;
import com.imagingtz.yeskirana.adapter.AdapterClickListener;
import com.imagingtz.yeskirana.adapter.ProductListAdapter;
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {
    private static final String ARG_PARAM = "param";
    private RecyclerView productList;
    private List<Product> productItem;
    private ProductListAdapter adapter;

    public static com.imagingtz.yeskirana.fragment.ProductFragment newInstance(int position) {
        com.imagingtz.yeskirana.fragment.ProductFragment fragment = new com.imagingtz.yeskirana.fragment.ProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        productList = (RecyclerView) rootView.findViewById(R.id.productList);
        productList.setLayoutManager(new LinearLayoutManager(getActivity()));
        productItem = new ArrayList<>();

        if (getArguments().getInt(ARG_PARAM) == 0)
            syncProductList();
        if (getArguments().getInt(ARG_PARAM) == 1) {

        }
        //syncProductList();
        adapter = new ProductListAdapter(getActivity(), productItem);
        productList.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterClickListener() {
            @Override
            public void onClick(View v, int position) {
                startActivity(new Intent(getActivity(), ProductListDetails.class)
                        .putExtra("id", productItem.get(position).getItemId()));
            }
        });

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyBasketActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }

    private void syncProductList() {
        Utils.show(getActivity());
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "products_by_category.php",
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
                params.put("cate_id", Utils.categoryId);
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
      /*  try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product();
                product.setItemId(jsonObject.getString("product_id"));
                product.setItemName(jsonObject.getString("product_name"));
                product.setItemImage(jsonObject.getString("product_image"));
                // product.setWeight(jsonObject.getString("course_name"));
                product.setPrize(jsonObject.getString("newPrice"));
                productItem.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

   
}
