package com.example.yodenproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yodenproject.R;
import com.github.abdularis.civ.CircleImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private List<OfferRecycler> offerList;
    private OfferAdapter.MyOfferListener listener;

    public OfferAdapter(List<OfferRecycler> offerList) {
        this.offerList = offerList;
    }

    public void setListener(OfferAdapter.MyOfferListener listener){
        this.listener=listener;
    }

    public interface MyOfferListener{
        void onOfferClick(int position);
        void onOfferLongClick(int position);
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder{

        TextView nameProTv;
        CircleImageView imageProIV;
        TextView relevantPriceTv;
        TextView moreInfoTv;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProTv=itemView.findViewById(R.id.pro_name_tv);
            imageProIV=itemView.findViewById(R.id.professional_offer_pic);
            relevantPriceTv=itemView.findViewById(R.id.relevant_price_tv);
            moreInfoTv=itemView.findViewById(R.id.moreInfo_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onOfferClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(listener!=null)
                        listener.onOfferLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_fragment,parent,false);
        OfferAdapter.OfferViewHolder offerViewHolder=new OfferAdapter.OfferViewHolder(view);
        return offerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        OfferRecycler offerRecycler=offerList.get(position);
        holder.nameProTv.setText(offerRecycler.getNameProfessional());
        holder.relevantPriceTv.setText(holder.itemView.getContext().getResources().getString(R.string.price)+": "+offerRecycler.getRelevantPrice());
        holder.moreInfoTv.setText(offerRecycler.getMoreInfo());
        if(offerRecycler.getPhotoPro().equals("default")){
            holder.imageProIV.setImageResource(R.drawable.ic_user);
        }
        else Glide.with(holder.imageProIV.getContext()).load(offerRecycler.getPhotoPro()).into(holder.imageProIV);

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}
