package com.imagingtz.yeskirana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddress extends AppCompatActivity {
    private EditText state;
    private EditText pincode;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText mobile;
    private EditText address1;
    private EditText address2;
    private EditText company;
    private Button saveAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setReference();

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(AddAddress.this) && Utils.validateField(email) && Utils.validateField(firstName)
                        && Utils.validateField(lastName) && Utils.validateField(mobile) && Utils.validateField(address1)
                        && Utils.validateField(address2) && Utils.validateField(pincode) && Utils.validateField(company)) {
                    addAddress();
                }
            }
        });
    }

    public void setReference() {
        state = (EditText) findViewById(R.id.state);
        email = (EditText) findViewById(R.id.email);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        mobile = (EditText) findViewById(R.id.mobile);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        company = (EditText) findViewById(R.id.company);
        pincode = (EditText) findViewById(R.id.pincode);
        saveAddress = (Button) findViewById(R.id.saveAdress);
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

    private void addAddress() {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "address_update.php",
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
                                        startActivity(new Intent(getApplicationContext(), PickAnAddress.class));
                                       /* Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);*/
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
               /* params.put("password", pincode.getText().toString());
                params.put("password", pincode.getText().toString());*/
                params.put("customer_id", Utils.getUserId(AddAddress.this));
                params.put("payment_postcode'", pincode.getText().toString());
                params.put("firstname", firstName.getText().toString());
                params.put("lastname", lastName.getText().toString());
                //    params.put("telephone", mobile.getText().toString());
                params.put("payment_address_1", address1.getText().toString());
                params.put("payment_address_2", address2.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

}
