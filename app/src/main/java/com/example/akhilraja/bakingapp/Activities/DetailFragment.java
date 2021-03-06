package com.example.akhilraja.bakingapp.Activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akhilraja.bakingapp.Adapter.DetailAdapter;
import com.example.akhilraja.bakingapp.Model.BakingModel;
import com.example.akhilraja.bakingapp.Model.Ingredient;
import com.example.akhilraja.bakingapp.Model.Step;
import com.example.akhilraja.bakingapp.R;
import com.example.akhilraja.bakingapp.Rest.ApiClient;
import com.example.akhilraja.bakingapp.Rest.ApiInterface;
import com.example.akhilraja.bakingapp.Widget.WidgetActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AkhilRaja on 21/06/17.
 */

public class DetailFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private BakingModel bakingModel;


    @Nullable@BindView(R.id.ingredient)
    TextView textView_ing;
    @Nullable@BindView(R.id.ingredient_list)
    TextView editText_inglist;

    ActionBar actionBar;
    List<BakingModel> bakingModels = new ArrayList<>();

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public  DetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);


        ButterKnife.bind(this,view);

        actionBar = getActivity().getActionBar();
        if(actionBar!=null)
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        pref = view.getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        if(getActivity().getIntent().getParcelableExtra("Model") == null && this.getArguments() != null)
            bakingModel = this.getArguments().getParcelable("Model");
        else
        bakingModel= getActivity().getIntent().getParcelableExtra("Model");

        if(bakingModel == null)
        {
            Gson gson = new Gson();
            String json = pref.getString("MyObject", "");
            BakingModel obj = gson.fromJson(json, BakingModel.class);
            bakingModel = obj;
        }


        Gson gson = new Gson();
        String json = gson.toJson(bakingModel);
        editor.putString("MyObject", json);
        editor.commit();


        int i = 0;
        textView_ing.setText(R.string.ingredients);

        if(bakingModel!=null) {
            for (Ingredient ingredient : bakingModel.getIngredients()) {
                i++;
                editText_inglist.append(i + ".  " + ingredient.getIngredient() + "\n");
            }

            adapter = new DetailAdapter(getContext(), bakingModel);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        return view;
    }

}
