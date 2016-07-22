package com.archer.truesure.treasure.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archer.truesure.R;
import com.archer.truesure.treasure.TreasureRepo;

/**
 * Author: qixuefeng on 2016/7/22 0022.
 * E-mail: 377289596@qq.com
 */
public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setBackgroundResource(R.drawable.screen_bg);
        adapter = new ListAdapter();
        recyclerView.setAdapter(adapter);



        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TreasureRepo repo = TreasureRepo.getInstance();
        adapter.addItems(repo.getTreasure());

    }
}
