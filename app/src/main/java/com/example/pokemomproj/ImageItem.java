package com.example.pokemomproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageItem extends RecyclerView.Adapter<ImageItem.ViewHolder> {

    // CLASS ITEM
    public static class item {
        private String tenHinh;
        private int resourceId;
        private int soLuong; // Thêm số lượng stack

        public item(String tenHinh, int resourceId) {
            this.tenHinh = tenHinh;
            this.resourceId = resourceId;
            this.soLuong = 0; // Mặc định là 0
        }

        public String gettenitem() {
            return tenHinh;
        }

        public int getDrawableId() {
            return resourceId;
        }

        public int getSoLuong() {
            return soLuong;
        }

        public void tangSoLuong() {
            this.soLuong++;
        }

        public void setSoLuong(int soLuong) {
            this.soLuong = soLuong;
        }
    }

    // INTERFACE CHO CLICK
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ADAPTER
    private Context context;
    private ArrayList<item> dsHinh;

    public ImageItem(Context context, ArrayList<item> dsHinh) {
        this.context = context;
        this.dsHinh = dsHinh;
    }

    // VIEWHOLDER
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtSoLuong;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgOnly);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        item currentItem = dsHinh.get(position);

        holder.imageView.setImageResource(currentItem.getDrawableId());

        // Hiển thị số lượng nếu > 0
        int soLuong = currentItem.getSoLuong();
        if (soLuong > 0) {
            holder.txtSoLuong.setVisibility(View.VISIBLE);
            holder.txtSoLuong.setText("x" + soLuong);
        } else {
            holder.txtSoLuong.setVisibility(View.GONE);
        }

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
