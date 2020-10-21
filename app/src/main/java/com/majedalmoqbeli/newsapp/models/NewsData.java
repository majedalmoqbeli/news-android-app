package com.majedalmoqbeli.newsapp.models;


public class NewsData {


    private String news_id, news_title, news_info, news_lat, news_lng, news_date, news_image,
            user_id, u_name, u_image;


    public NewsData(String news_id, String news_title, String news_info, String news_lat,
                    String news_lng, String news_date, String news_image, String user_id,
                    String u_name, String u_image) {
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_info = news_info;
        this.news_lat = news_lat;
        this.news_lng = news_lng;
        this.news_date = news_date;
        this.news_image = news_image;
        this.user_id = user_id;
        this.u_name = u_name;
        this.u_image = u_image;
    }


    public String getNews_id() {
        return news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public String getNews_info() {
        return news_info;
    }

    public Double getNews_lat() {
        return Double.parseDouble(news_lat);
    }

    public Double getNews_lng() {
        return Double.parseDouble(news_lng);
    }

    public String getNews_date() {
        return news_date;
    }

    public String getNews_image() {
        return news_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getU_name() {
        return u_name;
    }

    public String getU_image() {
        return u_image;
    }
}
