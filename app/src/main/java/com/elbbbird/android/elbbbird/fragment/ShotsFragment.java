package com.elbbbird.android.elbbbird.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elbbbird.android.elbbbird.R;
import com.elbbbird.android.elbbbird.app.BaseFragment;

import butterknife.Bind;

/**
 * Created by zhanghailong-ms on 2015/9/18.
 */
public class ShotsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

//    @Bind(R.id.fragment_shots_swipe_refresh)
//    SwipeRefreshLayout refresh;
//    @Bind(R.id.fragment_shots_recycler)
//    RecyclerView recycler;

    public static ShotsFragment newInstance(int type) {
        ShotsFragment newFragment = new ShotsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shots, container, false);
    }

    @Override
    public void init() {

//        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
//        refresh.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {
    }

}
