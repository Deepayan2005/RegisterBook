package com.coderdeepayan.registerbook;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class CreateRecordActivity extends AppCompatActivity {
    RecyclerView recyclerView_creditors,newRecordsRecyclerView;
    MaterialButton addNewRecordButton,saveButton;
    CheckBox newCreditorCheckbox;
    TextInputEditText newCreditorIdInput,newCreditorNameInput;
    DataAdapter dataAdapter;
    List<DataRecord> dataRecordList;
    Creditor creditor;
    LinearLayout linearLayout;
    String date;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);

        dataRecordList = new ArrayList<>();
        recyclerView_creditors = findViewById(R.id.creditorsRecyclerView);
        linearLayout = findViewById(R.id.inputLayout);
        newRecordsRecyclerView = findViewById(R.id.newRecordsRecyclerView);
        newCreditorCheckbox = findViewById(R.id.wantNewCreditorCheckBox);
        addNewRecordButton = findViewById(R.id.addNewRecordButton);
        newCreditorIdInput = findViewById(R.id.newCreditorInput_ID);
        newCreditorNameInput = findViewById(R.id.newCreditorInput_Name);
        saveButton=findViewById(R.id.saveButton);

        dataAdapter = new DataAdapter(this,dataRecordList,null,null);
        newRecordsRecyclerView.setAdapter(dataAdapter);
        newRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        dataAdapter.notifyDataSetChanged();

        List<Creditor> creditorList  = new ArrayList<>();
        Set<Creditor> creditorSet = LoadingActivity.creditorSet;
        linearLayout.setVisibility(View.GONE);

        if (creditorSet.size()>0){
            for (Creditor creditor:creditorSet.toArray(new Creditor[0])) {
                creditorList.add(creditor);
            }
        }
        else {
            recyclerView_creditors.setVisibility(View.GONE);
            newCreditorCheckbox.setChecked(true);
            linearLayout.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            @Override
            public void onClick(View v) {
                try {
                    if (dataRecordList.size()>0){

                        dataRecordList.addAll(LoadingActivity.dataRecordList);
                        uploadData(dataRecordList);
                    }
                    else {
                        Toast.makeText(CreateRecordActivity.this,
                                "Nothing to save. First add now", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("Upload", "onClick: ", e);
                }
            }
        });

        newCreditorCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    recyclerView_creditors.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView_creditors.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });

        CreditorAdapter creditorAdapter = new CreditorAdapter(this, creditorList,
                new SelectCreditorListener() {
            @Override
            public void selectedCreditor(int position) {
                creditor = creditorList.get(position);
            }
        });

        recyclerView_creditors.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView_creditors.setAdapter(creditorAdapter);
        creditorAdapter.notifyDataSetChanged();

        addNewRecordButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (newCreditorCheckbox.isChecked()){
                    if (newCreditorNameInput.getText().toString().trim().length()==0){
                        Toast.makeText(CreateRecordActivity.this,
                                "Put creditor name.", Toast.LENGTH_SHORT).show();
                        newCreditorNameInput.setError("Put creditor name");
                    }
                    if (newCreditorIdInput.getText().toString().trim().length()==0){
                        Toast.makeText(CreateRecordActivity.this,
                                "Put creditor ID.", Toast.LENGTH_SHORT).show();
                        newCreditorIdInput.setError("Put creditor ID");
                    }
                    if (newCreditorIdInput.getText().toString().trim().length()>0 &&
                            newCreditorNameInput.getText().toString().trim().length()>0){
                        showAddDialogBox(new Creditor(newCreditorNameInput.getText().toString().trim(),
                                newCreditorNameInput.getText().toString().trim(),false));
                    }
                } else if (creditor!=null) {
                    showAddDialogBox(creditor);
                }
                else {
                    Toast.makeText(CreateRecordActivity.this,
                            "Select creditor first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private void uploadData(List<DataRecord> recordList) throws JSONException {
        View view = LayoutInflater.from(this).inflate(R.layout.upload_view,null,false);

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

        JSONObject mainJson = new JSONObject();
        String tempIds = "",tempNames = "";
        for (int i = 0; i <recordList.size() ; i++) {
            if (tempIds.trim().isEmpty()){
                tempIds = tempIds+(recordList.get(i).getId());
                tempNames = tempNames+(recordList.get(i).getCreditor());
            }
            else {
                if (!tempIds.contains(recordList.get(i).getId())){
                    tempIds= tempIds+"%"+(recordList.get(i).getId());
                    tempNames= tempNames+"%"+(recordList.get(i).getCreditor());
                }
            }
        }

        String[] ids = tempIds.split("%");
        String[] names = tempNames.split("%");

        Log.d("Data", "uploadData: IDS "+ Arrays.toString(ids));
        Log.d("Data", "uploadData: Names "+ Arrays.toString(names));

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i <ids.length ; i++) {
            int finalI = i;
            List<DataRecord> records2 = recordList.stream().filter(new Predicate<DataRecord>() {
                @Override
                public boolean test(DataRecord dataRecord) {
                    return ids[finalI].equalsIgnoreCase(dataRecord.getId());
                }
            }).toList();

            JSONArray tempJsonArray = new JSONArray();
            for (DataRecord dataRec:records2) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("purpose",dataRec.getPurpose());
                jsonObject.put("date",dataRec.getDateView());
                jsonObject.put("cost",dataRec.getAmount());
                tempJsonArray.put(jsonObject);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",names[i]);
            jsonObject.put("id",ids[i]);
            jsonObject.put("bill",tempJsonArray);
            jsonArray.put(jsonObject);
        }

        mainJson.put("data",jsonArray);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GithubService githubService = new GithubService();
                    int resp = githubService.uploadRecords(mainJson);
                    if (resp==200){
                        Log.d("Success", "Upload Done. Code="+resp);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }
                    else {
                        Log.e("Error", "Code = "+resp );
                    }
                } catch (Exception e) {
                    Log.e("TAG", "Upload: ",e );
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    private void showAddDialogBox(Creditor creditor) {
        View view = LayoutInflater.from(this).inflate(R.layout.new_record_view,null,false);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        TextView textView = view.findViewById(R.id.creditorNameView);
        TextInputEditText purposeInput = view.findViewById(R.id.newPurposeInput),
                amountInput = view.findViewById(R.id.newAmountInput);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        MaterialButton materialButton = view.findViewById(R.id.createNewRecordButton);

        textView.setText(creditor.getName());
        calendarView.setDate(System.currentTimeMillis());
        date = DateTimeFormatter.ofPattern("dd/MMMM/yyyy").format(LocalDate.now());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = DateTimeFormatter.ofPattern("dd/MMMM/yyyy").format(LocalDate.of(year,month+1,dayOfMonth));
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountInput.getText().toString().trim().length()==0){
                    Toast.makeText(CreateRecordActivity.this,
                            "Put the amount of the record.", Toast.LENGTH_SHORT).show();
                }
                if (purposeInput.getText().toString().trim().length()==0){
                    Toast.makeText(CreateRecordActivity.this,
                            "Put the purpose of the record.", Toast.LENGTH_SHORT).show();
                }
                if (amountInput.getText().toString().trim().length()>0 &&
                        purposeInput.getText().toString().trim().length()>0){
                    dataRecordList.add(new DataRecord(creditor.getName(),
                            creditor.getId(),purposeInput.getText().toString(),date,
                            Double.parseDouble(amountInput.getText().toString()),DataRecord.RECORD));
                    dataAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                    date = "";
                }
            }
        });
        alertDialog.show();
    }
}