package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.ChatsPojo;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.databinding.RecycleChattingItemLeftBinding;

import java.util.ArrayList;

public class CustomerSupportChatAdapter extends RecyclerView.Adapter<CustomerSupportChatAdapter.ViewHolder>  {

    Activity nActivity;
    private static final String TAG = "Chat_Adapter";
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    ArrayList<ChatsPojo> chatlist;
    String msg;

    public CustomerSupportChatAdapter(Activity nActivity,String message) {

        this.nActivity = nActivity;
        this.msg=message;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_chatting_item_right,
                    parent, false);
            return new CustomerSupportChatAdapter.ViewHolder(view);
        } else {

            View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_chatting_item_left,
                    parent, false);
            return new CustomerSupportChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextViewCustomFont tvMessage;
        TextViewCustomFont tvMessageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage=itemView.findViewById(R.id.tvMessage);
            tvMessageTime=itemView.findViewById(R.id.tvMessageTime);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (msg!=null) {//here comes the user id as sender id
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
