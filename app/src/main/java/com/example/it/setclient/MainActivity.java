package com.example.it.setclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {
    EditText nickname;
    int token = 0;
    Retrofit retrofit;
    JsonServer jsonServer;

    class Register {
        public String status;
        public int token;

        public Register(String status, int token) {
            this.status = status;
            this.token = token;
        }
    }
    class Card {
        public int count, fill, shape, color;

        public Card() {
            this.count = count;
            this.fill = fill;
            this.shape = shape;
            this.color = color;
        }
        @Override
        public String toString()
        {
            return "count: " + count + ", fill: " + fill + ", shape: " + shape + ", color: " + color;
        }
    }
    class CardList
    {
        String status;
        List<Card> cards;

        public CardList(String status, List<Card> cards)
        {
            this.status = status;
            this.cards = cards;
        }
    }
    class User {
        public String action;
        public String nickname;

        public User(String action, String nickname) {
            this.action = action;  this.nickname = nickname;
        }
    }

    class FetchCards {
        public String action;
        public int token;

        public FetchCards(String action, int token) {
            this.action = action;  this.token = token;
        }
    }


    interface JsonServer {
        @POST("/")
        Call<Register> register(@Body User user);
        @POST("/")
        Call<CardList> fetchcards(@Body FetchCards fetchCards);
    }

    public static final String API_URL = "http://194.176.114.21:8050/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nickname = (EditText) findViewById(R.id.nickname);
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        jsonServer = retrofit.create(JsonServer.class);
    }

    public void onClickRegister(View v)
    {
        Call<Register> call = jsonServer.register(new User("register", nickname.getText().toString()));

        //final Button registerbutton = (Button) findViewById(R.id.register);
        //registerbutton.setEnabled(false);

        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, retrofit2.Response<Register> response) {
                Register register = response.body();
                Toast.makeText(MainActivity.this, "status: " + register.status
                        + ", id: " + register.token, Toast.LENGTH_SHORT).show();
                token = register.token;
                //registerbutton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w("DEBUG", t.getMessage());
                //registerbutton.setEnabled(true);
            }
        });
    }

    public void onClickFetch(View v)
    {
        Call<CardList> call = jsonServer.fetchcards(new FetchCards("fetch_cards", token));

        call.enqueue(new Callback<CardList>() {
            @Override
            public void onResponse(Call<CardList> call, retrofit2.Response<CardList> response) {
                CardList cardList = response.body();
                String cards = cardList.cards.get(0).toString();
                Toast.makeText(MainActivity.this, "status: " + cardList.status
                        + "card: " + cards
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CardList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w("DEBUG", t.getMessage());
            }
        });
    }
}
