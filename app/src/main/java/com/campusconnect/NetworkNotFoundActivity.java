package com.campusconnect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;

public class NetworkNotFoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_not_found);
        NetworkNotFoundDialog getdetailsDialog = new NetworkNotFoundDialog(this);
        Window window = getdetailsDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getdetailsDialog.show();
    }

    public class NetworkNotFoundDialog extends Dialog {
        public Activity c;
        public Dialog d;
        Context context;


        public NetworkNotFoundDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_network_down);
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            c.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(NetworkNotFoundActivity.this,HomeActivity2.class);
        home.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(home);
        finish();
    }
}
