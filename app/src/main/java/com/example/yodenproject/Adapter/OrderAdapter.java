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

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    private List<Order> orderList;

    private MyOrderListener listener;

    public interface MyOrderListener{
        void onViewClick(int position);
    }

    public void setListener(MyOrderListener listener){
        this.listener=listener;
    }

    public OrderAdapter(List<Order> ordersList) {
        this.orderList = ordersList;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        ImageView imageOrderIV;
        Button viewOrderBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageOrderIV=itemView.findViewById(R.id.image_order_user);
            viewOrderBtn=itemView.findViewById(R.id.view_order_btn);


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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_s_user_card,parent,false);
        OrderViewHolder orderViewHolder=new OrderViewHolder(view);
        return orderViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order=orderList.get(position);
        if(order.getPhoto().equals("default"))
            holder.imageOrderIV.setImageResource(R.drawable.ic_dress);
        else
            Glide.with(holder.imageOrderIV.getContext()).load(order.getPhoto()).into(holder.imageOrderIV);
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }
}