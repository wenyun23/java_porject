package com.example.online_photo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
//图库浏览界面
public class Photo_Fragment extends Fragment {
    Photo_ViewModel viewModel;
    Photo_Adapter adapter;
    RecyclerView recycle;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentGalleryView = inflater.inflate(R.layout.fragment_photo_, container, false);
        recycle= fragmentGalleryView.findViewById(R.id.reycycle);
        swipeRefresh= fragmentGalleryView.findViewById(R.id.swiperefresh);
        return fragmentGalleryView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu,menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:{
                swipeRefresh.setRefreshing(true);
                viewModel.requestData();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);//让菜单显示

        viewModel=ViewModelProviders.of(this).get(Photo_ViewModel.class);
        adapter=new Photo_Adapter(viewModel);

        if (recycle!=null){
            //将其设置为瀑布流布局
            recycle.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            recycle.setAdapter(adapter);
        }

        if (viewModel._photoListLive.getValue()==null){//初始化数据
            Log.d("chushih", "onActivityCreated: ");
            swipeRefresh.setRefreshing(false);
            viewModel.requestData();
        }

        //下拉刷新
        swipeRefresh.setOnRefreshListener(() -> viewModel.requestData());

        //为要加载的数据添加观察
        viewModel._photoListLive.observe(getViewLifecycleOwner(), photoItems -> {
            if (viewModel.cometop){
                recycle.scrollToPosition(0);
                viewModel.cometop=false;
            }
            adapter.submitList(photoItems);
            swipeRefresh.setRefreshing(false);
        });

        //为三种状态添加数据
        viewModel._StatListLive.observe(getViewLifecycleOwner(), integer -> {

            adapter.THREE_STATE=integer;//将当前状态赋值过去
            Log.d("again7", "onChanged: "+adapter.THREE_STATE);
            adapter.notifyItemChanged(adapter.getItemCount()-1);

            if (integer==Tool.STATE_FAILURE){
                swipeRefresh.setRefreshing(false);
            }
        });

        //为recycleview添加一个滚动监听事件,用于监听是否浏览到最后一张图片
        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy<0){return;}

                StaggeredGridLayoutManager manager=(StaggeredGridLayoutManager)recycle.getLayoutManager();
                int[] array=new int[2];
                manager.findLastVisibleItemPositions(array);

                if (array[1]==adapter.getItemCount()-1){//如果到了最后一个图片，就加载图片
                    Log.d("again8", "onScrolled: ");
                    viewModel.fectData();
                }
            }
        });
    }
}