package com.majedalmoqbeli.newsapp.ui.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.majedalmoqbeli.newsapp.R;
import com.majedalmoqbeli.newsapp.controller.SharedPreferencesNews;
import com.majedalmoqbeli.newsapp.models.NewsData;

import java.util.ArrayList;

import static com.majedalmoqbeli.newsapp.helper.Function.showToast;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            ArrayList<NewsData> newsData = SharedPreferencesNews.getInstance(getActivity()).LoadData();
            if (newsData != null) {
                if (newsData.size() > 0) {
                    setMarker(newsData, googleMap);
                } else {
                    showToast(getActivity(), getString(R.string.there_is_no_data));
                }
            } else {
                showToast(getActivity(), getString(R.string.there_is_no_data));
            }

        }

        /**
         * this to set marker of news
         * @param newsData : this is data of news comes from local
         * @param mMap : my GoogleMap object
         */
        private void setMarker(ArrayList<NewsData> newsData, GoogleMap mMap) {
            for (int i = 0; i < newsData.size(); i++) {
                LatLng latLng = new LatLng(newsData.get(i).getNews_lat(), newsData.get(i).getNews_lng());
                mMap.addMarker(new MarkerOptions().position(latLng).title(newsData.get(i).getNews_title()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(newsData.get(i).getNews_lat(), newsData.get(i).getNews_lng()), 15.0f));
            }

        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}