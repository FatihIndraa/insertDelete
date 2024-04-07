package com.example.tugas67;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    TextView textViewNama, textViewNomor, textViewTanggal, textViewAlamat;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new Database(this);
        textViewNama = findViewById(R.id.nama);
        textViewNomor = findViewById(R.id.nomor);
        textViewTanggal = findViewById(R.id.tanggal);
        textViewAlamat = findViewById(R.id.alamat);

        // Ambil data kontak yang dipilih dari Intent
        Intent intent = getIntent();
        if (intent.hasExtra("nama")) {
            String selectedContact = intent.getStringExtra("nama");
            displayContactDetails(selectedContact);
        } else {
            Toast.makeText(this, "Data kontak tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Method untuk menampilkan detail kontak
    private void displayContactDetails(String selectedContact) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM kontak WHERE nama = ?", new String[]{selectedContact});
        if (cursor.moveToFirst()) {
            int nomorIndex = cursor.getColumnIndex("no");
            int tanggalIndex = cursor.getColumnIndex("tgl");
            int alamatIndex = cursor.getColumnIndex("alamat");

            if (nomorIndex != -1 && tanggalIndex != -1 && alamatIndex != -1) {
                String nomor = cursor.getString(nomorIndex);
                String tanggal = cursor.getString(tanggalIndex);
                String alamat = cursor.getString(alamatIndex);

                textViewNama.setText(selectedContact);
                textViewNomor.setText(nomor);
                textViewTanggal.setText(tanggal);
                textViewAlamat.setText(alamat);
            } else {
                Toast.makeText(this, "Kolom tidak ditemukan", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Data kontak tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
        cursor.close();
    }
}
