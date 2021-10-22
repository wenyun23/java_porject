package com.example.online_photo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

//写一个适配器,继承ListAdapter
public class Photo_Adapter extends ListAdapter<photoItem,Photo_Adapter.Myadapter>{
    private final int TYPE1_cell=0;
    private final int TYPE2_upload=1;

    public int THREE_STATE=Tool.STATE_LOADING;
    private final Photo_ViewModel viewModel;

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount()-1==position)return TYPE2_upload;//当位置为最后一张图片时，就显示“正在加载”那个视图
        else return TYPE1_cell;//否则显示正常视图
    }

    public Photo_Adapter(Photo_ViewModel viewModel){

        super(new DiffUtil.ItemCallback<photoItem>() {//因为继承的是ListAdapter,所以要有比较器
            @Override
            public boolean areItemsTheSame(@NonNull photoItem oldItem, @NonNull photoItem newItem) {
                return oldItem==newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull photoItem oldItem, @NonNull photoItem newItem) {
                return oldItem.getId()==newItem.getId();
            }
        });

        this.viewModel=viewModel;
    }

    @NonNull
    @Override
    public Myadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Myadapter myadapter;
        if (viewType==TYPE1_cell) {//如果是第一个adapter就显示第一个adapter

            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
            myadapter= new Myadapter(itemview);

            Myadapter finalMyadapter = myadapter;
            myadapter.image_phtot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();

                    //将整个listview传过去
                    bundle.putParcelableArrayList("PHOTO_LIST", new ArrayList(getCurrentList()));
                    bundle.putInt("PHOTO_POSITION", finalMyadapter.getAdapterPosition());//将当前点击的图片文字传过去

                    NavController controller = Navigation.findNavController(view);
                    controller.navigate(R.id.action_photo_Fragment_to_look_Fragment, bundle);
                }
            });
        }else {//否则就是显示底部“正在加载”的视图
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload, parent, false);
            myadapter= new Myadapter(view);
            //让底部正在加载视图显示在中间
            ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).setFullSpan(true);

            Myadapter finalMyadapter1 = myadapter;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalMyadapter1.progressBar.setVisibility(View.VISIBLE);
                    finalMyadapter1.text_title.setText("正在加载");
                    viewModel.fectData();
                }
            });
        }
        return myadapter;
    }

    @Override
    public void onBindViewHolder(@NonNull Myadapter holder, int position) {
        if (position==getItemCount()-1){
            Log.d("again9", "onBindViewHolder: "+THREE_STATE);
            switch (THREE_STATE){
                case  Tool.STATE_LOADING:{
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.text_title.setText("正在加载");
                    holder.setIsRecyclable(false);
                }break;
                case Tool.STATE_FAILURE:{
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    holder.text_title.setText("加载异常，点击重试");
                    holder.setIsRecyclable(true);
                }break;
                case Tool.STATE_COMPLETE:{
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    holder.text_title.setText("加载完成");
                    holder.setIsRecyclable(false);
                }break;

            }
            return;
        }

        holder.shimmercell.setShimmerColor(0x55FFFFFF);
        holder.shimmercell.setShimmerAngle(0);
        holder.shimmercell.startShimmerAnimation();

        holder.author.setText("作者:"+getItem(position).getUser());
        holder.like_text.setText("点赞量::"+getItem(position).getLikes());
        holder.live_text.setText("收藏量::"+getItem(position).getCollections());


        Glide.with(holder.itemView)
                .load(getItem(position).webformatURL)
                .placeholder(R.drawable.ic_baseline_photo_24)
                .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (holder.shimmercell!=null){
                                    holder.shimmercell.stopShimmerAnimation();
                                }
                                return false;
                            }
                        }
                )
                .into(holder.image_phtot);
    }

    class Myadapter extends RecyclerView.ViewHolder{
        ShimmerLayout shimmercell;
        ImageView image_phtot;
        TextView author,like_text,live_text;

        ProgressBar progressBar;
        TextView text_title;
        public Myadapter(@NonNull View itemView) {
            super(itemView);
            shimmercell=itemView.findViewById(R.id.shimmercell);
            image_phtot=itemView.findViewById(R.id.image_photo);
            author=itemView.findViewById(R.id.author);
            like_text=itemView.findViewById(R.id.like_text);
            live_text=itemView.findViewById(R.id.live_text);

            progressBar=itemView.findViewById(R.id.progressBar);
            text_title=itemView.findViewById(R.id.text_title);
        }
    }


}
