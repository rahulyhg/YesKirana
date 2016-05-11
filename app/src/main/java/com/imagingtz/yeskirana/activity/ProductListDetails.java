package com.imagingtz.yeskirana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.imagingtz.yeskirana.java.Synchronize;
import com.imagingtz.yeskirana.java.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListDetails extends AppCompatActivity implements View.OnClickListener {
    private String itemId;
    private List<Product> productItem;
    private ImageView productImage;
    private TextView itemName;
    private TextView prize,newPrize;
    private TextView weight;
    private TextView description;
    private TextView addToCart;
    private ImageButton subtract;
    private ImageButton addQuantity;
    private TextView number;
    private ProgressBar progressBar;
    LinearLayout layout;
    private Product product;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemId = getIntent().getStringExtra("id");
        setReferensh();
        syncProductList();
        addToCart.setOnClickListener(this);
        addQuantity.setOnClickListener(this);
        subtract.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductListDetails.this, MyBasketActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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


    private void setReferensh() {
        layout = (LinearLayout) findViewById(R.id.layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        productImage = (ImageView) findViewById(R.id.productImage);
        itemName = (TextView) findViewById(R.id.itemName);
        prize = (TextView) findViewById(R.id.tvOldPrize);
        newPrize = (TextView) findViewById(R.id.tvNewPrize);
        weight = (TextView) findViewById(R.id.weight);
        description = (TextView) findViewById(R.id.productDetails);
        addToCart = (TextView) findViewById(R.id.addTo);
        number = (TextView) findViewById(R.id.number);
        addQuantity = (ImageButton) findViewById(R.id.add);
        subtract = (ImageButton) findViewById(R.id.subtract);
    }

    private void syncProductList() {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                Synchronize.BASE_URL + "products_by_id.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        Utils.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            product = new Product().getProductList(jsonArray.getJSONObject(0));
                            getProductList();
                            // adapter.notifyDataSetChanged();
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
                params.put("pro_id", itemId);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    private void getProductList() {
        itemName.setText(product.getItemName());
        //product.setItemImage(jsonObject.getString("product_image"));
        Utils.setImageWithProgress(this, productImage, product.getItemImage(), progressBar);
        prize.setText(product.getPrize());
        newPrize.setText(product.getNewPrize());
        number.setText(product.getNumber() + "");
        if (product.getNumber() > 0) {
            addToCart.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
            addToCart.setVisibility(View.VISIBLE);
        }
    }

    private void productUpdate(final String quantity) {
        Utils.show(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                com.imagingtz.yeskirana.java.Synchronize.BASE_URL + "cart_update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        Utils.dismiss();
                        /*try {

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
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
                params.put("product_id", itemId);
                params.put("quantity", quantity);
                params.put("session_id", "4iq2f6li7nr84mvh1q9o7cv9r0");
                params.put("customer_id", Utils.getUserId(ProductListDetails.this));
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addTo:
                addToCart.setVisibility(View.GONE);
                product.setNumber(product.getNumber() + 1);
                number.setText(product.getNumber() + "");
                productUpdate(product.getNumber() + "");
                layout.setVisibility(View.VISIBLE);
                break;
            case R.id.add:
                product.setNumber(product.getNumber() + 1);
                number.setText(product.getNumber() + "");
                productUpdate(product.getNumber() + "");
                break;
            case R.id.subtract:
                if (product.getNumber() <= 1) {
                    product.setNumber(product.getNumber() - 1);
                    layout.setVisibility(View.GONE);
                    addToCart.setVisibility(View.VISIBLE);
                    productUpdate("0");
                }
                if (product.getNumber() > 1) {
                    product.setNumber(product.getNumber() - 1);
                    number.setText(product.getNumber() + "");
                    productUpdate(product.getNumber() + "");
                }
                break;
        }
    }
}
