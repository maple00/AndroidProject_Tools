package com.sxs.item.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sxs.item.R;
import com.sxs.item.common.BaseRecyclerViewAdapters;

/**
 * @Author: shearson
 * @time: 2019/12/3 13:59
 * @des: 可进行文本拷贝的副本
 */
public final class CopyAdapter extends BaseRecyclerViewAdapters<String> {

    public CopyAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends BaseRecyclerViewAdapters.ViewHolder {

        ViewHolder() {
            super(R.layout.item_copy);
        }

        @Override
        public void onBindView(int position) {

        }
    }
}