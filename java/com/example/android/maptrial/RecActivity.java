package com.example.android.maptrial;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RecActivity extends AppCompatActivity {

    public int nxt;
    public String to_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reclayout);

        ListView listView = (ListView) findViewById(R.id.list1);
        TextView nxt = (TextView) findViewById(R.id.next);

        ArrayAdapter<String> alladapter= new ArrayAdapter<String>(RecActivity.this,
                android.R.layout.simple_list_item_1, MainActivity2.recievedsymp);

        listView.setAdapter(alladapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int p = position;
                to_add = MainActivity2.recievedsymp.get(p);
                showAlertDialogButtonClicked();







            }
        });

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(RecActivity.this, ProceedActivity.class);
                startActivity(activityChangeIntent);
            }
        });

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
}
