package com.example.newapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.newapps.adapter.SubKategoriAdapter;
import com.example.newapps.api.ApiRequest;
import com.example.newapps.api.Retroserver;
import com.example.newapps.model.ResponseSubKategori;
import com.example.newapps.model.SubKategori;
import com.example.newapps.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubKategoriActivity extends AppCompatActivity {

    ProgressDialog pd;
    private RecyclerView mRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private List<SubKategori> mItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_kategori);
        View parent_view = findViewById(R.id.parent_view);


        initToolbar();
        initContent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Entertain ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_1000);
    }

    private void initContent(){
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
        Call<ResponseSubKategori>getdata;

        getdata = Api.allSubKategori();
        getdata.enqueue(new Callback<ResponseSubKategori>() {
            @Override
            public void onResponse(Call<ResponseSubKategori> call, Response<ResponseSubKategori> response) {
                pd.dismiss();
                mItem = response.body().getData();
                mAdapter = new SubKategoriAdapter(SubKategoriActivity.this, mItem);
                mRecycler.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseSubKategori> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(SubKategoriActivity.this, "Data Tidak Di Temukan",Toast.LENGTH_SHORT);


            }
        });
    }
}