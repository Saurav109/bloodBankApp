package com.example.bloodbank.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodbank.R;
import com.example.bloodbank.ViewHolder.BloodBankViewHolder;
import com.example.bloodbank.ViewHolder.BloodRequestViewHolder;
import com.example.bloodbank.ViewHolder.DonnerViewHolder;
import com.example.bloodbank.ViewHolder.ProfileViewHolder;
import com.google.firebase.database.DataSnapshot;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int BLOOD_REQUEST_ITEM_VIEW = 1;
    public static final int PROFILE_ITEM_VIEW = 2;
    public static final int BLOOD_BANK_ITEM_VIEW = 3;
    public static final int DONNER_ITEM_VIEW = 4;

    private CustomHashMap<String, DataSnapshot> dataSnapshotCustomHashMap;
    private int VIEW_MODEL;
    private Context context;

    public Adapter(Context context,CustomHashMap<String, DataSnapshot> dataSnapshotCustomHashMap, int VIEW_MODEL) {
        this.dataSnapshotCustomHashMap = dataSnapshotCustomHashMap;
        this.VIEW_MODEL = VIEW_MODEL;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        if (VIEW_MODEL == BLOOD_REQUEST_ITEM_VIEW) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blood_request_item_view, viewGroup, false);
            return new BloodRequestViewHolder(view);
        } else if (VIEW_MODEL == BLOOD_BANK_ITEM_VIEW) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blood_bank_item_view, viewGroup, false);
            return new BloodBankViewHolder(view);
        } else if (VIEW_MODEL == PROFILE_ITEM_VIEW) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.donner_item_view, viewGroup, false);
            return new ProfileViewHolder(view);
        } else if (VIEW_MODEL == DONNER_ITEM_VIEW){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.donner_item_view, viewGroup, false);
            return new DonnerViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);
        Log.e("Adapter", "onBindViewHolder: binding view to index :" + i + " key: " + dataSnapshotCustomHashMap.getValueByIndex(i));
        DataSnapshot valueModel = dataSnapshotCustomHashMap.getValueByIndex(i);

        if (viewHolder instanceof BloodRequestViewHolder) {
            ((BloodRequestViewHolder) viewHolder).setView(valueModel,context);
        }else if(viewHolder instanceof ProfileViewHolder){
            ((ProfileViewHolder) viewHolder).setView(valueModel);
        }else if(viewHolder instanceof BloodBankViewHolder){
            ((BloodBankViewHolder) viewHolder).setView(valueModel);
        }else if(viewHolder instanceof DonnerViewHolder){
            ((DonnerViewHolder) viewHolder).setView(valueModel,context);
        }
    }

    @Override
    public int getItemCount() {
        return dataSnapshotCustomHashMap.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
