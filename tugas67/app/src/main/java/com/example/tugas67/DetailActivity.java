package com.example.tugas67;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {
    protected Cursor cursor;
    Database database;
    TextView nama,nomor,tanggal,alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database = new Database(this);
        nama = (TextView) findViewById(R.id.nama);
        nomor = (TextView)findViewById(R.id.nomor);
        tanggal = (TextView)findViewById(R.id.tanggal);
        alamat = (TextView)findViewById(R.id.alamat);

        SQLiteDatabase db = database.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM kontak WHERE no= '"+
                getIntent().getStringExtra("nama")+"'", null);

        cursor.moveToFirst();
        if (cursor.getCount()>0){
            cursor.moveToPosition(0);
            nama.setText(cursor.getString(1));
            nomor.setText(cursor.getString(2));
            tanggal.setText(cursor.getString(3));
            alamat.setText(cursor.getString(4));
        }

    }
}