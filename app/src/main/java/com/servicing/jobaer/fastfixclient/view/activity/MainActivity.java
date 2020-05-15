package com.servicing.jobaer.fastfixclient.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.controller.adapter.BottomNavigationPageAdapter;
import com.servicing.jobaer.fastfixclient.controller.adapter.RequestListAdapter;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiClient;
import com.servicing.jobaer.fastfixclient.controller.retrofit.ApiInterface;
import com.servicing.jobaer.fastfixclient.model.SearchResponseModel;
import com.servicing.jobaer.fastfixclient.model.requests.RequestModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    EditText searchText;
    TextView closeSearch;
    RecyclerView searchRecycler;
    private RequestListAdapter requestListAdapter;
    private ArrayList<RequestModel> requestModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

    }

    private void initUi() {
        viewPager=findViewById(R.id.viewPager);
        closeSearch = findViewById(R.id.closeSearch);
        searchText = findViewById(R.id.searchText);
        BottomNavigationPageAdapter bottomNavigationPageAdapter=new BottomNavigationPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(bottomNavigationPageAdapter);
        BottomNavigationView bottomNavigation=findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (item.getItemId() == R.id.about_us) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (item.getItemId() == R.id.contact_us) {
                    viewPager.setCurrentItem(2);
                    return true;
                } else if (item.getItemId() == R.id.services) {
                    viewPager.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                searchText.setFocusableInTouchMode(true);

                return false;
            }
        });
        searchRecycler = findViewById(R.id.searchRecycler);
        requestListAdapter = new RequestListAdapter(this, requestModels);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setAdapter(requestListAdapter);
        ImageView searchButton=findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
                searchText.setFocusable(false);
                String text=searchText.getText().toString();
                if (text.isEmpty()) {
                    hideSearch();
                    showCards();
                } else {
                    getSearchResult(text);
                }
            }
        });

        closeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearch();
                showCards();
                searchText.setText("");
            }
        });
    }
    private void getSearchResult(String q) {
        ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getString(R.string.searching));
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchResponseModel> call = apiInterface.getSearchResult(q);
        call.enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                progressDialog.dismiss();
                SearchResponseModel searchResponseModel = response.body();
                if (searchResponseModel != null) {
                    requestModels.clear();
                    if (searchResponseModel.getData().size() > 0) {
                        requestModels.addAll(searchResponseModel.getData());
                        requestListAdapter.notifyDataSetChanged();
                        hideCards();
                        showSearch();
                    } else {
                        hideSearch();
                        showCards();
                        Toast.makeText(MainActivity.this, getString(R.string.nothing_found), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                showCards();
                hideSearch();
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hideCards() {

        viewPager.setVisibility(View.GONE);
    }

    private void showCards() {
        viewPager.setVisibility(View.VISIBLE);
    }

    private void showSearch() {
        searchRecycler.setVisibility(View.VISIBLE);
        closeSearch.setVisibility(View.VISIBLE);
    }

    private void hideSearch() {
        searchRecycler.setVisibility(View.GONE);
        closeSearch.setVisibility(View.GONE);
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
