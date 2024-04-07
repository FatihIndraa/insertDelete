package com.example.tugas67;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText editTextNama, editTextNomor, editTextTanggal, editTextAlamat;
    Button buttonUpdate;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbHelper = new Database(this);
        editTextNama = findViewById(R.id.nama);
        editTextNomor = findViewById(R.id.nomor);
        editTextTanggal = findViewById(R.id.tanggal);
        editTextAlamat = findViewById(R.id.alamat);
        buttonUpdate = findViewById(R.id.update_button);

        // Ambil data kontak yang dipilih dari database
        displayContactDetails();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
    }

    // Method untuk menampilkan detail kontak yang akan diperbarui
    // Method untuk menampilkan detail kontak yang akan diperbarui
    private void displayContactDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM kontak", null);
        if (cursor.moveToFirst()) {
            int namaIndex = cursor.getColumnIndex("nama");
            int nomorIndex = cursor.getColumnIndex("no");
            int tanggalIndex = cursor.getColumnIndex("tgl");
            int alamatIndex = cursor.getColumnIndex("alamat");

            if (namaIndex != -1 && nomorIndex != -1 && tanggalIndex != -1 && alamatIndex != -1) {
                String nama = cursor.getString(namaIndex);
                String nomor = cursor.getString(nomorIndex);
                String tanggal = cursor.getString(tanggalIndex);
                String alamat = cursor.getString(alamatIndex);

                editTextNama.setText(nama);
                editTextNomor.setText(nomor);
                editTextTanggal.setText(tanggal);
                editTextAlamat.setText(alamat);
            } else {
                Toast.makeText(this, "Kolom tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
    }


    // Method untuk memperbarui kontak dalam database
    private void updateContact() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("no", editTextNomor.getText().toString());
        values.put("tgl", editTextTanggal.getText().toString());
        values.put("alamat", editTextAlamat.getText().toString());
        String selection = "nama" + " LIKE ?";
        String[] selectionArgs = { editTextNama.getText().toString() };
        int count = db.update("kontak", values, selection, selectionArgs);
        if (count > 0) {
            Toast.makeText(this, "Kontak berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke MainActivity setelah memperbarui kontak
        } else {
            Toast.makeText(this, "Gagal memperbarui kontak", Toast.LENGTH_SHORT).show();
        }
    }
}
