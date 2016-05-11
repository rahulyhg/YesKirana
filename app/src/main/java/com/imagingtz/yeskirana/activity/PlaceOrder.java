package com.imagingtz.yeskirana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sked.core.model.AddressList;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity implements View.OnClickListener {
    private Button placeOrder;
    private RadioGroup paymentMethod;
    private RadioButton cashOnDelivery;
    private RadioButton payMoney;
    private String payType;
    private AddressList address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        address = (AddressList) getIntent().getSerializableExtra("address");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        paymentMethod = (RadioGroup) findViewById(R.id.paymentMethod);
        cashOnDelivery = (RadioButton) findViewById(R.id.cashOnDelvery);
        payMoney = (RadioButton) findViewById(R.id.payMoney);
        placeOrder = (Button) findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(this);
        paymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cashOnDelvery) {
                    payType = "cod";
                } else if (checkedId == R.id.payMoney) {
                    payType = "payU";
                }
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

    private void placeOrder() {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "order_updation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        Utils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("status")) {
                                try {
                                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                        startActivity(new Intent(getApplicationContext(), ThankYou.class));
                                       // Toast.makeText(com.uks.yeskirana.activity.LoginActivity.this, "successfully logged in.", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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
                params.put("customer_id", Utils.getUserId(PlaceOrder.this));
                params.put("payment_method", payType);
                params.put("firstname", address.getFirstName());
                params.put("lastname", address.getFirstName());
                params.put("email", "r@gmail.com");
                params.put("telephone", "8130858755");
                params.put("shipping_firstname", address.getFirstName());
                params.put("shipping_lastname", address.getLastName());
                params.put("shipping_company", address.getCompany());
                params.put("shipping_address_1", address.getAddrees1());
                params.put("shipping_address_2", address.getAddress2());
                params.put("shipping_postcode", "110019");
                params.put("payment_firstname", address.getFirstName());
                params.put("payment_lastname", address.getLastName());
                params.put("payment_company", address.getCompany());
                params.put("payment_address_1", address.getAddrees1());
                params.put("payment_address_2", address.getAddress2());
                Log.d("Address", address.getAddress2());
                params.put("payment_postcode", "110019");
                params.put("total", "1200");
                params.put("cart_id", getIntent().getStringExtra("itemIds"));
                //params.put("password", password.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.placeOrder:
                placeOrder();
                break;
        }
    }
}
