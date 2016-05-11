package com.imagingtz.yeskirana.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sked.core.model.AddressList;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.java.AdapterCheckedChangeListener;

import java.util.List;


public class PickAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private List<AddressList> addressList;
    private com.imagingtz.yeskirana.adapter.AdapterClickListener adapterClickListener;
    private AdapterCheckedChangeListener checkedChangeListener;

    //private int quantity = 1;
    public PickAddressAdapter(Context context, List<AddressList> list) {
        this.context = context;
        this.addressList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pick_address,
                viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        return new CategoryViewHolder(view, adapterClickListener, checkedChangeListener);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder ViewHolder, int position) {
        AddressList address = addressList.get(position);
        if (ViewHolder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) ViewHolder).address1.setText(address.getAddrees1());
            ((CategoryViewHolder) ViewHolder).address2.setText(address.getAddress2());
            //  ((CategoryViewHolder) ViewHolder).pincode.setText(address.getPincode());
        }

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void setOnItemClickListener(com.imagingtz.yeskirana.adapter.AdapterClickListener adapterClickListener) {
        this.adapterClickListener = adapterClickListener;
    }

    public void setOnCheckedChangeListener(AdapterCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView address1, address2, pincode;
        RadioButton selectAddress;

        public CategoryViewHolder(View view, final com.imagingtz.yeskirana.adapter.AdapterClickListener
                adapterClickListener, final AdapterCheckedChangeListener checkedChangeListener) {
            super(view);
            address1 = (TextView) view.findViewById(R.id.tvAddress1);
            address2 = (TextView) view.findViewById(R.id.tvAddress2);
            pincode = (TextView) view.findViewById(R.id.pincode);
            selectAddress = (RadioButton) view.findViewById(R.id.rbButton);
            selectAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checkedChangeListener.OnCheckedChange(isChecked, getAdapterPosition());
                }
            });
            /*image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterClickListener.onClick(v, getAdapterPosition());
                }
            });*/
        }
    }


}
