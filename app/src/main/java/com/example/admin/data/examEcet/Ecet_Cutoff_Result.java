package com.example.admin.data.examEcet;



import android.content.Intent;

import android.graphics.Color;

import android.os.Handler;

import com.google.android.material.snackbar.Snackbar;



import androidx.cardview.widget.CardView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;

import android.text.TextWatcher;

import android.util.Log;

import android.view.View;

import android.view.animation.AnimationUtils;

import android.view.animation.LayoutAnimationController;

import android.widget.AdapterView;

import android.widget.ArrayAdapter;

import android.widget.EditText;

import android.widget.LinearLayout;

import android.widget.ProgressBar;

import android.widget.Spinner;

import android.widget.TextView;

import android.widget.Toast;



import com.example.admin.data.R;

import com.example.admin.data.examEcet.adapter.CutoffAdapter;

import com.example.admin.data.examEcet.rest.ApiClient;

import com.example.admin.data.examEcet.rest.ApiInterface;

import com.example.admin.data.examPgecet.model.CollegeBranchDetails;



import java.util.ArrayList;

import java.util.Arrays;

import java.util.Collections;

import java.util.List;



import retrofit2.Call;

import retrofit2.Callback;

import retrofit2.Response;



public class Ecet_Cutoff_Result extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";

    List<CollegeBranchDetails> list;

    String gender,caste,rank,council,sortby;

    String q;

    String cas;

    Spinner branch;

    public final static String MESSAGE_KEY ="colcode";

    EditText t;

    CutoffAdapter cutoffAdapter;

    String branchselected;

    List<String> coucillist;

    LinearLayout ll;

    String[] c,d,all;

    SwipeRefreshLayout refreshLayout;

    ProgressBar p;

    CardView ft;

    TextView retry;

    public void refresh(){





        refreshLayout.setColorSchemeResources(R.color.cutdown);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        start(branchselected);

                        refreshLayout.setRefreshing(false);

                    }

                },2000);

            }

        });

    }

    @Override

    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ecet__cutoff__result);



        Intent i=getIntent();

        gender=i.getStringExtra("gender");

        caste=i.getStringExtra("caste");

        rank=i.getStringExtra("rank");

        council=i.getStringExtra("council");



        sortby=i.getStringExtra("sortby");

        d =i.getStringArrayExtra("selectedItems");

        c=i.getStringArrayExtra("selectedColleges");



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        t=(EditText)findViewById(R.id.search) ;

        t.setVisibility(View.INVISIBLE);



        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshcutoff);



        refreshLayout.setVisibility(View.INVISIBLE);

        p=findViewById(R.id.progress);

        ft=findViewById(R.id.ft);

        retry=findViewById(R.id.retry);



        ll=findViewById(R.id.ll);

        ll.setVisibility(View.INVISIBLE);

        retry.setVisibility(View.INVISIBLE);

        branch=(Spinner)findViewById(R.id.branchesdata);

        branch.setVisibility(View.INVISIBLE);



        all=new String[d.length+1];

        all[0]="ALL";





        for (int j=0;j<d.length;j++){

            System.out.print(d[j]);

            all[j+1]=d[j];

        }



        coucillist = Arrays.asList(all);

        branchselected=coucillist.get(0);

        //Toast.makeText(this, "Branchselected  :"+branchselected, Toast.LENGTH_SHORT).show();

        ArrayAdapter<String> counada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coucillist);

        counada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        branch.setAdapter(counada);



        refresh();



        switch (caste){

            case "OC":

                if(gender.equals("M")){

                    cas="oc_m_max";

                }

                else{

                    cas="oc_f_max";

                }

                break;

            case "BC_A":

                if(gender.equals("M")){

                    cas="bc_a_m_max";

                }

                else{

                    cas="bc_a_f_max";

                }

                break;

            case "BC_B":

                if(gender.equals("M")){

                    cas="bc_b_m_max";

                }

                else{

                    cas="bc_b_f_max";

                }

                break;

            case "BC_C":

                if(gender.equals("M")){

                    cas="bc_c_m_max";



                }

                else{

                    cas="bc_c_f_max";



                }

                break;

            case "BC_D":

                if(gender.equals("M")){

                    cas="bc_d_m_max";



                }

                else{

                    cas="bc_d_f_max";



                }

                break;

            case "BC_E":

                if(gender.equals("M")){

                    cas="bc_e_m_max";



                }

                else{

                    cas="bc_e_f_max";



                }

                break;

            case "SC":

                if(gender.equals("M")){

                    cas="sc_m_max";



                }

                else{

                    cas="sc_f_max";



                }

                break;

            case "ST":

                if(gender.equals("M")){

                    cas="st_m_max";



                }

                else{

                    cas="sc_f_max";



                }

                break;

        }



        branch.setOnItemSelectedListener(this);

        // start(branchselected);

    }

    void start(String branchselected){



        try {



            p.setVisibility(View.VISIBLE);



            refreshLayout.setVisibility(View.INVISIBLE);

            retry.setVisibility(View.INVISIBLE);





            if (c.length == 0) {



                if (branchselected=="ALL"){



                    q = "{$or:[";

                    for (int i = 0; i < d.length; i++) {



                        if (i != d.length - 1) {

                            q = q + "{" + cas + ":{$gte:" + rank + "},branch:'" + d[i] + "'},{" + cas + ":'NA',branch:'" + d[i] + "'},";

                        } else {

                            q = q + "{" + cas + ":{$gte:" + rank + "},branch:'" + d[i] + "'},{" + cas + ":'NA',branch:'" + d[i] + "'}]}";

                        }

                    }

                }

               /*else{

                    q = "{$or: [{" + cas + " :{$gte:" + rank + "},branch:'" + branchselected + "'},{" + cas + ":'NA',branch:'" + branchselected + "'}]}";

                }*/

            } else {

                if(branchselected.equals("ALL")){

                    q = "{$or:[";

                    for (int i = 0; i < c.length; i++) {



                        for (int j = 0; j < d.length; j++) {

                            if (i != c.length - 1) {

                                q = q + "{" + cas + ":{$gte:" + rank + "},branch:'" + d[j]+ "',colcode:'" + c[i] + "'},{" + cas + ":'NA',branch:'" + d[j] + "',colcode:'" + c[i] + "'},";

                            } else {

                                q = q + "{" + cas + ":{$gte:" + rank + "},branch:'" + d[j] + "',colcode:'" + c[i] + "'},{" + cas + ":'NA',branch:'" + d[j] + "',colcode:'" + c[i] + "'},";

                            }

                        }

                    }

                    q= q.substring(0,q.length()-1);
                    q=q+"]}";

                }

               /* else {

                    q = "{$or:[";

                    for (int i = 0; i < c.length; i++) {



                        if (i != c.length - 1) {

                            q = q + "{" + cas + ":{$gte:" + rank + "},branch:'" + branchselected + "',code:'" + c[i] + "'},{" + cas + ":'NA',branch:'" + branchselected + "',code:'" + c[i] + "'},";

                        } else {

                            q = q + "{" + cas + ":{$gte:" + rank + "},branch:'" + branchselected + "',code:'" + c[i] + "'},{" + cas + ":'NA',branch:'" + branchselected + "',code:'" + c[i] + "'}]}";

                        }

                    }

                }*/

            }

            // Toast.makeText(this, gender+rank+council+caste+q, Toast.LENGTH_LONG).show();

            Log.i("\n\n\n\nQuery\n\n\n\n", q);

            // Toast.makeText(getApplicationContext(), q, Toast.LENGTH_SHORT).show();



            final RecyclerView recycle = (RecyclerView) findViewById(R.id.recyclecutoff);

            recycle.setLayoutManager(new LinearLayoutManager(this));



            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            final LayoutAnimationController controller =

                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);

            Call<List<CollegeBranchDetails>> call;

            if (council.contains("counselling 1")) {

                call = apiService.getData1(q);

            } else {

                call = apiService.getData2(q);

            }





            call.enqueue(new Callback<List<CollegeBranchDetails>>() {

                @Override

                public void onResponse(Call<List<CollegeBranchDetails>> call, Response<List<CollegeBranchDetails>> response) {

                    list = response.body();

                    // Toast.makeText(getApplicationContext(), "Branch: "+list.get(0).getBranch(), Toast.LENGTH_SHORT).show();

                    t.setVisibility(View.VISIBLE);

                    branch.setVisibility(View.VISIBLE);

                    t.setVisibility(View.VISIBLE);

                    branch.setVisibility(View.VISIBLE);



                    ll.setVisibility(View.VISIBLE);



                    p.setVisibility(View.INVISIBLE);

                    refreshLayout.setVisibility(View.VISIBLE);



                    if (sortby.equals("yes")){

                        try {
                            Collections.sort(list);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }





                    cutoffAdapter = new CutoffAdapter(list, R.layout.recycle_cutoff, new CutoffAdapter.OnItemClick() {

                        @Override

                        public void onItem(CollegeBranchDetails item) {

                            // Toast.makeText(Eamcet_Colleges.this, "Clicked "+item.getCode(), Toast.LENGTH_SHORT).show();

                            Intent r = new Intent(Ecet_Cutoff_Result.this, Ecet_Colleges_Details.class);

                            r.putExtra(MESSAGE_KEY, item.getColcode());

                            startActivity(r);

                        }

                    }, getApplicationContext());

                    recycle.setAdapter(cutoffAdapter);



                    recycle.setLayoutAnimation(controller);

                    cutoffAdapter.notifyDataSetChanged();

                    recycle.scheduleLayoutAnimation();

                    // Log.i("Count", String.valueOf(list.size()));

                    if (list.isEmpty()) {

                        retry.setText("No Colleges Found For Your Rank\nPlease Select Another Branch");

                        retry.setVisibility(View.VISIBLE);

                        recycle.setVisibility(View.INVISIBLE);

                        Toast.makeText(getApplicationContext(), "\t\t\tNo Colleges Found\nPlease Select Another Branch", Toast.LENGTH_LONG).show();

                    }

                }



                @Override

                public void onFailure(Call<List<CollegeBranchDetails>> call, Throwable t) {

                    retry.setVisibility(View.VISIBLE);

                    p.setVisibility(View.INVISIBLE);

                    ll.setVisibility(View.INVISIBLE);

                    refreshLayout.setVisibility(View.VISIBLE);

                }

            });

            setupsearch();

        }

        catch (Exception e){

            start(branchselected);

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

        //new array list that will hold the filtered data90

        List<CollegeBranchDetails> filterdNames =new ArrayList<CollegeBranchDetails>();



        //looping through existing elements

        for (CollegeBranchDetails s : list) {

            //if the existing elements contains the search input

            if (s.getColcode().contains(text.toUpperCase())||s.getColcode().contains(text.toUpperCase())) {

                //adding the element to filtered list

                filterdNames.add(s);

            }

        }

        //calling a method of the adapter class and passing the filtered list

        cutoffAdapter.filterList(filterdNames);

    }



    @Override

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        branchselected=adapterView.getItemAtPosition(i).toString();



        //Toast.makeText(Eamcet_Cutoff_Result.this, "Swipe Down to Refresh", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override

            public void run() {



                start(branchselected);

            }

        },5000);



    }



    @Override

    public void onNothingSelected(AdapterView<?> adapterView) {

        //  branchselected=adapterView.getItemAtPosition(0).toString().substring(0,3);

    }

}