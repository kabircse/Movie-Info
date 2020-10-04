package com.emranul.movieinfo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emranul.movieinfo.R;
import com.emranul.movieinfo.adapter.AdapterSlider;
import com.emranul.movieinfo.adapter.AdapterCategories;
import com.emranul.movieinfo.adapter.AdapterPlaying;
import com.emranul.movieinfo.adapter.AdapterPopular;
import com.emranul.movieinfo.api.ApiClint;
import com.emranul.movieinfo.api.ApiServices;
import com.emranul.movieinfo.model.CategoriesGenres;
import com.emranul.movieinfo.model.CategoriesPlaying;
import com.emranul.movieinfo.model.CategoriesPopular;
import com.emranul.movieinfo.model.Genres;
import com.emranul.movieinfo.model.Results;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emranul.movieinfo.Constant.API_KEY;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ApiServices apiServices = ApiClint.getRetrofit().create(ApiServices.class);

        recyclerView = view.findViewById(R.id.rv_categories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager.getReverseLayout();
        recyclerView.setLayoutManager(linearLayoutManager);


        Call<CategoriesGenres> genresCall = apiServices.getGenres(API_KEY);
        genresCall.enqueue(new Callback<CategoriesGenres>() {
            @Override
            public void onResponse(Call<CategoriesGenres> call, Response<CategoriesGenres> response) {
                if (response.isSuccessful()) {
                    List<Genres> genres = response.body().getGenres();
                    genres.add(new Genres("Recommends", -1));
                    AdapterCategories adapter = new AdapterCategories(genres);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<CategoriesGenres> call, Throwable t) {
                Toast.makeText(getContext(), "Internet Connection Problem", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });


        RecyclerView popularRecycler = view.findViewById(R.id.popular_recycler);
        popularRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


        Call<CategoriesPopular> popularCall = apiServices.getPopular(API_KEY);
        popularCall.enqueue(new Callback<CategoriesPopular>() {
            @Override
            public void onResponse(Call<CategoriesPopular> call, Response<CategoriesPopular> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    AdapterPopular adapterPopular = new AdapterPopular(response.body().getResults());
                    popularRecycler.setAdapter(adapterPopular);
                    adapterPopular.notifyDataSetChanged();


                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesPopular> call, Throwable t) {
                Toast.makeText(getContext(), "Faild.."+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });


        RecyclerView playingRecycler = view.findViewById(R.id.playing_recycler);
        playingRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


//        List<Results> list = new ArrayList<>();
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));
//        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf"));


        Call<CategoriesPopular> topRatedCalling = apiServices.getTopRated(API_KEY);
        topRatedCalling.enqueue(new Callback<CategoriesPopular>() {
            @Override
            public void onResponse(Call<CategoriesPopular> call, Response<CategoriesPopular> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Loading playing", Toast.LENGTH_SHORT).show();
                    AdapterPlaying playing = new AdapterPlaying(response.body().getResults());
                    playingRecycler.setAdapter(playing);
                    playing.notifyDataSetChanged();

                }else {
                    Toast.makeText(getContext(), "Playing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesPopular> call, Throwable t) {
                Toast.makeText(getContext(),"Internet Error Playing", Toast.LENGTH_SHORT).show();
            }
        });


        //Slider part started:

        viewPager2 = view.findViewById(R.id.viewpager2);


        List<Results> list = new ArrayList<>();
        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf","",""));
        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf","",""));
        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf","",""));
        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf","",""));
        list.add(new Results(1, "TItle", "Original", 1, "wef", "sdf","",""));

        //viewPager2.setAdapter(new SliderAdapter(list,viewPager2));

        Call<CategoriesPlaying> playingCall = apiServices.getPlaying(API_KEY);
        playingCall.enqueue(new Callback<CategoriesPlaying>() {
            @Override
            public void onResponse(Call<CategoriesPlaying> call, Response<CategoriesPlaying> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Loading playing", Toast.LENGTH_SHORT).show();
                    AdapterSlider adapterSlider = new AdapterSlider(response.body().getResults(), viewPager2);
                    viewPager2.setAdapter(adapterSlider);

                } else {
                    Toast.makeText(getContext(), "Fiald playing", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onResponse: "+response.message());

                }
            }

            @Override
            public void onFailure(Call<CategoriesPlaying> call, Throwable t) {
                Toast.makeText(getContext(), "Internet Problem playing", Toast.LENGTH_SHORT).show();
            }
        });


        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float x = 1 - Math.abs(position);
                page.setScaleY(0.8f + x * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 4000);
            }
        });

        return view;
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

}