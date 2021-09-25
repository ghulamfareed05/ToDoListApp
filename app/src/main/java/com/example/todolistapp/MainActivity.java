
package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ItemAdapter.ItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.float_btn).setOnClickListener(this);

        SqlHelper sqlHelper = new SqlHelper(getApplicationContext(),SqlHelper.DB_NAME);
        Cursor cursor = sqlHelper.getReadableDatabase().rawQuery("Select * from "+ SqlHelper.REMINDER_TABLE,null);
        ArrayList<ReminderModel> reminderModels = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SqlHelper.ID));
            String note = cursor.getString(cursor.getColumnIndex(SqlHelper.NOTES));
            String date = cursor.getString(cursor.getColumnIndex(SqlHelper.DUE_DATE));
            String time = cursor.getString(cursor.getColumnIndex(SqlHelper.DUE_TIME));
            String snoozeText = cursor.getString(cursor.getColumnIndex(SqlHelper.SNOOZE_TEXT));
            String addList = cursor.getString(cursor.getColumnIndex(SqlHelper.LIST_NAME));
            ReminderModel reminderModel = new ReminderModel(note,date,time,snoozeText,addList,id);
            reminderModels.add(reminderModel);
        }
        cursor.close();
        RecyclerView recyclerView =  findViewById(R.id.todoList);
        ItemAdapter itemAdapter = new ItemAdapter(reminderModels,this);
        itemAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView =findViewById(R.id.todoList);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if(adapter != null)
        recyclerView.getAdapter().notifyDataSetChanged();
        
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.float_btn){
        startActivity(new Intent(this,CreateNoteActivity.class));
    }
    }

    @Override
    public void onItemClicked(ReminderModel reminderModel) {
        Toast.makeText(getApplicationContext(),reminderModel.getNotes(),Toast.LENGTH_LONG).show();
       Intent intent = new Intent(this,CreateNoteActivity.class);
       Bundle bundle = new Bundle();
       bundle.putSerializable("Data",reminderModel);
       intent.putExtras(bundle);
       startActivity(intent);
    }

    @Override
    public void onDeleteItem(ReminderModel reminderModel,int pos) {
    SqlHelper sqlHelper = new SqlHelper(getApplicationContext(),SqlHelper.DB_NAME);
        SQLiteDatabase database = sqlHelper.getWritableDatabase();

        String[] args = {String.valueOf(reminderModel.getId())};
        int i = database.delete("Reminder_Table","Id"+"=?",args);
        if(i>0){
            Toast.makeText(getApplicationContext(), "Task is delete", Toast.LENGTH_LONG).show();
        }
        RecyclerView recyclerView = findViewById(R.id.todoList);

        ItemAdapter itemAdapter = (ItemAdapter)recyclerView.getAdapter();
        itemAdapter.remove(pos);


    }
}
