package com.elbbbird.android.elbbbird.service;

import com.elbbbird.android.elbbbird.models.Shot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Created by zhanghailong-ms on 2015/6/17.
 */
public class DribbbleService {
    private static final String DRIBBBLE_API_URL = "https://api.dribbble.com";

    private static final String LOCAL_TOKEN = "c0315158a8000f6267d981a96f9be06a0e3df4f222674e56bbaa78f839ecfe88";
    private static final String OPTIONS_PER_PAGE = "25";
    public static final String SORT_BY_POPULARITY = "popularity";
    public static final String SORT_BY_RECENT = "recent";
    public static final String LIST_ANY = "any";
    public static final String LIST_TEAMS = "teams";
    public static final String LIST_DEBUTS = "debuts";
    public static final String LIST_PLAYOFFS = "playoffs";
    public static final String LIST_REBOUNDS = "rebounds";

    public static final int TYPE_SHOTS_POPULAR = 0;
    public static final int TYPE_SHOTS_RECENT = 1;
    public static final int TYPE_TEAMS_POPULAR = 3;
    public static final int TYPE_TEAMS_RECENT = 4;
    public static final int TYPE_DEBUTS_POPULAR = 6;
    public static final int TYPE_DEBUTS_RECENT = 7;
    public static final int TYPE_PLAYOFFS_POPULAR = 9;
    public static final int TYPE_PLAYOFFS_RECENT = 10;
    public static final int TYPE_REBOUNDS_POPULAR = 12;
    public static final int TYPE_REBOUNDS_RECENT = 13;

    interface Dribbble {

        @GET("/v1/shots")
        Observable<List<Shot>> getAnyShots(@QueryMap Map<String, String> options);

    }

    public static Observable<List<Shot>> getShots(int type, int page) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DRIBBBLE_API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        Dribbble dribbble = retrofit.create(Dribbble.class);

        String sort, list;
        if (TYPE_SHOTS_POPULAR == type || TYPE_TEAMS_POPULAR == type || TYPE_DEBUTS_POPULAR == type ||
                TYPE_PLAYOFFS_POPULAR == type || TYPE_REBOUNDS_POPULAR == type)
            sort = SORT_BY_POPULARITY;
        else
            sort = SORT_BY_RECENT;

        switch (type) {
            case TYPE_SHOTS_POPULAR:
            case TYPE_SHOTS_RECENT:
                list = LIST_ANY;
                break;
            case TYPE_TEAMS_POPULAR:
            case TYPE_TEAMS_RECENT:
                list = LIST_TEAMS;
                break;
            case TYPE_DEBUTS_POPULAR:
            case TYPE_DEBUTS_RECENT:
                list = LIST_DEBUTS;
                break;
            case TYPE_PLAYOFFS_POPULAR:
            case TYPE_PLAYOFFS_RECENT:
                list = LIST_PLAYOFFS;
                break;
            case TYPE_REBOUNDS_POPULAR:
            case TYPE_REBOUNDS_RECENT:
                list = LIST_REBOUNDS;
                break;
            default:
                list = LIST_ANY;
                break;
        }

        Map<String, String> options = new HashMap<>();
        options.put("access_token", LOCAL_TOKEN);
        options.put("per_page", OPTIONS_PER_PAGE);
        options.put("sort", sort);
        options.put("list", list);
        options.put("page", String.valueOf(page));
        return dribbble.getAnyShots(options);
    }

}
