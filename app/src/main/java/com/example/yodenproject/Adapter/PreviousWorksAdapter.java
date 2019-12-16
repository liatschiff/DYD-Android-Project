package com.example.yodenproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yodenproject.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PreviousWorksAdapter extends RecyclerView.Adapter<PreviousWorksAdapter.PreviousWorkViewHolder> {

    private ArrayList<String> imagesList;
    private MyWorksImagesListener listener;


    public interface MyWorksImagesListener{
        void onWorkClick(int position);
    }

    public void setListener(MyWorksImagesListener listener){
        this.listener=listener;
    }

    public PreviousWorksAdapter(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

    public class PreviousWorkViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public PreviousWorkViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_of_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onWorkClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public PreviousWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.prev_work_fragment,parent,false);
        PreviousWorkViewHolder previousWorkViewHolder=new PreviousWorkViewHolder(view);
        return previousWorkViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousWorkViewHolder holder, int position) {
        String urlImage=imagesList.get(position);
        if(urlImage.equals("default"))
            holder.imageView.setImageResource(R.drawable.ic_dress);
           else Glide.with(holder.imageView.getContext()).load(urlImage).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }
}
