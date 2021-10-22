package com.example.online_photo;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class Look_Adapter extends ListAdapter<photoItem,Look_Adapter.Adapter> {
    public  Look_Adapter(){
        super(new DiffUtil.ItemCallback<photoItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull photoItem oldItem, @NonNull photoItem newItem) {
                return oldItem==newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull photoItem oldItem, @NonNull photoItem newItem) {
                return oldItem.getId()==newItem.getId();
            }
        });
    }
    @NonNull
    @Override
    public Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item=LayoutInflater.from(parent.getContext()).inflate(R.layout.look_viewpage,parent,false);
        Adapter adapter=new Adapter(item);
        return adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter holder, int position) {
        Glide.with(holder.itemView)
                .load(getItem(position).webformatURL)
                .placeholder(R.drawable.ic_baseline_photo_24)
                .into(holder.look_photo);
    }

    class Adapter extends RecyclerView.ViewHolder{
        ImageView look_photo;
        public Adapter(@NonNull View itemView) {
            super(itemView);
            look_photo=itemView.findViewById(R.id.look_photo);
        }
    }
}
