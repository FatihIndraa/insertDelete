package com.example.tugas67;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TambahActivity extends AppCompatActivity {

    EditText editTextNama, editTextNomor, editTextTanggal, editTextAlamat;
    Button buttonSimpan;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        dbHelper = new Database(this);
        editTextNama = findViewById(R.id.nama);
        editTextNomor = findViewById(R.id.nomor);
        editTextTanggal = findViewById(R.id.tanggal);
        editTextAlamat = findViewById(R.id.alamat);
        buttonSimpan = findViewById(R.id.simpan);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahKontak();
            }
        });
    }

    // Method untuk menambahkan kontak baru ke dalam database
    private void tambahKontak() {
        String nama = editTextNama.getText().toString().trim();
        String nomor = editTextNomor.getText().toString().trim();
        String tanggal = editTextTanggal.getText().toString().trim();
        String alamat = editTextAlamat.getText().toString().trim();

        if (!nama.isEmpty() && !nomor.isEmpty() && !tanggal.isEmpty() && !alamat.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nama", nama);
            values.put("no", nomor);
            values.put("tgl", tanggal);
            values.put("alamat", alamat);

            long newRowId = db.insert("kontak", null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Kontak berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke MainActivity setelah menambahkan kontak
            } else {
                Toast.makeText(this, "Gagal menambahkan kontak", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Silakan lengkapi semua field", Toast.LENGTH_SHORT).show();
        }
    }
}
