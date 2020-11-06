package com.majedalmoqbeli.newsapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majedalmoqbeli.newsapp.R;
import com.majedalmoqbeli.newsapp.controller.SaveSetting;
import com.majedalmoqbeli.newsapp.helper.LocalDateTimeUtils;
import com.majedalmoqbeli.newsapp.models.NewsData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ShowNewsAdapter extends RecyclerView.Adapter<ShowNewsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NewsData> newsData;

    public ShowNewsAdapter(Context context, ArrayList<NewsData> newsData) {
        this.context = context;
        this.newsData = newsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(newsData.get(position));
    }

    @Override
    public int getItemCount() {
        return newsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewUserImage, mImageViewNewsImage;
        private TextView mTextViewUserName, mTextViewNewsTitle, mTextViewNewsDate, mTextViewNewsInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewUserImage = itemView.findViewById(R.id.user_image);
            mImageViewNewsImage = itemView.findViewById(R.id.news_image);
            mTextViewUserName = itemView.findViewById(R.id.user_name);
            mTextViewNewsTitle = itemView.findViewById(R.id.news_title);
            mTextViewNewsInfo = itemView.findViewById(R.id.news_info);
            mTextViewNewsDate = itemView.findViewById(R.id.news_date);


        }


        public void bind(NewsData newsData) {
            Picasso.with(context).load(SaveSetting.ImageUserURL + newsData.getU_image())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_user_image)
                    .into(mImageViewUserImage);

            Picasso.with(context).load(SaveSetting.ImageNewsURL + newsData.getNews_image())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.news)
                    .into(target);

            mTextViewUserName.setText(newsData.getU_name());
            mTextViewNewsTitle.setText(newsData.getNews_title());
            mTextViewNewsInfo.setText(newsData.getNews_info());

            mTextViewNewsDate.setText(LocalDateTimeUtils.getOnlyDataToShow(newsData.getNews_date()));


        }

        private Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap.getWidth() >= 1000 || bitmap.getHeight() >= 1000)
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8),
                            (int) (bitmap.getHeight() * 0.8), true);

                mImageViewNewsImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageViewNewsImage.setImageBitmap(bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                mImageViewNewsImage.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mImageViewNewsImage.setImageDrawable(placeHolderDrawable);
            }
        };

    }
}
