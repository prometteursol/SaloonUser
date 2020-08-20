package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.prometteur.saloonuser.Adapter.CustomerSupportChatAdapter;
import com.prometteur.saloonuser.Model.ChatsPojo;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.ActivityCustomerSupportBinding;
import com.prometteur.saloonuser.databinding.LayoutChatProcessBinding;

import java.util.ArrayList;
import java.util.Date;

public class ActivityCustomerSupport extends AppCompatActivity implements View.OnClickListener {
    ActivityCustomerSupportBinding customerSupportBinding;
    // LayoutChatProcessBinding chatProcessBinding;
    Activity nActivity;
    RecyclerView recycleChatting;
    ArrayList<ChatsPojo> chatlist;
    ChatsPojo chatsPojo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerSupportBinding = ActivityCustomerSupportBinding.inflate(getLayoutInflater());
        View view = customerSupportBinding.getRoot();
        setContentView(view);
        nActivity = this;
        recycleChatting=findViewById(R.id.recycleChatting);
        chatlist=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(nActivity);
        layoutManager.setStackFromEnd(true);
        recycleChatting.setLayoutManager(layoutManager);
        recycleChatting.setAdapter(new CustomerSupportChatAdapter(nActivity,
                customerSupportBinding.edtTypeMessage.getText().toString()));

        customerSupportBinding.ivBackArrowimg.setOnClickListener(this);
        customerSupportBinding.ivsendMessage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBackArrowimg:
                finish();
                break;
            case R.id.ivsendMessage:
                long dateTime=new Date().getTime();
                chatsPojo = new ChatsPojo();
                chatsPojo.setTime(dateTime);
                if (customerSupportBinding.edtTypeMessage.getText().toString().length()>0) {
                    chatsPojo.setMessage(customerSupportBinding.edtTypeMessage.getText().toString());
                }
                chatsPojo.setSenderid("123");
                chatsPojo.setReceiverid("456");
                chatlist.add(chatsPojo);
                customerSupportBinding.edtTypeMessage.setText("");
                break;
        }
    }
}
