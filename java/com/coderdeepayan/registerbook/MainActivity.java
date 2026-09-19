package com.coderdeepayan.registerbook;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DataAdapter dataAdapter;
    RecyclerView recyclerView;
    List<DataRecord> dataRecordList;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.allDataRecyclerView);
        dataRecordList = new ArrayList<>(LoadingActivity.dataRecordList);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode()==RESULT_OK){
                    Toast.makeText(MainActivity.this,
                            "Data Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent =new Intent(MainActivity.this, CreateRecordActivity.class);

        dataAdapter = new DataAdapter(this, dataRecordList, new AddButtonListener() {
            @Override
            public void want_to_add_Record() {
                intentActivityResultLauncher.launch(intent);
            }
        }, new AnalyticsListener() {
            @Override
            public void showAnalyticsLister() {
                startActivity(new Intent(MainActivity.this, AnalyticsActivity.class));
            }
        });
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        dataRecordList.add(new DataRecord(DataRecord.UTILITIES));
        dataAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(dataRecordList.size()-1);
    }

}