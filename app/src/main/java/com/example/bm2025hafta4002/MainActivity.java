package com.example.bm2025hafta4002;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView   listView;
    EditText editText;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database=openOrCreateDatabase("TASKS",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS TASKS(ID INTEGER PRIMARY KEY AUTOINCREMENT,TASK VARCHAR, Status INTEGER)");
        listView =(ListView) findViewById(R.id.listView);
        editText =(EditText) findViewById(R.id.editTextText);


        Listele();


    }

    private void Listele() {
        ArrayList<ToDoItem> liste= new ArrayList<>();
        Cursor cursor=database.rawQuery("SELECT * FROM TASKS",null);
        cursor.moveToFirst();

        while (cursor.moveToNext()){
            @SuppressLint("Range")int id=cursor.getInt(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String task=cursor.getString(cursor.getColumnIndex("TASK"));
            @SuppressLint("Range") int status=cursor.getInt(cursor.getColumnIndex("Status"));
            ToDoItem item=new ToDoItem(status,task);
            liste.add(item);
        }
        ArrayAdapter<ToDoItem> adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                liste);
        listView.setAdapter(adapter);

    }

    public void Ekle(View view) {

        String task=editText.getText().toString();
        database.execSQL("INSERT INTO TASKS(TASK,Status) VALUES('"+task+"',0)");
        Listele();

    }
}