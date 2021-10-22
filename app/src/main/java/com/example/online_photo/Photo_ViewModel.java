package com.example.online_photo;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Photo_ViewModel extends AndroidViewModel {
    public MutableLiveData<List<photoItem>> _photoListLive;//用于观察要加载的数据
    public MutableLiveData<Integer> _StatListLive;//用于观察三种状态

    //定义一些key值
    private final String[] keyurl = {"cat", "dog", "car", "beauty", "phone", "computer", "flower", "pig",
            "duck", "chicken", "fish", "scenery", "fruit", "snacks", "toy", "cellphone", "cow", "star", "tank",
            "house", "city", "state", "worker", "boy", "girl"};

    boolean cometop=true;//回到顶部
    private final int perpage=25;//每次加载的页数

    private int currentPage = 1;//当前页码
    private int totalPage = 1;//总页数
    private String currentKey = "";//当前关键词
    private boolean isNewQuery = true; //是否是新请求
    private boolean isLoading = false; //是否在加载中


    public Photo_ViewModel(@NonNull Application application) {
        super(application);
        _photoListLive = new MutableLiveData<>();
        _StatListLive=new MutableLiveData<>();
    }

    public void requestData(){
        //初始化一些值
        currentPage = 1;
        totalPage = 1;
        currentKey=keyurl[new Random().nextInt(keyurl.length)];
        isNewQuery = true;
        cometop=true;
        Log.d("test3", "requestData: ");
        fectData();//加载数据
    }

    public void fectData() {
        Log.d("test1", "fectData: "+isLoading);
        if (isLoading)return;//开始就拦截一下
        if (currentPage>totalPage){//如果当前页数大于总页数，就结束加载
            _StatListLive.setValue(Tool.STATE_COMPLETE);//状态为加载完成
            return;
        }

        isLoading=true;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, getURl(),
                response -> {
                    Pixabay pixabay=new Gson().fromJson(response,Pixabay.class);

                    totalPage=(int)Math.ceil((double)(pixabay.getTotalHits())/perpage);

                    if (isNewQuery){
                        Log.d("again2", "fectData:");
                        _photoListLive.setValue(pixabay.getHits());
                    }else {

                        ArrayList<photoItem> arrayList1=new ArrayList<>(pixabay.getHits());
                        Log.d("again3", "fectData: "+arrayList1);
                        ArrayList<photoItem> arrayList2=new ArrayList<>(_photoListLive.getValue());
                        Log.d("again4", "fectData:"+arrayList2);

                        ArrayList<photoItem> arrayList3=new ArrayList<>();

                        for (int i=0;i<arrayList2.size();i++){
                            arrayList3.add(arrayList2.get(i));
                        }

                        for (int i=0;i<arrayList1.size();i++){
                            arrayList3.add(arrayList1.get(i));
                        }

                        Log.d("again5","fectData:"+arrayList3.size());
                        Log.d("again6","fectData:"+arrayList3);

                        _photoListLive.setValue(arrayList3);
                    }

                    _StatListLive.setValue(Tool.STATE_LOADING);//状态赋值正在加载
                    isNewQuery=false;
                    isLoading=false;
                    currentPage++;
                },
                error -> {
                    Log.d("test6", "fectData: ");
                    _StatListLive.setValue(Tool.STATE_FAILURE);//状态赋值加载异常
                    isLoading=false;
                    Log.d("TAG", "fectData:" + error.toString());
                }
        );

        VolleySingtel.getInstance(getApplication()).requestQueue().add(stringRequest);
    }


    private String getURl() {//定义一个函数用来存要加载的网址(key值，每次加载的页数，第几页)
        return "https://pixabay.com/api/?key=23474477-731f9ca718df43feba3954945&q=" + currentKey + "&per_page="+perpage+"&page"+currentPage;
    }

    public MutableLiveData<List<photoItem>> get_photoListLive() {
        return _photoListLive;
    }

    public void set_photoListLive(MutableLiveData<List<photoItem>> _photoListLive) {
        this._photoListLive = _photoListLive;
    }

    public MutableLiveData<Integer> get_StatListLive() {
        return _StatListLive;
    }

    public void set_StatListLive(MutableLiveData<Integer> _StatListLive) {
        this._StatListLive = _StatListLive;
    }
}
