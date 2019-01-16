package com.lee.mvvm.base.view.widge.rv;

import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * this adapter is use for DataBinding&RecycleView
 *
 * @param <T> data type
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder>
        implements View.OnClickListener {

    private OnItemClickListener onItemClickListener;

    protected int layoutId;
    protected int variableId;
    protected List<T> data;

    /**
     * @param layoutId   item layout id
     * @param variableId variable id: the name of variable
     * @param data       data
     */
    public BaseAdapter(@LayoutRes int layoutId, int variableId, @NonNull List<T> data) {
        this.layoutId = layoutId;
        this.variableId = variableId;
        this.data = data;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, null, false)
                .getRoot();
        v.setTag(i);
        v.setOnClickListener(this);
        return new BaseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        convert(baseViewHolder, data.get(i));
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void convert(BaseViewHolder holder, T t) {
        holder.setBinding(variableId, t);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
