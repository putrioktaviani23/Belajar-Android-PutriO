package com.example.newapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.newapps.adapter.BeritaAdapter;
import com.example.newapps.adapter.SubKategoriAdapter;
import com.example.newapps.api.ApiRequest;
import com.example.newapps.api.Retroserver;
import com.example.newapps.model.Berita;
import com.example.newapps.model.ResponseBerita;
import com.example.newapps.model.ResponseSubKategori;
import com.example.newapps.model.SubKategori;
import com.example.newapps.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    ProgressDialog pd;
    private RecyclerView mRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private List<Berita> mItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initToolbar();
        initContent();

    }

    private void initToolbar() {
        Intent data = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data.getStringExtra("nama_sub_kategori"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_1000);
    }

    private  void initContent(){
        Intent data = getIntent();

        pd = new ProgressDialog(this);
        mRecycler = findViewById(R.id.recyclerView);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);

        pd.setMessage("Loading Ndor...");
        pd.setCancelable(false);
        pd.show();

        ApiRequest Api = Retroserver.getClient().create(ApiRequest.class);
        Call<ResponseBerita> getdata = Api. showBeritaSubKategori(data.getStringExtra("id_sub_kategori"));
        getdata.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                pd.dismiss();
                mItem= response.body().getData();
                mAdapter = new BeritaAdapter(NewsActivity.this, mItem);
                mRecycler.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {

                pd.dismiss();
                Toast.makeText(NewsActivity.this, "Data Tidak Di Temukan",Toast.LENGTH_SHORT);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchs, menu);
        Tools.changeMenuIconColor(menu, Color.WHITE);
        return true;
    }





}