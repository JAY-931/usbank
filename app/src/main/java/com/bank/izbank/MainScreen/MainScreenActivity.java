package com.bank.izbank.MainScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bank.izbank.Adapters.CryptoPostAdapter;
import com.bank.izbank.MainScreen.FinanceScreen.CryptoModel;
import com.bank.izbank.MainScreen.FinanceScreen.FinanceFragment;
import com.bank.izbank.R;
import com.bank.izbank.Sign.SignIn;
import com.bank.izbank.service.ICryptoAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainScreenActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    final Fragment fragment1 = new Fragment1();
    final Fragment fragment2 = new Fragment2();
    final Fragment fragment4 = new Fragment4();
    final Fragment fragment5 = new Fragment5();

    private Fragment tempFragment=fragment1;

    final FragmentManager fm = getSupportFragmentManager();

    private ArrayList<CryptoModel> cryptoModels;
    private final String BASE_URL = "https://api.nomics.com/v1/";
    private Retrofit retrofit;
    private ImageView downloadingImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Retrofit & JSON
        Gson gson=new GsonBuilder().setLenient().create();

        retrofit=new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
        loadData();
        //Retrofit end

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        fm.beginTransaction().add(R.id.fragment_container,fragment5,"5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment4,"4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment2,"2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1,"1").commit();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu1:
                        fm.beginTransaction().hide(tempFragment).show(fragment1).commit();
                        tempFragment = fragment1;
                        break;
                    case R.id.menu2:
                        fm.beginTransaction().hide(tempFragment).show(fragment2).commit();
                        tempFragment = fragment2;
                        break;
                    case R.id.menu3:
                        getSupportFragmentManager().beginTransaction().hide(tempFragment).add(R.id.fragment_container,tempFragment=new FinanceFragment(cryptoModels)).show(tempFragment).commit();
                        break;
                    case R.id.menu4:
                        fm.beginTransaction().hide(tempFragment).show(fragment4).commit();
                        tempFragment = fragment4;
                        break;
                    case R.id.menu5:
                        fm.beginTransaction().hide(tempFragment).show(fragment5).commit();
                        tempFragment = fragment5;
                        break;

                }


                return true;
            }
        });


    }


    private void loadData(){
        ICryptoAPI cryptoAPI=retrofit.create(ICryptoAPI.class);
        Call<List<CryptoModel>> call=cryptoAPI.getData();
        call.enqueue(new Callback<List<CryptoModel>>() {
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                if(response.isSuccessful()){
                    List<CryptoModel> responseList=response.body();
                    cryptoModels=new ArrayList<>(responseList);


                }
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),  t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void logOut(View view){
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e !=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(getApplicationContext(), SignIn.class);
                    startActivity(intent);
                }
            }
        });
    }
}