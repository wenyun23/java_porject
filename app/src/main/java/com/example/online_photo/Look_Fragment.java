package com.example.online_photo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
//图片浏览界面
public class Look_Fragment extends Fragment {
    private View view;
    ViewPager2 viewPager2;
    TextView textView;
    Look_Adapter look_adapter;
    ImageView image_save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_look_, container, false);
        viewPager2=view.findViewById(R.id.viewPager2);
        textView=view.findViewById(R.id.page_ye);
        image_save=view.findViewById(R.id.image_save);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        look_adapter=new Look_Adapter();
        ArrayList list =getArguments().getParcelableArrayList("PHOTO_LIST");
        look_adapter.submitList(list);
        viewPager2.setAdapter(look_adapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                textView.setText(position+1+"/"+list.size());
            }
        });

        viewPager2.setCurrentItem(getArguments().getInt("PHOTO_POSITION"),false);
        //viewPager2.setOrientation(viewPager2.ORIENTATION_VERTICAL);//使图片垂直翻转

        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //动态权限申请
                if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){//没有权限
                    Log.d("TAG", "没有权限 ");
                    //申请权限
                    ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {//如果有权限，就保存
                   saveimage();
                }
            }
        });
    }

    //处理回调,用于当用拒绝权限时
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults!=null&&grantResults[0]==PackageManager.PERMISSION_GRANTED){//再次判断，表示权限已经通过
            saveimage();//执行保存
        }else {
            Toast.makeText(requireContext(),"权限获取失败,请开启权限",Toast.LENGTH_SHORT).show();
        }
    }

    public void saveimage(){
        //先利用viewpage2找到recycle,并转换为recycleview
        RecyclerView recyclerView=(RecyclerView) viewPager2.getChildAt(0);
        //再利用Recycleviewzha找到adapter,并转换为adapter
        Look_Adapter.Adapter hodel= (Look_Adapter.Adapter) recyclerView.findViewHolderForAdapterPosition(viewPager2.getCurrentItem());

        //将其预存图片转换为位图
        Bitmap bitmap=((BitmapDrawable)hodel.look_photo.getDrawable()).getBitmap();

        Uri serurl=requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new ContentValues());
        try {
            OutputStream outputStream=requireContext().getContentResolver().openOutputStream(serurl);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)){
                Toast.makeText(requireContext(),"图片保存成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireContext(),"图片保存失败",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(requireContext(),"图片保存失败",Toast.LENGTH_SHORT).show();
        }

    }
}