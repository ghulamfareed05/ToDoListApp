package com.example.todolistapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {
    SqlHelper sqlHelper;
    boolean isEditMode = false;
    ReminderModel reminderModel;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);


        findViewById(R.id.create_btn).setOnClickListener(this);
        findViewById(R.id.calender_btn).setOnClickListener(this);
        findViewById(R.id.time_btn).setOnClickListener(this);
        findViewById(R.id.date_delete_btn).setOnClickListener(this);
        findViewById(R.id.time_delete_btn).setOnClickListener(this);
        findViewById(R.id.new_item_add_to_list).setOnClickListener(this);
        findViewById(R.id.due_date).setOnClickListener(this);
        findViewById(R.id.due_time).setOnClickListener(this);
        sqlHelper = new SqlHelper(getApplicationContext(),SqlHelper.DB_NAME);
        initSpinner();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            reminderModel = (ReminderModel) bundle.getSerializable("Data");
          loadDataInFields(reminderModel);
          isEditMode =true;
        }



    }

    private void loadDataInFields(ReminderModel reminderModel) {
        findViewById(R.id.task_finished).setVisibility(View.VISIBLE);
        findViewById(R.id.date_delete_btn).setVisibility(View.VISIBLE);
        findViewById((R.id.time_field)).setVisibility(View.VISIBLE);
        findViewById(R.id.time_delete_btn).setVisibility(View.VISIBLE);
        ((EditText)findViewById(R.id.notes)).setText(reminderModel.getNotes());
        ((TextView)findViewById(R.id.due_date)).setText(reminderModel.getDueDate());
        ((TextView)findViewById(R.id.due_time)).setText(reminderModel.getDoeTime());
        Spinner snooze_spinner = findViewById(R.id.snooze_time);
        Spinner add_list_spinner = findViewById(R.id.add_to_list);
        snooze_spinner.setSelection(getSpinnerItemPosition(reminderModel.getSnoozeText(),snooze_spinner));
        add_list_spinner.setSelection(getSpinnerItemPosition(reminderModel.getAddList(),add_list_spinner));
    }

    private int getSpinnerItemPosition(String item,Spinner spinner){

        for(int i = 0;i<spinner.getAdapter().getCount();i++){
            if(item.equals(spinner.getItemAtPosition(i))) return i;
        }
        return -1;
    }
    private void initSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        Cursor cursor = sqlHelper.getReadableDatabase().rawQuery("Select * From "+SqlHelper.REMINDER_LIST_TABLE,null);
        while (cursor.moveToNext()){
            spinnerAdapter.add(cursor.getString(cursor.getColumnIndex(SqlHelper.LIST_NAME)));
        }
        cursor.close();
        ((Spinner)findViewById(R.id.add_to_list)).setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.calender_btn ||v.getId()==R.id.due_date){
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar timeCal = Calendar.getInstance();
                    timeCal.set(year,month,dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault());
                    String mDate = dateFormat.format(timeCal.getTime());
                    ((TextView)findViewById(R.id.due_date)).setText(mDate);

                    findViewById(R.id.date_delete_btn).setVisibility(View.VISIBLE);
                    findViewById(R.id.time_field).setVisibility(View.VISIBLE);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

        }
        else if(v.getId()==R.id.date_delete_btn){
            ((TextView)findViewById(R.id.due_date)).setText("");
            findViewById(R.id.date_delete_btn).setVisibility(View.GONE);
        }
        else if(v.getId()==R.id.time_btn || v.getId()==R.id.due_time){

            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar calendar = Calendar.getInstance();

                    calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    calendar.set(Calendar.MINUTE,minute);
                    SimpleDateFormat tmeForm = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String timeFormat = tmeForm.format(calendar.getTime());
                    ((TextView) findViewById(R.id.due_time)).setText(timeFormat);

                    findViewById(R.id.time_delete_btn).setVisibility(View.VISIBLE);
                }
            },0,0,false).show();
             }

        else if(v.getId()==R.id.time_delete_btn){
            ((TextView)findViewById(R.id.due_time)).setText("");
            findViewById(R.id.time_delete_btn).setVisibility(View.GONE);
        }
        else if(v.getId()==R.id.new_item_add_to_list){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New List");
            EditText editText = new EditText(this);
            builder.setView(editText);

            builder.setPositiveButton("Ok",(d,w)->{
                @SuppressWarnings("All")

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
                copyAdapter(((Spinner)findViewById(R.id.add_to_list)).getAdapter(),adapter);
                adapter.add(editText.getText().toString());

                ((Spinner)findViewById(R.id.add_to_list)).setAdapter(adapter);
                insertListItemDb(editText.getText().toString());

            });
            builder.setNegativeButton("Cancel",(d,w)->{
               d.dismiss();
            });
            builder.show();
        }
        else if(v.getId()==R.id.create_btn) {
            long id=0;
            String notes = ((EditText) findViewById(R.id.notes)).getText().toString();
            String dueDate = ((TextView) findViewById(R.id.due_date)).getText().toString();
            String dueTime = ((TextView) findViewById(R.id.due_time)).getText().toString();
            String snoozeText = ((Spinner) findViewById(R.id.snooze_time)).getSelectedItem().toString();
            String addList = ((Spinner) findViewById(R.id.add_to_list)).getSelectedItem().toString();



            if (notes.isEmpty()) {
                Toast.makeText(this, "Please enter task at first", Toast.LENGTH_LONG).show();
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SqlHelper.NOTES, notes);
                contentValues.put(SqlHelper.DUE_DATE, dueDate);
                contentValues.put(SqlHelper.DUE_TIME, dueTime);
                contentValues.put(SqlHelper.SNOOZE_TEXT, snoozeText);
                contentValues.put(SqlHelper.LIST_NAME, addList);
                if(isEditMode) {
                    id = reminderModel.getId();
                    String[] ids = new String[]{String.valueOf(id)};
                    SQLiteDatabase db = sqlHelper.getWritableDatabase();
                    db.update(SqlHelper.REMINDER_TABLE,contentValues," Id = ? ", ids);
                }
                else {
                    id = sqlHelper.getWritableDatabase().insert(SqlHelper.REMINDER_TABLE, null, contentValues);

                }
//            Cursor cursor = sqlHelper.getReadableDatabase().rawQuery("select * from "+SqlHelper.TABLE_NAME,null);
//            Log.e("COlumn name = ",cursor.getColumnName(1));
//                        while (cursor.moveToNext()){
//                Log.e("Notes",cursor.getString(cursor.getColumnIndex(SqlHelper.NOTES)));
//            }
//            cursor.close();
                String date = dueDate + " " + dueTime;
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy hh:mm a", Locale.getDefault());
                try {
                    Date date1 = dateFormat.parse(date);
                    setAlarm(date1.getTime(),id);
                    Log.e("TIme = ", date1.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TIme Exec = ", e.getMessage());
                }

                startActivity(new Intent(this, MainActivity.class));

            }
        }
    }

    private void setAlarm(long timeInMillis,long id) {

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        String task = ((EditText)findViewById(R.id.notes)).getText().toString();
        task =  task.length() >25 ? task.substring(0,25) : task;

        intent.putExtra("M",task);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) id,intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,timeInMillis,pendingIntent);
        }
        else {
            alarmManager.setExact(AlarmManager.RTC,timeInMillis,pendingIntent);
        }

    }

    private void copyAdapter(SpinnerAdapter source, ArrayAdapter<String> destination) {
        for(int i = 0;i<source.getCount();i++){
            destination.add((String)source.getItem(i));
        }
    }

    private void insertListItemDb(String toString) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlHelper.LIST_NAME,toString);
        sqlHelper.getWritableDatabase().insert(SqlHelper.REMINDER_LIST_TABLE,null,contentValues);
    }
}
