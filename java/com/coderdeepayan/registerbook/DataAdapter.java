package com.coderdeepayan.registerbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter {
    Context context;
    List<DataRecord> dataRecordList;
    AddButtonListener addButtonListener;
    AnalyticsListener analyticsListener;

    public DataAdapter(Context context, List<DataRecord> dataRecordList,
                       AddButtonListener addButtonListener, AnalyticsListener analyticsListener) {
        this.context = context;
        this.dataRecordList = dataRecordList;
        this.addButtonListener = addButtonListener;
        this.analyticsListener = analyticsListener;
    }

    @Override
    public int getItemViewType(int position) {
        return dataRecordList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case DataRecord.RECORD:
                return new DataHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.record_view,parent,false));
            case DataRecord.UTILITIES:
                return new UtilitesHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.utilities_view,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (dataRecordList.get(position).getType()){
            case DataRecord.RECORD:
                ((DataHolder)holder).showDataRecord(dataRecordList.get(position));
                break;
            case DataRecord.UTILITIES:
                ((UtilitesHolder)holder).showUtility();
        }

    }

    @Override
    public int getItemCount() {
        return dataRecordList.size();
    }

    class UtilitesHolder extends RecyclerView.ViewHolder{
        private ImageView addButton,graphButton;
        public UtilitesHolder(@NonNull View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.addButton);
            graphButton = itemView.findViewById(R.id.graphButton);
        }

        public void showUtility() {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addButtonListener.want_to_add_Record();
                }
            });
            graphButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    analyticsListener.showAnalyticsLister();
                }
            });
        }
    }
    class DataHolder extends RecyclerView.ViewHolder{
        private TextView creditorNameView, purposeNameView, amountView,dateView;
        public DataHolder(@NonNull View itemView) {
            super(itemView);
            creditorNameView = itemView.findViewById(R.id.creditorView);
            purposeNameView = itemView.findViewById(R.id.purposeNameView);
            amountView = itemView.findViewById(R.id.amountView);
            dateView =itemView.findViewById(R.id.dateView);
        }
        public void showDataRecord(DataRecord dataRecord) {
            creditorNameView.setText(dataRecord.getCreditor());
            purposeNameView.setText(dataRecord.getPurpose());
            amountView.setText(dataRecord.getAmount()+"");
            dateView.setText(dataRecord.getDateView());

            if (dataRecord.getAmount()<0){
                amountView.setTextColor(context.getColor(R.color.green));
            }
            else {
                amountView.setTextColor(context.getColor(R.color.red));
            }
        }
    }
}
