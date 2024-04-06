package com.example.tugas67;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TambahActivity extends AppCompatActivity {
    protected Cursor cursor;
    Database database;
    Button batal,simpan;
    EditText nama,nomor,tanggal,alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = new Database(this);
        nama = findViewById(R.id.nama);
        nomor = findViewById(R.id.nomor);
        tanggal = findViewById(R.id.tanggal);
        alamat = findViewById(R.id.alamat);
        simpan = findViewById(R.id.simpan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = database.getWritableDatabase();
                db.execSQL("INSERT INTO kontak (no, nama, tgl, alamat) values('"+
                        nomor.getText().toString() + "', '" +
                        nama.getText().toString() + "', '" +
                        tanggal.getText().toString() + "', '" +
                        alamat.getText().toString() + "')");
                Toast.makeText(TambahActivity.this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                MainActivity.main.RefreshList();
                finish();
            }
        });
        batal = findViewById(R.id.batal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup aktivitas saat ini dan kembali ke aktivitas sebelumnya (menu utama)
            }
        });

    }
}
