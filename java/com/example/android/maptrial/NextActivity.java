package com.example.android.maptrial;

import androidx.annotation.Nullable;
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
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class NextActivity extends AppCompatActivity{



    public static ArrayList<String> allsymp = new ArrayList<String>();
    public int nxt;
    public static int flag=0;
    public String to_add;
    public static ArrayList<String> currentsymp = new ArrayList<String>();
    public static ArrayList<Symptom> currentsymp_ = new ArrayList<Symptom>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);
        readArr(R.raw.symptom_name);

        ListView listView = (ListView) findViewById(R.id.list1);

        TextView bd = (TextView) findViewById(R.id.BodyDiagram);
        TextView nxt = (TextView) findViewById(R.id.Next);

        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(NextActivity.this, BodyActivity.class);

                // currentContext.startActivity(activityChangeIntent);

                startActivity(activityChangeIntent);
            }
        });

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(NextActivity.this , MainActivity2.class);

                // currentContext.startActivity(activityChangeIntent);

                startActivity(activityChangeIntent);
            }
        });

        Collections.sort(allsymp);
        ArrayAdapter<String> alladapter= new ArrayAdapter<String>(NextActivity.this,
                android.R.layout.simple_list_item_1, allsymp);

        listView.setAdapter(alladapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int p = position;
                to_add = allsymp.get(p);
                showAlertDialogButtonClicked();







            }
        });


    }

    public void readArr(int id)
    {
        try
        {
            //String r = "R.raw"+file;
            InputStream is = this.getResources().openRawResource(id);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String sCurrentLine;


            while ((sCurrentLine = br.readLine()) != null) {

                //Log.d(sCurrentLine,"txt0");


                allsymp.add(sCurrentLine.replace("_"," "));
                //Log.d("this is my line", "arr: " + sCurrentLine);
            }



            //Log.d("this is my size", String.valueOf(i));

        }
        catch (IOException e) {
            Log.d("this is my size", "im not found2");
            e.printStackTrace();
        }
    }

    public void showAlertDialogButtonClicked()
    {
        flag=0;
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Enter Scale");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_layout,
                        null);
        builder.setView(customLayout);

        SeekBar seekBar = (SeekBar) customLayout.findViewById(R.id.seekbarID);
        TextView tv = (TextView) customLayout.findViewById(R.id.tvid);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("this is a progress" , String.valueOf(progress));
                tv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // add a button
        builder
                .setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {

                                // send data from the
                                // AlertDialog to the Activity



                                nxt = seekBar.getProgress();
                                if(!currentsymp.contains(to_add) && nxt > 0)
                                {
                                    currentsymp.add(to_add);
                                    currentsymp_.add(new Symptom(to_add , nxt));

                                }
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }




}
