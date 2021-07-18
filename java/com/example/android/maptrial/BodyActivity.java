package com.example.android.maptrial;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.io.InputStream;
import java.util.Arrays;


public class BodyActivity extends AppCompatActivity {

    public ArrayList<String> symps = new ArrayList<String>();
    public ArrayList<String> dummy = new ArrayList<String>();

    public int active = 0;

    public ArrayList<String> head = new ArrayList<String>();
    public ArrayList<String> body = new ArrayList<String>();
    public ArrayList<String> extremes = new ArrayList<String>();
    public ArrayList<String> abdomen = new ArrayList<String>();
    public ArrayList<String> chest = new ArrayList<String>();
    public ArrayList<String> waste = new ArrayList<String>();

    public ArrayAdapter<String> sadapter;
    public static int nxt;
    public static String to_add;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout3);
        readArr(R.raw.symptom_name , R.raw.symptom_part);


        sadapter = new ArrayAdapter<String>(BodyActivity.this, android.R.layout.simple_list_item_1, dummy);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createNotificationChannel();
        }



        Intent intent = new Intent(BodyActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(BodyActivity.this,0,intent,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long timenow = System.currentTimeMillis();
        long tensec = 1000*10;

        alarmManager.set(AlarmManager.RTC_WAKEUP, timenow + tensec , pendingIntent);


        TextView sl = (TextView) findViewById(R.id.SymptomsList);
        TextView nxt = (TextView) findViewById(R.id.Next);
        ListView listView = (ListView) findViewById(R.id.list3);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.my_frame);
        Button button = (Button) findViewById(R.id.wholebody);
        //Button chestbtn = (Button) findViewById(R.id.chestbtn);
        // Button headbtn = (Button) findViewById(R.id.headbtn);

        sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(BodyActivity.this , NextActivity.class);

                // currentContext.startActivity(activityChangeIntent);

                startActivity(activityChangeIntent);
            }
        });



        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(BodyActivity.this , MainActivity2.class);

                // currentContext.startActivity(activityChangeIntent);

                startActivity(activityChangeIntent);
            }
        });

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                getHotspotColor(x,y);

                return true;
            }
        });









        listView.setAdapter(sadapter);

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(dataAdapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                String item = parent.getItemAtPosition(position).toString();
//                Log.d("this is an item: " , item);
//                if(item == "1")
//                {
//                    symps.clear();
//                    symps.add("headache");
//                    symps.add("Dizzy");
//                    symps.add("Double Vision");
//
//                    sadapter.notifyDataSetChanged();
//                }
//                else if(item == "2")
//                {
//                    symps.clear();
//                    symps.add("Difficulty in breathing");
//                    symps.add("Cough");
//                    symps.add("Chest pressure");
//                    symps.add("Chest burning");
//
//                    sadapter.notifyDataSetChanged();
//                }
//                else
//                {
//                    symps.clear();
//                    sadapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                symps.clear();
//                sadapter.notifyDataSetChanged();
//            }
//
//        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int p = position;
                if(active == 1)
                {
                    to_add = body.get(p);
                }
                if(active == 2)
                {
                    to_add = head.get(p);
                }
                if(active == 3)
                {
                    to_add = chest.get(p);
                }
                if(active == 4)
                {
                    to_add = abdomen.get(p);
                }
                if(active == 5)
                {
                    to_add = waste.get(p);
                }
                else if(active == 6)
                {
                    to_add = extremes.get(p);
                }


                showAlertDialogButtonClicked();



            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                active = 1;
                sadapter.clear();
                sadapter.addAll(body);


                sadapter.notifyDataSetChanged();

            }
        });

    }

    public void getHotspotColor(int x,int y){
        ImageView img = (ImageView) findViewById (R.id.image_areas);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        int px = (hotspots.getPixel(x,y));
        Log.d("this is a pixel " , String.valueOf(px));
        if(px == -65524) //head
        {
            active = 2;
            sadapter.clear();
            sadapter.addAll(head);

            sadapter.notifyDataSetChanged();
        }
        if(px == -15066462) //chest
        {
            active = 3;
            sadapter.clear();
            sadapter.addAll(chest);

            sadapter.notifyDataSetChanged();
        }
        if(px == -15925504) //abdomen
        {
            active = 4;
            sadapter.clear();
            sadapter.addAll(abdomen);


            sadapter.notifyDataSetChanged();
        }
        if(px == -37634) //waste ext
        {
            active = 5;
            sadapter.clear();
            sadapter.addAll(waste);
            sadapter.notifyDataSetChanged();
        }
        if(px == -590080) //Exremeties
        {
            active = 6;
            sadapter.clear();
            sadapter.addAll(extremes);
            sadapter.notifyDataSetChanged();
        }

    };

    public void readArr(int id , int id2)
    {
        try
        {
            //String r = "R.raw"+file;
            InputStream is = this.getResources().openRawResource(id);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String sCurrentLine;
            int i=0;


            while ((sCurrentLine = br.readLine()) != null) {

                Log.d(sCurrentLine,"txt0");


                symps.add(sCurrentLine);
                //Log.d("this is my line", "arr: " + sCurrentLine);
            }

            InputStream is2 = this.getResources().openRawResource(id2);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
            String sCurrentLine2;
            Log.d("this is a size" , String.valueOf(i));


            while ((sCurrentLine2 = br2.readLine()) != null) {

                Log.d("this is i" ,String.valueOf(i));

                if(sCurrentLine2.contains("Head"))
                {
                    head.add(symps.get(i).replace("_"," "));
                }
                if(sCurrentLine2.contains("WholeBody"))
                {
                    body.add(symps.get(i).replace("_"," "));
                }
                if(sCurrentLine2.contains("Extremeties"))
                {
                    extremes.add(symps.get(i).replace("_"," "));
                }
                if(sCurrentLine2.contains("Abdominal"))
                {
                    abdomen.add(symps.get(i).replace("_"," "));
                }
                if(sCurrentLine2.contains("Chest"))
                {
                    chest.add(symps.get(i).replace("_"," "));
                }
                if(sCurrentLine2.contains("WasteExtractionOrgans"))
                {
                    waste.add(symps.get(i).replace("_"," "));
                }



                i++;

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
                                if(!NextActivity.currentsymp.contains(to_add) && nxt > 0)
                                {
                                    NextActivity.currentsymp.add(to_add);
                                    NextActivity.currentsymp_.add(new Symptom(to_add , nxt));
                                }
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }



    private void createNotificationChannel(){
        CharSequence name = "notifyme";
        String description = "Notifcation channel";
        int imp = NotificationManager.IMPORTANCE_DEFAULT;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("notifyme",name,imp);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


}
