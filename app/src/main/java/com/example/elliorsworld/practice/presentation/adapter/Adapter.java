package com.example.elliorsworld.practice.presentation.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.elliorsworld.practice.R;
import com.example.elliorsworld.practice.domain.model.ResultImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private static final String TAG = "Adapter";

    private final List<ResultImage> imageViewList = new ArrayList<>();
    private final OnImageListener mOnImageListener;

    public Adapter(OnImageListener onImageListener) {
        this.mOnImageListener = onImageListener;
    }

    public void setItems(Collection<ResultImage> imageViews) {
        imageViewList.clear();
        imageViewList.addAll(imageViews);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v, mOnImageListener);
    }

    private int getBackgroundColor(int position){
        if(position%2 == 0){
            return Color.GREEN;
        } else {
            return Color.RED;
        }}

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.imageView.setImageBitmap(imageViewList.get(i).getBitmap());
        viewHolder.itemView.setBackgroundColor(getBackgroundColor(i));
    }

    @Override
    public int getItemCount() {
        return imageViewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final OnImageListener onImageListener;

        ViewHolder(View v, OnImageListener onImageListener) {
            super(v);
            imageView = v.findViewById(R.id.image_res);
            this.onImageListener = onImageListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ResultImage image = imageViewList.get(getAdapterPosition());
            onImageListener.onImageClick(image);
            Log.d(TAG, "result:" + imageViewList.get(getAdapterPosition()));
        }
    }

    public interface OnImageListener {
        void onImageClick(ResultImage image);
    }
}
