package com.example.yodenproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yodenproject.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TenderAdapter extends RecyclerView.Adapter<TenderAdapter.TenderViewHolder> {

    private List<TenderRecycler> tendersList;
    private MyTenderListener listener;

    public interface MyTenderListener{
        void onTenderClick(int position);
    }

    public void setListener(MyTenderListener listener){
        this.listener=listener;
    }

    public TenderAdapter(List<TenderRecycler> tendersList) {
        this.tendersList = tendersList;
    }

    public class TenderViewHolder extends RecyclerView.ViewHolder{

        TextView nameOrderTv;
        ImageView imageOrderIV;
        TextView finalDateTv;
        Button showOrderBtn;

        public TenderViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOrderTv=itemView.findViewById(R.id.order_name_tv);
            imageOrderIV=itemView.findViewById(R.id.image_order_in_tender);
            finalDateTv=itemView.findViewById(R.id.order_finaldate_tv);
            showOrderBtn=itemView.findViewById(R.id.show_tender_detail_btn);

            showOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onTenderClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public TenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tender_fragment,parent,false);
        TenderViewHolder tenderViewHolder=new TenderViewHolder(view);
        return tenderViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TenderViewHolder holder, int position) {

        TenderRecycler tender=tendersList.get(position);
        holder.nameOrderTv.setText(tender.getNameOrder());
        holder.finalDateTv.setText(tender.getFinalDate());
        if(tender.getPhoto().equals("default"))
            holder.imageOrderIV.setImageResource(R.drawable.ic_dress);
        else
            Glide.with(holder.imageOrderIV.getContext()).load(tender.getPhoto()).into(holder.imageOrderIV);

    }

    @Override
    public int getItemCount() {
        return tendersList.size();
    }
}

