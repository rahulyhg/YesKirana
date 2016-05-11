package com.imagingtz.yeskirana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sked.core.model.Product;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.adapter.AdapterClickListener;
import com.imagingtz.yeskirana.adapter.MyBasketAdapter;
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyBasketActivity extends AppCompatActivity {

    private RecyclerView basketList;
    private List<Product> productItem;
    private MyBasketAdapter adapter;
    private Button checkout;
    private TextView prize;
    double total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkout = (Button) findViewById(R.id.checkout);
        basketList = (RecyclerView) findViewById(R.id.basketList);
        prize=(TextView)findViewById(R.id.tvPrice);
        basketList.setLayoutManager(new LinearLayoutManager(this));

        productItem = new ArrayList<>();
        adapter = new MyBasketAdapter(this, productItem);
        adapter.setOnItemClickListener(new AdapterClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
        basketList.setAdapter(adapter);
        syncProductList();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String cartId = "";
                for (int i = 0; i < productItem.size(); i++) {
                    cartId = cartId.concat(productItem.get(i).getCart_id());
                    if(i != productItem.size())
                        cartId = cartId.concat(",");
                }
               /* String itemIds = "";
                for (int i = 0; i < productItem.size(); i++) {
                    itemIds = itemIds.concat(productItem.get(i).getItemId());
                    if(i != productItem.size())
                        itemIds = itemIds.concat(",");
                }*/
                startActivity(new Intent(getApplicationContext(), PickAnAddress.class)
                .putExtra("itemIds", cartId));
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.basket, menu);
        return true;
    }

    private void syncProductList() {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "my_cart.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        Utils.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            getProductList(jsonArray);
                            adapter.notifyDataSetChanged();
                         //   Toast.makeText(getApplicationContext(), "Total number of Items are:" + basketList.getAdapter().getItemCount(), Toast.LENGTH_LONG).show();
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
                params.put("customer_id", Utils.getUserId(MyBasketActivity.this));
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
                Product product = new Product();
                product.setCart_id(jsonObject.getString("cart_id"));
                product.setItemId(jsonObject.getString("product_id"));
                product.setItemName(jsonObject.getString("name"));
                product.setItemImage(jsonObject.getString("image"));
                product.setWeight(jsonObject.getString("model"));
                product.setPrize(jsonObject.getString("price"));
               // product.setNewPrize(jsonObject.getString("price"));
                product.setNumber(jsonObject.getInt("quantity"));
                productItem.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
