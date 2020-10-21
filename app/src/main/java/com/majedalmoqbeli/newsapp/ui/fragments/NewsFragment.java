package com.majedalmoqbeli.newsapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.newsapp.R;
import com.majedalmoqbeli.newsapp.adapters.ShowNewsAdapter;
import com.majedalmoqbeli.newsapp.constants.ApiErrorCode;
import com.majedalmoqbeli.newsapp.constants.ErrorCode;
import com.majedalmoqbeli.newsapp.controller.SaveSetting;
import com.majedalmoqbeli.newsapp.controller.SharedPreferencesNews;
import com.majedalmoqbeli.newsapp.models.NewsData;
import com.majedalmoqbeli.newsapp.ui.activities.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.majedalmoqbeli.newsapp.helper.Function.checkErrorType;
import static com.majedalmoqbeli.newsapp.helper.Function.isNetworkAvailable;
import static com.majedalmoqbeli.newsapp.helper.Function.setViewGone;
import static com.majedalmoqbeli.newsapp.helper.Function.setViewVisible;
import static com.majedalmoqbeli.newsapp.helper.Function.showSnackBar;


public class NewsFragment extends Fragment {


    private ArrayList<NewsData> newsData;
    private boolean stopScrollListener = false;
    private boolean isLoading = false;
    private RecyclerView mRecyclerView;
    private AVLoadingIndicatorView mProgress;
    private AVLoadingIndicatorView mProgressGetMore;
    private ShowNewsAdapter adapter;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        return intiView(inflater.inflate(R.layout.fragment_news, container, false));
    }


    private View intiView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mProgress = view.findViewById(R.id.avi);
        mProgressGetMore = view.findViewById(R.id.progress_get_more);

        newsData = new ArrayList<>();
        setViewVisible(mProgress);
        getNews();


        return view;
    }

    /**
     * to connect with server side and get the json data
     */
    private void getNews() {
        String url = SaveSetting.ServerURL + "getNews.php";


        if (isNetworkAvailable()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        setViewGone(mProgress);
                        Log.i("Response :", response);
                        parseResponseData(response);
                    }, error -> {
                Log.i("ResponseERROE :", error.toString());

                setViewGone(mProgress);

                if (checkErrorType(error).equals(ErrorCode.CONNECTION_TIMEOUT) ||
                        checkErrorType(error).equals(ErrorCode.IS_NOT_NETWORK)
                ) {
                    setOfflineMode();
                } else
                    showSnackBar(getActivity(), mRecyclerView, getString(R.string.error, checkErrorType(error)));
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();

                    if (newsData.size() > 0) {
                        map.put("last_id", newsData.get(newsData.size() - 1).getNews_id());
                    } else {
                        map.put("last_id", "0");
                    }
                    return map;
                }

            };

            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                    , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } else {
            setViewGone(mProgress);
            setOfflineMode();
        }

    }


    /**
     * get more data if the user scrolled to the last item
     */
    private void initScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!stopScrollListener) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (!isLoading) {
                        if (linearLayoutManager != null &&
                                linearLayoutManager.findLastCompletelyVisibleItemPosition() ==
                                        newsData.size() - 1) {
                            mProgressGetMore.setVisibility(View.VISIBLE);
                            getNews();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    /**
     * for parse data and save it in arrayList object
     *
     * @param response : this is JSON data comes from  server
     */
    private void parseResponseData(String response) {
        try {
            JSONObject object = new JSONObject(response);
            if (object.getInt("status_code") == ApiErrorCode.STATUS_OK) {
                JSONArray array = object.getJSONArray("news");
                int i = 0;
                if (array.length() > 0) {
                    while (i < array.length()) {
                        JSONObject p = array.getJSONObject(i);

                        newsData.add(
                                new NewsData(
                                        p.getString("news_id"),
                                        p.getString("news_title"),
                                        p.getString("news_info"),
                                        p.getString("news_lat"),
                                        p.getString("news_lng"),
                                        p.getString("news_date"),
                                        p.getString("news_image"),
                                        p.getString("user_id"),
                                        p.getString("u_name"),
                                        p.getString("u_image")));
                        i++;
                    }
                } else {
                    setViewGone(mProgressGetMore);
                    stopScrollListener = true;
                }


                if (newsData.size() > 0) {
                    if (isLoading) {
                        setViewGone(mProgressGetMore);
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        saveOfflineMode();
                    } else {
                        saveOfflineMode();
                        setRecyclerView(mRecyclerView, newsData);
                    }

                } else {
                    showSnackBar(getActivity(), mRecyclerView, getString(R.string.there_is_no_data));
                }

            } else if (object.getInt("status_code") == ApiErrorCode.STATUS_NOT_HAVE_PERMISSION
                    || object.getInt("status_code") == ApiErrorCode.STATUS_NOT_ACCEPTABLE
            ) {
                showSnackBar(getActivity(), mRecyclerView, getString(R.string.error, ErrorCode.NOT_HAVE_PERMISSION));
            } else if (object.getInt("status_code") == ApiErrorCode.STATUS_FAILED_GET_DATA) {
                showSnackBar(getActivity(), mRecyclerView, getString(R.string.error, ErrorCode.DATA_BASE_ERROR));
            }

        } catch (JSONException e) {
            Log.i("JSONExceptionRequest :", e.toString());
            showSnackBar(getActivity(), mRecyclerView, getString(R.string.error, ErrorCode.PARSE_DATA_ERROR));
        }

    }

    /**
     * thi function well call if there is no internet connection
     */
    private void showTryAgainMessage() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(getString(R.string.internetError));
        builder1.setIcon(R.drawable.ic_no_network);
        builder1.setCancelable(false);
        builder1.setPositiveButton(getString(R.string.replay), (dialog, which) -> {
            setViewVisible(mProgress);
            getNews();
        });
        builder1.setNeutralButton(getString(R.string.exit), (dialog, which) -> {
            MainActivity.APP.finish();
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * this is for call adapter and set it in recyclerView
     *
     * @param recyclerView :
     * @param data         : the arrayList data after parse json data
     */
    private void setRecyclerView(RecyclerView recyclerView, ArrayList<NewsData> data) {
        recyclerView.removeAllViews();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShowNewsAdapter(context, data);
        recyclerView.setAdapter(adapter);
        initScrollListener();
    }

    private void saveOfflineMode() {
        SharedPreferencesNews.getInstance(context).SaveData(newsData);
    }

    private void setOfflineMode() {
        newsData = SharedPreferencesNews.getInstance(context).LoadData();
        if (newsData != null) {
            if (newsData.size() > 0) {
                setRecyclerView(mRecyclerView, newsData);
            } else {
                showTryAgainMessage();
            }
        } else {
            showTryAgainMessage();
        }
    }

}