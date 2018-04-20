package com.example.k.bankapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

    public static final String TAG = "TransactionListAdapter";

    private Context mContext;
    private int mResource;
    private User mUser;

    public TransactionListAdapter(Context context, int resource, ArrayList<Transaction> objects, User user) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mUser = user;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String id = getItem(position).getId();
        String date = getItem(position).getDate();
        int sender = getItem(position).getSender();
        int receiver = getItem(position).getReceiver();
        double amount = getItem(position).getAmount();

        Transaction transaction = new Transaction(id, date, sender, receiver, amount);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDate = (TextView)convertView.findViewById(R.id.textView1);
        TextView tvSender = (TextView)convertView.findViewById(R.id.textView2);
        TextView tvReceiver = (TextView)convertView.findViewById(R.id.textView3);
        TextView tvAmount = (TextView)convertView.findViewById(R.id.textView4);

        tvDate.setText(date.substring(0, 10));
        if(Integer.parseInt(mUser.getAccounts().get(0).getId()) == sender){
            tvSender.setText("You");
        } else {
            tvSender.setText(Integer.toString(sender));
        }
        if(Integer.parseInt(mUser.getAccounts().get(0).getId()) == receiver){
            tvReceiver.setText("You");
        } else {
            tvReceiver.setText(Integer.toString(receiver));
        }
        tvAmount.setText(Double.toString(amount));

        return convertView;
    }
}

