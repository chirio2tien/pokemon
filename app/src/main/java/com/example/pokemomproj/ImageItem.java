package com.example.pokemomproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageItem extends
        RecyclerView.Adapter<ImageItem.ViewHolder> {
    public static class HinhAnh {
        private String tenHinh;
        private int resourceId;

        public HinhAnh(String tenHinh, int resourceId) {
            this.tenHinh = tenHinh;
            this.resourceId = resourceId;
        }

        public String getTenHinh() {
            return tenHinh;
        }

        public int getResourceId() {
            return resourceId;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private Context context;
    private ArrayList<HinhAnh> dsHinh; // danh sách resource ID hình ảnh

    public ImageItem(Context context, ArrayList<HinhAnh> dsHinh) {
        this.context = context;
        this.dsHinh = dsHinh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgOnly);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(dsHinh.get(position).getResourceId());

        holder.imageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dsHinh.size();
    }
}
