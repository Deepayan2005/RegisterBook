package com.coderdeepayan.registerbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.AHolder> {
    List<Analytics> analyticsList;

    public AnalyticsAdapter(List<Analytics> analyticsList) {
        this.analyticsList = analyticsList;
    }

    @NonNull
    @Override
    public AnalyticsAdapter.AHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.analytics_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticsAdapter.AHolder holder, int position) {
        holder.showData(analyticsList.get(position));
    }

    @Override
    public int getItemCount() {
        return analyticsList.size();
    }
    class AHolder extends RecyclerView.ViewHolder{
        private TextView creditorNameView,totalAmountView,givenPaybackView, remainingAmountView,givenPercentageView;
        public AHolder(@NonNull View itemView) {
            super(itemView);
            creditorNameView = itemView.findViewById(R.id.creditorNameView2);
            totalAmountView = itemView.findViewById(R.id.totalAmountView);
            givenPaybackView  =itemView.findViewById(R.id.returnedAmountView);
            remainingAmountView = itemView.findViewById(R.id.remainingAmountView);
            givenPercentageView = itemView.findViewById(R.id.remainingPercentageView);
        }

        public void showData(Analytics analytics) {
            creditorNameView.setText(analytics.getCreditorName());
            totalAmountView.setText(analytics.getTotalAmount()+"");
            givenPaybackView.setText(analytics.getGivenBackAmount()+"");
            remainingAmountView.setText(analytics.getRemainingAmount()+"");
            givenPercentageView.setText(analytics.getGivenPercentage()+"%");
        }
    }
}
