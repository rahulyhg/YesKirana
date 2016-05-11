package com.imagingtz.yeskirana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sked.core.model.AddressList;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.adapter.AdapterClickListener;
import com.imagingtz.yeskirana.adapter.PickAddressAdapter;
import com.imagingtz.yeskirana.java.AdapterCheckedChangeListener;
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickAnAddress extends AppCompatActivity implements View.OnClickListener {
    private Button addNewAddress;
    private Button next;
    private RecyclerView addressList;
    private PickAddressAdapter adapter;
    private List<AddressList> addressListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_an_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addNewAddress = (Button) findViewById(R.id.addNewAddress);
        next = (Button) findViewById(R.id.next);
        addressList = (RecyclerView) findViewById(R.id.addressList);
        next.setOnClickListener(this);
        addressList.setLayoutManager(new LinearLayoutManager(this));
        addressListItem = new ArrayList<>();
        adapter = new PickAddressAdapter(this, addressListItem);
        adapter.setOnItemClickListener(new AdapterClickListener() {
            @Override
            public void onClick(View v, int position) {
            }
        });
        adapter.setOnCheckedChangeListener(new AdapterCheckedChangeListener() {
            @Override
            public void OnCheckedChange(boolean flag, int position) {
                if (flag) {
                    for (int i = 0; i < addressListItem.size(); i++) {
                        if (i == position)
                            addressListItem.get(position).setIsChecked(true);
                        else
                            addressListItem.get(position).setIsChecked(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        addressList.setAdapter(adapter);
        syncAddressList();
        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickAnAddress.this, AddAddress.class));
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

    private void syncAddressList() {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "address_fetcher.php",
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
                params.put("customer_id", Utils.getUserId(PickAnAddress.this));
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
                AddressList addressList = new AddressList();
                addressList.setAddrees1(jsonObject.getString("address_1"));
                addressList.setAddress2(jsonObject.getString("address_2"));
                addressList.setFirstName(jsonObject.getString("firstname"));
                addressList.setLastName(jsonObject.getString("lastname"));
                addressList.setCompany(jsonObject.getString("company"));
                addressListItem.add(addressList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                AddressList address = null;
                for (int i = 0; i < addressListItem.size(); i++) {
                    if (addressListItem.get(i).isChecked())
                        address = addressListItem.get(i);
                }
                startActivity(new Intent(PickAnAddress.this, PlaceOrder.class)
                        .putExtra("address", address)
                        .putExtra("itemIds", getIntent().getStringExtra("itemIds")));
                break;
        }
    }
}
