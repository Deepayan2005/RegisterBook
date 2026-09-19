package com.coderdeepayan.registerbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoadingActivity extends AppCompatActivity {
    static List<DataRecord> dataRecordList;
    static Set<Creditor> creditorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GithubService service = new GithubService();
                    dataRecordList = service.getRecords();
                    creditorSet = service.getCreditorIdSet();
                    if (dataRecordList!=null){
                        startActivity(new Intent(LoadingActivity.this, PasswordActivity.class));
                        finish();
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    Log.e("Hospitals", "run: "+e);
                }

            }
        }).start();



    }
}