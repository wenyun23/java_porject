package com.example.online_photo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

//数据类,用于向服务器请求资源
public class Pixabay {
    private int totalHits;
    private int total;
    private List<photoItem>hits;

    public Pixabay(int totalHits, int total, List<photoItem> hits) {//构造方法
        this.totalHits = totalHits;
        this.total = total;
        this.hits = hits;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<photoItem> getHits() {
        return hits;
    }

    public void setHits(List<photoItem> hits) {
        this.hits = hits;
    }

    //重写一下equals和hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixabay pixabay = (Pixabay) o;
        return totalHits == pixabay.totalHits && total == pixabay.total && Objects.equals(hits, pixabay.hits);
    }

    @Override
    public int hashCode() {

        return Objects.hash(totalHits, total, hits);
    }
}
class photoItem implements Parcelable {
    @SerializedName("webformatURL") String webformatURL;//存常规图
    @SerializedName("largeImageURL")String largeImageURL;//存大图
    @SerializedName("id")int id;//存图片ID
    @SerializedName("user")String user;//存图片的作者
    @SerializedName("likes")int likes;//存图片喜欢数量
    @SerializedName("collections")int collections;//存图片收藏数量


    protected photoItem(Parcel in) {
        webformatURL = in.readString();
        largeImageURL = in.readString();
        id = in.readInt();
        user = in.readString();
        likes = in.readInt();
        collections = in.readInt();
    }

    public static final Creator<photoItem> CREATOR = new Creator<photoItem>() {
        @Override
        public photoItem createFromParcel(Parcel in) {
            return new photoItem(in);
        }

        @Override
        public photoItem[] newArray(int size) {
            return new photoItem[size];
        }
    };

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCollections() {
        return collections;
    }

    public void setCollections(int collections) {
        this.collections = collections;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(webformatURL);
        parcel.writeString(largeImageURL);
        parcel.writeInt(id);
        parcel.writeString(user);
        parcel.writeInt(likes);
        parcel.writeInt(collections);
    }
}
