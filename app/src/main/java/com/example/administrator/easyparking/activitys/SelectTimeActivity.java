package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.dialog.ChangeBirthDialog;
import com.example.administrator.easyparking.dialog.TimePickerDialog;
import com.umeng.socialize.utils.Log;

/**
 * Created by tan on 2017/2/25 0025.
 */
public class SelectTimeActivity extends Activity {

    private EditText edit_start_date;
    private EditText edit_start_time;
    private EditText edit_end_date;
    private EditText edit_end_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker_layout);


        edit_start_date = (EditText) findViewById(R.id.edit_start_date);
        edit_start_date.setInputType(InputType.TYPE_NULL);
        edit_start_time = (EditText) findViewById(R.id.edit_start_time);
        edit_start_time.setInputType(InputType.TYPE_NULL);
        edit_end_date = (EditText) findViewById(R.id.edit_end_date);
        edit_end_date.setInputType(InputType.TYPE_NULL);
        edit_end_time = (EditText) findViewById(R.id.edit_end_time);
        edit_end_time.setInputType(InputType.TYPE_NULL);
        setEditTextListener();

    }

    private void setEditTextListener() {
        edit_start_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeBirthDialog changeBirthDialog = new ChangeBirthDialog(SelectTimeActivity.this);
                    changeBirthDialog.show();
                    changeBirthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

                        @Override
                        public void onClick(String year, String month, String day) {
                            // TODO Auto-generated method stub
                            edit_start_date.setText(year + "-" + month + "-" + day);
                            Log.d("TAG", "select time is " + year + "-" + month + "-" + day);
                        }
                    });
                }
            });

            edit_end_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeBirthDialog changeBirthDialog = new ChangeBirthDialog(SelectTimeActivity.this);
                    changeBirthDialog.show();
                    changeBirthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

                        @Override
                        public void onClick(String year, String month, String day) {
                            // TODO Auto-generated method stub
                            edit_end_date.setText(year + "-" + month + "-" + day);
                            Log.d("TAG", "select time is " + year + "-" + month + "-" + day);
                        }
                    });
                }
            });

            edit_start_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(SelectTimeActivity.this);
                    timePickerDialog.show();
                    timePickerDialog.setOnTimeListener(new TimePickerDialog.OnTimeListener() {
                        @Override
                        public void onClick(String hour, String minute) {
                            edit_start_time.setText(hour + "-" + minute);
                            Log.d("TAG", "select time is " + hour + "-" + minute);
                        }
                    });
                }
            });

            edit_end_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(SelectTimeActivity.this);
                    timePickerDialog.show();
                    timePickerDialog.setOnTimeListener(new TimePickerDialog.OnTimeListener() {
                        @Override
                        public void onClick(String hour, String minute) {
                            edit_end_time.setText(hour + "-" + minute);
                            Log.d("TAG", "select time is " + hour + "-" + minute);
                        }
                    });
                }
            });


        }
}
