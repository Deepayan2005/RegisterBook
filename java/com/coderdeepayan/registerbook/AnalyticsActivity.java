package com.coderdeepayan.registerbook;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AnalyticsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<DataRecord> dataRecordList = LoadingActivity.dataRecordList;
    List<Analytics> analyticsList;
    AnalyticsAdapter analyticsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        
        analyticsList = new ArrayList<>();
        recyclerView = findViewById(R.id.analyticsRecyclerview);
        analyticsAdapter = new AnalyticsAdapter(analyticsList);
        recyclerView.setAdapter(analyticsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));

        String tempIds = "",tempNames = "";
        for (int i = 0; i <dataRecordList.size() ; i++) {
            if (tempIds.trim().isEmpty()){
                tempIds = tempIds+(dataRecordList.get(i).getId());
                tempNames = tempNames+(dataRecordList.get(i).getCreditor());
            }
            else {
                if (!tempIds.contains(dataRecordList.get(i).getId())){
                    tempIds= tempIds+"%"+(dataRecordList.get(i).getId());
                    tempNames= tempNames+"%"+(dataRecordList.get(i).getCreditor());
                }
            }
        }

        String[] ids = tempIds.split("%");
        String[] names = tempNames.split("%");

        for (int i = 0; i <ids.length ; i++) {
            int finalI = i;
            String creditorName = names[finalI];
            List<DataRecord> records2 = dataRecordList.stream().filter(new Predicate<DataRecord>() {
                @Override
                public boolean test(DataRecord dataRecord) {
                    return ids[finalI].equalsIgnoreCase(dataRecord.getId());
                }
            }).toList();
            double totalAmount = 0,givenAmount = 0,remainingAmount=0,givenPercentage=0;
            for (DataRecord dataRec:records2) {
                if (dataRec.getAmount()>0){
                    totalAmount+=dataRec.getAmount();
                }
                else {
                    givenAmount+=Math.abs(dataRec.getAmount());
                }
            }
            remainingAmount = totalAmount-givenAmount;
            givenPercentage = (remainingAmount/totalAmount)*100;
            analyticsList.add(new Analytics(creditorName,
                    String.valueOf(totalAmount).replace(".0","")
                    ,String.valueOf(givenAmount).replace(".0","")
                    ,String.valueOf(remainingAmount).replace(".0","")
                    ,formatPercentage(givenPercentage)));
            Log.d("Percent", creditorName+" ~ "+givenPercentage);
        }
        analyticsAdapter.notifyDataSetChanged();

    }

    private String formatPercentage(double givenPercentage) {
        String val = String.valueOf(givenPercentage).replace(".0","");
        int i = val.indexOf(".");
        if (i>0){
            return val.substring(0,i+3);
        }
        return val;
    }
}