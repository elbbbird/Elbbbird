package com.elbbbird.android.elbbbird.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elbbbird.android.elbbbird.R;
import com.elbbbird.android.elbbbird.app.BaseFragment;
import com.elbbbird.android.elbbbird.models.Shot;
import com.elbbbird.android.elbbbird.service.DribbbleService;
import com.elbbbird.android.elbbbird.view.CircleImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhanghailong-ms on 2015/9/18.
 */
public class ShotsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.fragment_shots_swipe_refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.fragment_shots_recycler)
    RecyclerView recycler;

    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Shot> dataset = new ArrayList<>();

    private int type;
    private int page = 1;

    public static ShotsFragment newInstance(int type) {
        ShotsFragment newFragment = new ShotsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
    }

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shots, container, false);
    }

    @Override
    public void init() {

        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        refresh.setOnRefreshListener(this);

        if (DribbbleService.TYPE_SHOTS_POPULAR == type || DribbbleService.TYPE_TEAMS_POPULAR == type ||
                DribbbleService.TYPE_DEBUTS_POPULAR == type || DribbbleService.TYPE_PLAYOFFS_POPULAR == type ||
                DribbbleService.TYPE_REBOUNDS_POPULAR == type) {
            layoutManager = new GridLayoutManager(this.getActivity(), 1);
            adapter = new ElbbbirdCardPopularAdapter(ShotsFragment.this, dataset);
        } else {
            layoutManager = new GridLayoutManager(this.getActivity(), 2);
            adapter = new ElbbbirdCardRecentAdapter(ShotsFragment.this, dataset);
        }
        recycler.setHasFixedSize(false); //是否是静态列别，如果true，会启用内部优化
        recycler.setLayoutManager(layoutManager);

        adapter.setHasStableIds(false);
        recycler.setAdapter(adapter);

        if (dataset.size() == 0) {
            refresh.post(new Runnable() {
                @Override
                public void run() {
                    refresh.setRefreshing(true);
                }
            });
            getShots(page);
        }
    }

    private void getShots(int pageIndex) {
        DribbbleService.getShots(type, pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        if (1 == page)
                            dataset.clear();

                        for (Shot shot : shots) {
                            String url = shot.getImages().getHidpi();
                            if (TextUtils.isEmpty(url))
                                url = shot.getImages().getNormal();

                            if (!TextUtils.isEmpty(url) && (url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpg"))) {
                                dataset.add(shot);
                            }
                        }

                        adapter.notifyDataSetChanged();

                        refresh.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        refresh.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        getShots(page = 1);
    }

    public static class ElbbbirdCardPopularAdapter extends RecyclerView.Adapter<ElbbbirdCardPopularAdapter.ElbbbirdCardViewHolder> {

        private WeakReference<Fragment> mFragment;
        private List<Shot> mDataset;
        private Context mContext;

        public static class ElbbbirdCardViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.elbbbird_card)
            public CardView mCardView;
            @Bind(R.id.elbbbird_card_photo)
            public ImageView mImageView;
            @Bind(R.id.elbbbird_card_avatar)
            CircleImageView mAvatar;
            public ElbbbirdCardClickListener mElbbbirdCardClickListener;

            public ElbbbirdCardViewHolder(View itemView, ElbbbirdCardClickListener listener) {
                super(itemView);
                this.mElbbbirdCardClickListener = listener;
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.elbbbird_card)
            public void onClick() {
                mElbbbirdCardClickListener.onElbbbirdCardClick(getLayoutPosition());
            }

            public interface ElbbbirdCardClickListener {
                void onElbbbirdCardClick(int position);
            }
        }

        public ElbbbirdCardPopularAdapter(Fragment fragment, List<Shot> dataset) {
            mFragment = new WeakReference<>(fragment);
            this.mContext = fragment.getContext();
            this.mDataset = dataset;
        }

        @Override
        public ElbbbirdCardPopularAdapter.ElbbbirdCardViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            final Fragment fragment = mFragment.get();
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_elbbbird_card_popular, parent, false);

            return new ElbbbirdCardViewHolder(view, new ElbbbirdCardViewHolder.ElbbbirdCardClickListener() {
                @Override
                public void onElbbbirdCardClick(int position) {
//                    Intent intent = new Intent();
//                    intent.setClass(fragment.getActivity(), ShotDetailActivity.class);
//                    intent.putExtra("shot", mDataset.get(position));
//                    ImageView i = (ImageView) view.findViewById(R.id.elbbbird_card_photo);
//                    ActivityOptionsCompat options = ActivityOptionsCompat
//                            .makeSceneTransitionAnimation(fragment.getActivity(), i, "shot");
//                    ActivityCompat.startActivity(fragment.getActivity(), intent, options.toBundle());
                }
            });

        }

        @Override
        public void onBindViewHolder(final ElbbbirdCardPopularAdapter.ElbbbirdCardViewHolder holder, int position) {
            String imageUrl = mDataset.get(position).getImages().getHidpi();
            if (TextUtils.isEmpty(imageUrl))
                imageUrl = mDataset.get(position).getImages().getNormal();
            String avatar = mDataset.get(position).getUser().getAvatar_url();

            Glide.with(mContext)
                    .load(imageUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .thumbnail(0.99f)
                    .placeholder(R.drawable.dribbble_holder)
                    .crossFade(1500)
                    .override(800, 600)
                    .into(holder.mImageView);
            Glide.with(mContext)
                    .load(avatar)
                    .dontAnimate()
                    .placeholder(R.drawable.dribbble_holder)
                    .into(holder.mAvatar);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        @Override
        public long getItemId(int position) {
            return mDataset.get(position).getUuid();
        }
    }

    public static class ElbbbirdCardRecentAdapter extends RecyclerView.Adapter<ElbbbirdCardRecentAdapter.ElbbbirdCardViewHolder> {

        private WeakReference<Fragment> mFragment;
        private List<Shot> mDataset;
        private Context mContext;

        public static class ElbbbirdCardViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.elbbbird_card)
            public CardView mCardView;
            @Bind(R.id.elbbbird_card_photo)
            public ImageView mImageView;
            @Bind(R.id.elbbbird_card_views_count)
            public TextView mTvViewsCount;
            @Bind(R.id.elbbbird_card_comments_count)
            public TextView mTvCommentsCount;
            @Bind(R.id.elbbbird_card_likes_count)
            public TextView mTvLikesCount;
            public ElbbbirdCardClickListener mElbbbirdCardClickListener;

            public ElbbbirdCardViewHolder(View itemView, ElbbbirdCardClickListener listener) {
                super(itemView);
                this.mElbbbirdCardClickListener = listener;
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.elbbbird_card)
            public void onClick() {
                mElbbbirdCardClickListener.onElbbbirdCardClick(getLayoutPosition());
            }

            public interface ElbbbirdCardClickListener {
                void onElbbbirdCardClick(int position);
            }
        }

        public ElbbbirdCardRecentAdapter(Fragment fragment, List<Shot> dataset) {
            mFragment = new WeakReference<>(fragment);
            this.mContext = fragment.getContext();
            this.mDataset = dataset;
        }

        @Override
        public ElbbbirdCardRecentAdapter.ElbbbirdCardViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            final Fragment fragment = mFragment.get();
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_elbbbird_card_recent, parent, false);
            return new ElbbbirdCardViewHolder(view, new ElbbbirdCardViewHolder.ElbbbirdCardClickListener() {
                @Override
                public void onElbbbirdCardClick(int position) {
//                    Intent intent = new Intent();
//                    intent.setClass(fragment.getActivity(), ShotDetailActivity.class);
//                    intent.putExtra("shot", mDataset.get(position));
//                    ImageView i = (ImageView) view.findViewById(R.id.elbbbird_card_photo);
//                    ActivityOptionsCompat options = ActivityOptionsCompat
//                            .makeSceneTransitionAnimation(fragment.getActivity(), i, "shot");
//                    ActivityCompat.startActivity(fragment.getActivity(), intent, options.toBundle());
                }
            });

        }

        @Override
        public void onBindViewHolder(final ElbbbirdCardRecentAdapter.ElbbbirdCardViewHolder holder, int position) {
            String imageUrl = mDataset.get(position).getImages().getHidpi();
            if (TextUtils.isEmpty(imageUrl))
                imageUrl = mDataset.get(position).getImages().getNormal();

            holder.mTvViewsCount.setText(mDataset.get(position).getViews_count() + "");
            holder.mTvCommentsCount.setText(mDataset.get(position).getComments_count() + "");
            holder.mTvLikesCount.setText(mDataset.get(position).getLikes_count() + "");
            Glide.with(mContext)
                    .load(imageUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .thumbnail(0.99f)
                    .placeholder(R.drawable.dribbble_holder)
                    .crossFade(1500)
                    .override(800, 600)
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        @Override
        public long getItemId(int position) {
            return mDataset.get(position).getUuid();
        }
    }

}
