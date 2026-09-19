package com.coderdeepayan.registerbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CreditorAdapter extends RecyclerView.Adapter<CreditorAdapter.Cholder> {

    Context context;
    List<Creditor> creditorList;
    SelectCreditorListener selectCreditorListener;

    private int currentPosition = -1,previousPosition=-1;

    public CreditorAdapter(Context context, List<Creditor> creditorList, SelectCreditorListener selectCreditorListener) {
        this.context = context;
        this.creditorList = creditorList;
        this.selectCreditorListener = selectCreditorListener;
    }

    @NonNull
    @Override
    public CreditorAdapter.Cholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Cholder(LayoutInflater.from(context).inflate(R.layout.creditor_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CreditorAdapter.Cholder holder, int position) {
        holder.showData(creditorList.get(position));
    }

    @Override
    public int getItemCount() {
        return creditorList.size();
    }

    class Cholder extends RecyclerView.ViewHolder{
        RadioButton radioButton;

        public Cholder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.creditorRadioButton);
        }

        public void showData(Creditor creditor) {
            radioButton.setText(creditor.getName());
            radioButton.setChecked(currentPosition==getLayoutPosition());

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = getLayoutPosition();
                    previousPosition = currentPosition;
                    currentPosition = newPosition;
                    if (previousPosition != -1) {
                        notifyItemChanged(previousPosition);
                    }
                    notifyItemChanged(currentPosition);
                    selectCreditorListener.selectedCreditor(currentPosition);
                }
            });
        }
    }
}
