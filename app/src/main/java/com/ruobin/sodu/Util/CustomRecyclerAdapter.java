package com.ruobin.sodu.Util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruobin on 2017/9/27.
 */
public class CustomRecyclerAdapter<T> extends RecyclerView.Adapter<CustomRecyclerAdapter.ListViewHolder> {

    private List<T> mData;

    private int itemViewId;

    private CustomRecyclerAdapter.ItemActionListener onItemActionListener;


    public CustomRecyclerAdapter(List<T> data,int id) {

        mData = data;
        itemViewId = id;
    }

    public void updateData(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * 添加新的Item
     */
    public void addNewItem(T item) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(0, item);
        notifyItemInserted(0);
    }

    /**
     * 删除Item
     */
    public void deleteItem(T item) {
        if (mData == null || mData.isEmpty()) {
            return;
        }

        int index = mData.indexOf(item);
        mData.remove(item);
        notifyItemRemoved(index);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(itemViewId, parent, false);
        ListViewHolder holder = new ListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        T data = mData.get(position);

        if (onItemActionListener != null) {
            onItemActionListener.onItemInitData(holder.itemView, data);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemActionListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemActionListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemActionListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemActionListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }


    public void setOnItemClickListener(CustomRecyclerAdapter.ItemActionListener listener) {

        this.onItemActionListener = listener;
    }

    @Override
    public int getItemCount() {

        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public ListViewHolder(View view) {
            super(view);
            itemView = view;
        }
    }

    public interface ItemActionListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

        void onItemInitData(View view, Object item);

    }
}
