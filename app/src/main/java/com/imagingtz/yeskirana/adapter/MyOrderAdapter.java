package com.imagingtz.yeskirana.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sked.core.model.MyOrderModel;
import com.imagingtz.yeskirana.R;

import java.util.List;

/**
 * YesKirana, All rights Reserved
 * Created by Sanjeet on 11-Feb-16.
 */
public class MyOrderAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private List<MyOrderModel> orderList;
    private AdapterClickListener adapterClickListener;

    public MyOrderAdapter(Context context, List<MyOrderModel> list) {
        orderList = list;
        this.context = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewgroup, int i) {
        context = viewgroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.my_order_list,
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
        return orderList.size();
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final MyOrderModel order = orderList.get(i);
        if (viewHolder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) viewHolder).orderId.setText(order.getOrderId());
            ((CategoryViewHolder) viewHolder).date.setText(order.getDate());
            ((CategoryViewHolder) viewHolder).prize.setText(order.getPrice());
            ((CategoryViewHolder) viewHolder).status.setText(order.getStatus());

        }
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView date, orderId,prize,status;

        public CategoryViewHolder(View view, final AdapterClickListener adapterClickListener) {
            super(view);
            orderId = (TextView) view.findViewById(R.id.orderId);
            date = (TextView) view.findViewById(R.id.date);
            prize = (TextView) view.findViewById(R.id.price);
            status = (TextView) view.findViewById(R.id.status);
         /*   image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterClickListener.onClick(v, getAdapterPosition());
                }
            });*/
        }
    }

}
