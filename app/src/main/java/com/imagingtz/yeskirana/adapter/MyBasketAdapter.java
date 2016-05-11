package com.imagingtz.yeskirana.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.imagingtz.yeskirana.database.DBOHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by Mishra on 1/4/2016.
 */
public class MyBasketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private List<Product> productList;
    private AdapterClickListener adapterClickListener;
    private int quantity = 1;

    public MyBasketAdapter(Context context, List<Product> list) {
        productList = list;
        this.context = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewgroup, int i) {
        context = viewgroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.basket_list,
                viewgroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        return new CategoryViewHolder(view, adapterClickListener);
    }

    public void setOnItemClickListener(AdapterClickListener adapterClickListener) {
        this.adapterClickListener = adapterClickListener;
    }

    public int getItemCount() {
        return productList.size();
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final Product product = productList.get(i);
        if (viewHolder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) viewHolder).itemName.setText(product.getItemName());
            ((CategoryViewHolder) viewHolder).weight.setText(product.getWeight());
            ((CategoryViewHolder) viewHolder).newPrize.setText(product.getPrize() + "");
            ((CategoryViewHolder) viewHolder).newPrize.setText(product.getNewPrize());
            ((CategoryViewHolder) viewHolder).number.setText(product.getNumber() + "");
            com.imagingtz.yeskirana.java.Utils.setImageWithProgress(context, ((CategoryViewHolder) viewHolder).image, product.getItemImage(),
                    ((CategoryViewHolder) viewHolder).progressBar);
           /* ((CategoryViewHolder) viewHolder).addTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CategoryViewHolder) viewHolder).addTo.setVisibility(View.GONE);
                    addToProduct(product.getItemId(), quantity + "");
                    insertProduct(product.getItemId(), quantity + "");
                    ((CategoryViewHolder) viewHolder).layout.setVisibility(View.VISIBLE);
                }
            });*/
            ((CategoryViewHolder) viewHolder).add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // quantity++;
                    product.setNumber(product.getNumber() + 1);
                    ((CategoryViewHolder) viewHolder).number.setText(product.getNumber() + "");
                    productUpdate(product.getItemId(), product.getNumber() + "");
                    // updateProduct(product.getItemId(), quantity + "");
                }
            });
            ((CategoryViewHolder) viewHolder).subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (product.getNumber() <= 1) {
                        product.setNumber(product.getNumber() - 1);
                        ((CategoryViewHolder) viewHolder).layout.setVisibility(View.GONE);
                        //((CategoryViewHolder) viewHolder).addTo.setVisibility(View.VISIBLE);
                        productUpdate(product.getItemId(), "0");
                        new DBOHelper().delete(context, com.imagingtz.yeskirana.database.Table.Cart.CART_TABLE,
                                com.imagingtz.yeskirana.database.Table.Cart.PRODUCT_ID + " = " + product.getItemId());
                    }
                    if (product.getNumber() > 1) {
                        product.setNumber(product.getNumber() - 1);
                        ((CategoryViewHolder) viewHolder).number.setText(product.getNumber() + "");
                        productUpdate(product.getItemId(), product.getNumber() + "");
                        //updateProduct(product.getItemId(), product.getNumber() + "");
                    }

                  /*  if (quantity <= 1) {
                        ((CategoryViewHolder) viewHolder).layout.setVisibility(View.GONE);
                        //  ((CategoryViewHolder) viewHolder).addTo.setVisibility(View.VISIBLE);
                        productUpdate(product.getItemId(), "0");
                        new com.uks.yeskirana.database.DBOHelper().delete(context, com.uks.yeskirana.database.Table.Cart.CART_TABLE,
                                com.uks.yeskirana.database.Table.Cart.PRODUCT_ID + " = " + product.getItemId());
                    }
                    if (quantity > 1) {
                        quantity--;
                        ((CategoryViewHolder) viewHolder).number.setText(product.getNumber() + "");
                        productUpdate(product.getItemId(), quantity + "");
                        updateProduct(product.getItemId(), quantity + "");
                    }*/

                }
            });

            ((CategoryViewHolder) viewHolder).delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDelete(product.getCart_id(), i);
                }
            });
        }
    }

    public void insertProduct(String productId, String quantity) {
        ContentValues values = new ContentValues();
        values.put(com.imagingtz.yeskirana.database.Table.Cart.PRODUCT_ID, productId);
        values.put(com.imagingtz.yeskirana.database.Table.Cart.QUANTITY, quantity);

        new com.imagingtz.yeskirana.database.DBOHelper().insert(context, com.imagingtz.yeskirana.database.Table.Cart.CART_TABLE, values);
    }

    public void updateProduct(String productId, String quantity) {
        ContentValues values = new ContentValues();
        values.put(com.imagingtz.yeskirana.database.Table.Cart.QUANTITY, quantity);

        new com.imagingtz.yeskirana.database.DBOHelper().update(context, com.imagingtz.yeskirana.database.Table.Cart.CART_TABLE, values, com.imagingtz.yeskirana.database.Table.Cart.PRODUCT_ID
                + " = " + productId);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, weight, newPrize,prize;
        ImageView image;
        ProgressBar progressBar;
        LinearLayout layout;
        ImageButton add, subtract, delete;
        TextView number;


        public CategoryViewHolder(View view, final AdapterClickListener adapterClickListener) {
            super(view);
            image = (ImageView) view.findViewById(R.id.product_image);
            itemName = (TextView) view.findViewById(R.id.itemName);
            weight = (TextView) view.findViewById(R.id.weight);
          //  prize = (TextView) view.findViewById(R.id.tvOldPrize);
            newPrize = (TextView) view.findViewById(R.id.tvNewPrize);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            // addTo = (TextView) view.findViewById(R.id.addTo);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            add = (ImageButton) view.findViewById(R.id.add);
            subtract = (ImageButton) view.findViewById(R.id.subtract);
            delete = (ImageButton) view.findViewById(R.id.delete);
            number = (TextView) view.findViewById(R.id.number);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterClickListener.onClick(v, getAdapterPosition());
                }
            });
        }
    }

    private void productUpdate(final String itemId, final String quantity) {
        com.imagingtz.yeskirana.java.Utils.show(context);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                com.imagingtz.yeskirana.java.Synchronize.BASE_URL + "cart_update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        com.imagingtz.yeskirana.java.Utils.dismiss();
                        /*try {

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.toString());
                com.imagingtz.yeskirana.java.Utils.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", itemId);
                params.put("quantity", quantity);
                params.put("session_id", "4iq2f6li7nr84mvh1q9o7cv9r0");
                params.put("customer_id", com.imagingtz.yeskirana.java.Utils.getUserId(context));
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

    private void productDelete(final String cartId, final int i) {
        com.imagingtz.yeskirana.java.Utils.show(context);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                com.imagingtz.yeskirana.java.Synchronize.BASE_URL + "cart_delete.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
                        com.imagingtz.yeskirana.java.Utils.dismiss();
                        productList.remove(i);
                        notifyItemRemoved(i);
                        // notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.toString());
                com.imagingtz.yeskirana.java.Utils.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cart_id", cartId);
                params.put("session_id", "4iq2f6li7nr84mvh1q9o7cv9r0");
                params.put("customer_id", com.imagingtz.yeskirana.java.Utils.getUserId(context));
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

}
