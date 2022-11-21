package com.example.paginationdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<MainData> arrayList;
    private MyAdapter myAdapter;

    private int page = 1, limit = 10;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.nested_scrollView);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        myAdapter = new MyAdapter(arrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        getData(page, limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    //when reached last item position
                    //increase page size
                    page++;
                    //show progress bar.
                    progressBar.setVisibility(View.VISIBLE);
                    //call getData method
                    getData(page, limit);
                }
            }
        });
    }

    private void getData(int page, int limit) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://picsum.photos/")
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.stringCall(page, limit);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null){
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MainData mainData = new MainData();
                            mainData.setImage(jsonObject.getString("download_url"));
                            mainData.setAuthorName(jsonObject.getString("author"));
                            arrayList.add(mainData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myAdapter = new MyAdapter(arrayList, MainActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(myAdapter);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}