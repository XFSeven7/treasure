package com.archer.truesure.treasure.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.archer.truesure.components.TreasureView;
import com.archer.truesure.treasure.Treasure;
import com.archer.truesure.treasure.detail.DetailActivity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Author: qixuefeng on 2016/7/22 0022.
 * E-mail: 377289596@qq.com
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private TreasureView treasureView;

    private ArrayList<Treasure> datas = new ArrayList<>();
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        treasureView = new TreasureView(parent.getContext());
        return new ViewHolder(treasureView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.treasureView.bindTreasure(datas.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.open(v.getContext(),datas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public final void addItems(Collection<Treasure> items) {
        if (items != null) {
            datas.addAll(items);
            notifyItemRangeChanged(0,datas.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TreasureView treasureView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.treasureView = (TreasureView) itemView;
        }
    }

}
