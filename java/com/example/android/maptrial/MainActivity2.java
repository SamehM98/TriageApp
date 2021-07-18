package com.example.android.maptrial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import com.example.android.maptrial.BodyActivity;
import com.example.android.maptrial.MainActivity;
import com.example.android.maptrial.NextActivity;
import com.example.android.maptrial.R;
import com.example.android.maptrial.SymptomAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity {

    //public ArrayAdapter<String> adapter1= new ArrayAdapter<String>(MainActivity.this,
     //       android.R.layout.simple_list_item_1, NextActivity.currentsymp);
    public static ArrayList<String> recievedsymp = new ArrayList<String>();





    public ArrayAdapter<String> adapter1;
    public SymptomAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       // NextActivity.currentsymp_.add(new Symptom("headache" , 3));


        Log.d("i am " , "working");
        ListView lview = (ListView) findViewById(R.id.mylist);
        TextView bd = (TextView) findViewById(R.id.BodyDiagram);
        TextView nxt = (TextView) findViewById(R.id.next);

        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityChangeIntent = new Intent(MainActivity2.this, BodyActivity.class);

                startActivity(activityChangeIntent);

            }
        });

//        nxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(NextActivity.currentsymp_.size() == 0)
//                    ShowAlert();
//                else{
//
//                Intent activityChangeIntent = new Intent(MainActivity2.this, MainActivity.class);
//
//               startActivity(activityChangeIntent);}
//
//            }
//        });


//         ArrayList<String> symps = new ArrayList<String>();
//
//        symps.add("Sameh");
//        symps.add("ahm");
//        symps.add("Saddeh");
         adapter1 = new ArrayAdapter<String>(MainActivity2.this, android.R.layout.simple_list_item_1, NextActivity.currentsymp);
         adapter2 = new SymptomAdapter(MainActivity2.this, R.layout.list_item, NextActivity.currentsymp_);


        lview.setAdapter(adapter2);



        lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos = position;

                new AlertDialog.Builder(MainActivity2.this).setIcon(android.R.drawable.ic_delete)
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

//        Button btn = (Button) findViewById(R.id.button1);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                Intent activityChangeIntent = new Intent(MainActivity.this, NextActivity.class);
//
//                // currentContext.startActivity(activityChangeIntent);
//
//                startActivity(activityChangeIntent);
//            }
//        });
//
//        Button btn2 = (Button) findViewById(R.id.button2);
//
//        btn2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                Intent activityChangeIntent2 = new Intent(MainActivity.this, BodyActivity.class);
//
//                // currentContext.startActivity(activityChangeIntent);
//
//                startActivity(activityChangeIntent2);
//            }
//        });
//
//        Button btn3 = (Button) findViewById(R.id.button3);

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NextActivity.currentsymp_.size() == 0)
                {
                    ShowAlert();
                    return;
                }


                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();

                for (int i=0;i<NextActivity.currentsymp_.size();i++)
                {
                    String key1 = "symp" + String.valueOf(i);
                    String key2 = "deg" + String.valueOf(i);
                    builder.add(NextActivity.currentsymp_.get(i).getName(), String.valueOf(NextActivity.currentsymp_.get(i).getDegree()));
                }

                RequestBody formbody2 = builder.build();
                //String url = "http://192.168.1.4:5000/tester";
                String url = "https://prediction-svcmodel.herokuapp.com/recommend";
                Request request = new Request.Builder().url(url).post(formbody2).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        if (response.isSuccessful()) {
                            Log.d("this is recommender ", "message");
                            String d = response.body().string();

                            try {

                                JSONObject obj = new JSONObject(d);
                                System.out.println(obj.length());

                                Log.d("My App this Json", obj.toString());
                                JSONArray key = obj.names();
                               for (int i = 0; i < key.length(); ++i) {
 
                                  String value = obj.getString(key.getString(i)); // Here's your value
                                   Log.d("this is a key value : ", key.getString(i));
                                   recievedsymp.add(key.getString(i).replace('_',' '));
                               }

                                Log.d("My App this Json", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed");
                            }

                            Log.d("I am" , "here");

                            Intent activityChangeIntent3 = new Intent(MainActivity2.this, RecActivity.class);

                            startActivity(activityChangeIntent3);

                        }


                    }
                });

            }
        });


//        nxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FormBody.Builder builder = new FormBody.Builder();
//
//                for (int i=0;i<NextActivity.currentsymp_.size();i++)
//                {
//                    String key1 = "symp" + String.valueOf(i);
//                    String key2 = "deg" + String.valueOf(i);
//                    builder.add(NextActivity.currentsymp_.get(i).getName(), String.valueOf(NextActivity.currentsymp_.get(i).getDegree()));
//                }
//
//                RequestBody formbody2 = builder.build();
//                String url = "https://prediction-svcmodel.herokuapp.com/predict";
//                OkHttpClient client = new OkHttpClient();
//
//                Request request = new Request.Builder().url(url).post(formbody2).build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//
//                        if(response.isSuccessful())
//                        {
//                            Log.d("this is " , "message");
//                            String d = response.body().string();
//                            Log.d("this is a value" , String.valueOf(d.charAt(1)));
//
//                            try{
//                                MainActivity2.this.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Log.d("this is " , "successful");
//
//
//                                    }
//                                });
//
//
//                            }
//                            catch (Throwable t)
//                            {
//                                Log.d("this is " , "error");
//                            }
//
//                        }
//
//                    }
//                });
//
//
//            }
//        });



    }



    public void ShowAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity2.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Please enter atleast one symptom");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter2.notifyDataSetChanged();

    }
}