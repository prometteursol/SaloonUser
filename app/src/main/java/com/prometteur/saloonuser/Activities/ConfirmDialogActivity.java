package com.prometteur.saloonuser.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.menuPos;
import static com.prometteur.saloonuser.Utils.Utils.logout;


public class ConfirmDialogActivity extends AppCompatActivity {
    TextView tvMessage;
    Button btnOk,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_confirmation);
        tvMessage=findViewById(R.id.tvMessage);
        btnOk=findViewById(R.id.btnOk);
        btnCancel=findViewById(R.id.btnCancel);

        tvMessage.setText(""+getIntent().getStringExtra("confirmMsg"));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPos=0;
               /* Preferences.getClearPrefs(ConfirmDialogActivity.this);
                startActivity(new Intent(ConfirmDialogActivity.this, ActivityLogin.class));
                finishAffinity();*/
                logout(ConfirmDialogActivity.this);
            }
        });
    }


    public void closeDialog(View view) {
        finish();
    }



}
