package com.example.paginationdemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import kotlin.Metadata;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> {
    private ArrayList<MainData> arrayList;
    private Context context;

    public MyAdapter(ArrayList<MainData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MainData data = arrayList.get(position);
        //set image on imageView
        Glide.with(context).load(data.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        //set text on textView
        holder.textView.setText(data.getAuthorName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected static class viewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_View);
        }
    }
}
