package com.example.yodenproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yodenproject.Model.Order;
import com.example.yodenproject.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActiveOrderProAdapter extends RecyclerView.Adapter<ActiveOrderProAdapter.OrderViewHolder> {

    private List<Order> orderActiveList;

    private MyOrderListener listener;

    public interface MyOrderListener{
        void onFinishClick(int position);
        void onViewClick(int position);
    }

    public void setListener(MyOrderListener listener){
        this.listener=listener;
    }

    public ActiveOrderProAdapter(List<Order> ordersList) {
        this.orderActiveList = ordersList;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        ImageView imageOrderIV;
        Button viewOrderBtn;
        Button finishOrderBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageOrderIV=itemView.findViewById(R.id.image_order);
            finishOrderBtn=itemView.findViewById(R.id.finish_order_btn);
            viewOrderBtn=itemView.findViewById(R.id.view_order_btn);


            finishOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onFinishClick(getAdapterPosition());
                }
            });

            viewOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onViewClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_s_profess_active,parent,false);
        OrderViewHolder orderViewHolder=new OrderViewHolder(view);
        return orderViewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order=orderActiveList.get(position);
        if(order.getPhoto().equals("default"))
            holder.imageOrderIV.setImageResource(R.drawable.ic_dress);
        else
            Glide.with(holder.imageOrderIV.getContext()).load(order.getPhoto()).into(holder.imageOrderIV);
    }


    @Override
    public int getItemCount() {
        return orderActiveList.size();
    }
}