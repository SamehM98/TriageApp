package com.example.android.maptrial;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ProceedActivity extends AppCompatActivity{

    public SymptomAdapter adapter2;
    public static String result = "3";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.d("i am " , "working");
        ListView lview = (ListView) findViewById(R.id.mylist);
        TextView bd = (TextView) findViewById(R.id.BodyDiagram);
        TextView nxt = (TextView) findViewById(R.id.next);

        adapter2 = new SymptomAdapter(ProceedActivity.this, R.layout.list_item, NextActivity.currentsymp_);


        lview.setAdapter(adapter2);
        bd.setText("Back");

        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityChangeIntent = new Intent(ProceedActivity.this, RecActivity.class);

                startActivity(activityChangeIntent);

                lview.setAdapter(adapter2);




            }
        });


        lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos = position;

                new AlertDialog.Builder(ProceedActivity.this).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?").setMessage("Do you want to delete this symptom?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = adapter2.getItem(pos).getName();
                                NextActivity.currentsymp_.remove(adapter2.getItem(pos));
                                NextActivity.currentsymp.remove(name);
                                adapter2.notifyDataSetChanged();
                                //Log.d("this is",adapter1.getItem(pos));
                            }
                        }).setNegativeButton("No",null).show();

                return true;
            }
        });

                nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NextActivity.currentsymp_.size() < 3 || NextActivity.currentsymp_.size() > 17 )
                {
                    ShowAlert();
                    return;
                }



                FormBody.Builder builder = new FormBody.Builder();

                for (int i=0;i<NextActivity.currentsymp_.size();i++)
                {
                    String key1 = "symp" + String.valueOf(i);
                    String key2 = "deg" + String.valueOf(i);
                    builder.add(NextActivity.currentsymp_.get(i).getName(), String.valueOf(NextActivity.currentsymp_.get(i).getDegree()));
                }

                RequestBody formbody2 = builder.build();
                String url = "https://prediction-svcmodel.herokuapp.com/predict";
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url(url).post(formbody2).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                        if(response.isSuccessful())
                        {
                            Log.d("this is " , "message");
                            String d = response.body().string();
                            Log.d("this is a value" , String.valueOf(d.charAt(1)));

                            if(d.charAt(1) == '2')
                                result = "2";
                            if(d.charAt(1) == '3')
                                result = "3" ;
                            if(d.charAt(1) == '4')
                                result = "4" ;
                            if(d.charAt(1) == '5')
                                result = "5" ;
                            //result = String.valueOf(d.charAt(1));

                            try{
                                ProceedActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("this is " , "successful");


                                    }
                                });

                            }
                            catch (Throwable t)
                            {
                                Log.d("this is " , "error");
                            }

                        }

                        Intent activityChangeIntent3 = new Intent(ProceedActivity.this, MainActivity.class);

                        startActivity(activityChangeIntent3);

                    }
                });


            }
        });

    }

    public void ShowAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(ProceedActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Number of symptoms must be between 3 and 17");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }



}
