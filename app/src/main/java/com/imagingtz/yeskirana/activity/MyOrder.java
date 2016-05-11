package com.imagingtz.yeskirana.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sked.core.model.MyOrderModel;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.adapter.AdapterClickListener;
import com.imagingtz.yeskirana.adapter.MyOrderAdapter;
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrder extends AppCompatActivity {
    private RecyclerView orderList;
    private List<MyOrderModel> order_list;
    private MyOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderList = (RecyclerView) findViewById(R.id.orderList);
        orderList.setLayoutManager(new LinearLayoutManager(this));
        order_list = new ArrayList<>();
        adapter = new MyOrderAdapter(this, order_list);
        adapter.setOnItemClickListener(new AdapterClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
        orderList.setAdapter(adapter);
        syncOrderList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void syncOrderList() {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "my_order.php",
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
                params.put("customer_id", Utils.getUserId(MyOrder.this));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }


    private void getProductList(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MyOrderModel order = new MyOrderModel();
                order.setOrderId(jsonObject.getString("order_id"));
                order.setDate(jsonObject.getString("date_added"));
                order.setPrice(jsonObject.getString("total"));
                order.setStatus(jsonObject.getString("get_order_status"));
                order_list.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
