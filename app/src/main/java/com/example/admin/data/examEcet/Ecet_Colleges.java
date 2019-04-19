package com.example.admin.data.examEcet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.data.FastScroll.FastScrollRecyclerViewItemDecoration;
import com.example.admin.data.R;
import com.example.admin.data.examEcet.adapter.CollegeAdapter;
import com.example.admin.data.examEcet.model.CollegeDetails;
import com.example.admin.data.examEcet.rest.ApiClient;
import com.example.admin.data.examEcet.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecet_Colleges extends AppCompatActivity {
    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    public final static String MESSAGE_KEY ="colcode";
    CollegeAdapter adapter;
    List<CollegeDetails> colg;
    EditText t;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecet__colleges);

        t=findViewById(R.id.searchview);
        t.setVisibility(View.INVISIBLE);
        start();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SwipeRefreshLayout r;
        r=(SwipeRefreshLayout)findViewById(R.id.refreshcollege);
        r.setColorScheme(android.R.color.holo_orange_dark);
        r.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start();
                        r.setRefreshing(false);
                    }
                },10000);
            }
        });

    }
    public void start(){

        try {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            AlertDialog alert = alertDialog.create();
            alert.show();
            alert.setCancelable(false);
            alert.setContentView(R.layout.loadingdialog);

            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain your API KEY", Toast.LENGTH_LONG).show();
                return;
            }
            final RecyclerView recyclerVie = (RecyclerView) findViewById(R.id.recyclecollege);
            recyclerVie.setLayoutManager(new LinearLayoutManager(this));

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);

            Call<List<CollegeDetails>> call = apiService.getCollege(API_KEY);
            call.enqueue(new Callback<List<CollegeDetails>>() {
                @Override
                public void onResponse(Call<List<CollegeDetails>> call, Response<List<CollegeDetails>> response) {
                    colg = response.body();
                    t.setVisibility(View.VISIBLE);
                    alert.dismiss();
                    Collections.sort(colg);
                    HashMap<String, Integer> map = calculateIndexesForName(colg);
                    adapter = new CollegeAdapter(colg,map, R.layout.recyclecolleges, new CollegeAdapter.OnItemClick() {
                        @Override
                        public void onItem(CollegeDetails item) {
                           // Toast.makeText(getApplicationContext(), "Clicked  " + item.getCode(), Toast.LENGTH_SHORT).show();
                            Intent r = new Intent(getApplicationContext(), Ecet_Colleges_Details.class);
                            r.putExtra(MESSAGE_KEY, item.getCode());
                            startActivity(r);
                        }
                    }, getApplicationContext());
                    recyclerVie.setAdapter(adapter);

                    recyclerVie.setLayoutAnimation(controller);
                    adapter.notifyDataSetChanged();
                    recyclerVie.scheduleLayoutAnimation();

                    FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(getApplicationContext());
                    recyclerVie.addItemDecoration(decoration);
                    recyclerVie.setItemAnimator(new DefaultItemAnimator());
                }

                @Override
                public void onFailure(Call<List<CollegeDetails>> call, Throwable t) {
                    alert.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.ll), "No Network", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    start();
                                }
                            });
                    View snackbarView = snackbar.getView();
                    snackbarView.setMinimumHeight(25);
                    snackbarView.setBackgroundResource(R.color.orange);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_action);
                    textView.setTextColor(Color.BLACK);
                    TextView textView1=snackbarView.findViewById(R.id.snackbar_text);
                    textView1.setTextSize(25);
                    textView.setTextSize(15);
                    snackbar.show();
                }
            });
            setupsearch();
        }
        catch (Exception e){
            start();
        }
    }
    public void setupsearch(){

        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        List<CollegeDetails> filterdNames =new ArrayList<CollegeDetails>();

        //looping through existing elements
        for (CollegeDetails s : colg) {
            //if the existing elements contains the search input
            if (s.getName().contains(text.toUpperCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);

    }
    private HashMap<String, Integer> calculateIndexesForName(List<CollegeDetails> c){

        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i<c.size(); i++){
            String name = c.get(i).getName();
            System.out.println(name+"\n");
            String index = name.substring(0,1);
            System.out.println(index+"hello");
            index = index.toUpperCase();
            System.out.println("what in upper"+index);

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        System.out.println(mapIndex+"in map index");
        return mapIndex;
    }
}
