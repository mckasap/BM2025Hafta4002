package com.example.bm2025hafta4002;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn;
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
        btn=(Button) findViewById(R.id.button);
        editText =(EditText) findViewById(R.id.editTextText);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                ToDoItem item=(ToDoItem) listView.getItemAtPosition(position);
                int status=item.getStatus();
                status=status==1?0:1;
                database.execSQL("UPDATE TASKS SET Status="+status+" WHERE Id="+item.getId());
                Listele();

            }
        });
        registerForContextMenu(listView);
        Listele();
    }

    private void Listele() {
        listView.setAdapter(null);
        ArrayList<ToDoItem> liste= new ArrayList<>();
        Cursor cursor=database.rawQuery("SELECT * FROM TASKS",null);
        cursor.moveToFirst();

        while (cursor.moveToNext()){
            @SuppressLint("Range") int id=cursor.getInt(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String task=cursor.getString(cursor.getColumnIndex("TASK"));
            @SuppressLint("Range") int status=cursor.getInt(cursor.getColumnIndex("Status"));
            ToDoItem item=new ToDoItem(id,status,task);
            liste.add(item);
        }
        ArrayAdapter<ToDoItem> adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                liste);
        listView.setAdapter(adapter);

    }

    ToDoItem selectedItem;
    public void Ekle(View view) {
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(this, "Boş Geçilemez", Toast.LENGTH_SHORT).show();
            return;
        }

        if(btn.getText().toString().equals("Ekle")){
            String task=editText.getText().toString();
            database.execSQL("INSERT INTO TASKS(TASK,Status) VALUES('"+task+"',0)");
            Listele();
        }
        else{

            String task=editText.getText().toString();
            database.execSQL("UPDATE TASKS SET TASK='"+task+"' WHERE Id="+selectedItem.getId());
            Listele();
            btn.setText("Ekle");
        }



    }

    public void Sil(View view) {
        database.execSQL("DELETE FROM TASKS WHERE Status=1");
        Listele();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.contextmenu,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {



        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedItem = (ToDoItem) listView.getItemAtPosition(info.position);


        if (item.getItemId() == R.id.sil) {

            if (selectedItem.getStatus() == 1){
                database.execSQL("DELETE FROM TASKS WHERE Id=" + selectedItem.getId());
                Listele();
            }else{

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle( "Görevi Henüz Tamamlanmamış \nSilmek İstediğinize Emin Misiniz?");
                builder.setPositiveButton("Evet", (dialog, which) -> {
                    database.execSQL("DELETE FROM TASKS WHERE Id=" + selectedItem.getId());
                    Listele();
                });
                builder.setNegativeButton("Hayır", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();

            }

        }
        else if (item.getItemId() == R.id.duzenle) {
            btn.setText("Duzenle");
            editText.setText(selectedItem.getTask());
            return false;
        }
                return super.onContextItemSelected(item);



    }
}