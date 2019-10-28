package com.example.bloodbank.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BloodBankValueHolder;
import com.example.bloodbank.requestBlood.BloodRequestValueModel;
import com.google.firebase.database.DataSnapshot;

public class BloodBankViewHolder extends RecyclerView.ViewHolder{
    TextView name,address,contact;
    public BloodBankViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.blood_bank_name);
        address=itemView.findViewById(R.id.blood_bank_address);
        contact=itemView.findViewById(R.id.blood_bank_contact);

    }
    public void setView(DataSnapshot dataSnapshot){
        BloodBankValueHolder bloodBankValueHolder=dataSnapshot.getValue(BloodBankValueHolder.class);

        address.setText(bloodBankValueHolder.getAddress());
        name.setText(bloodBankValueHolder.getName());
        contact.setText(bloodBankValueHolder.getContact());
    }
}
