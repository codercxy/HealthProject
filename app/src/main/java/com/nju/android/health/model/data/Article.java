package com.nju.android.health.model.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by 57248 on 2016/10/10.
 */

public class Article implements Parcelable{

    private Integer imgRes;
    private String title;
    private String type;
    private String author;
    private String date;
    private String content;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imgRes);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<Article> CREATOR
            = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }

    };

    public Article(Parcel in) {
        imgRes = in.readInt();
        title = in.readString();
        type = in.readString();
        author = in.readString();
        date = in.readString();
        content = in.readString();
    }
    public Article() {

    }

    public Integer getImgRes() {
        return imgRes;
    }

    public void setImgRes(Integer imgRes) {
        this.imgRes = imgRes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
