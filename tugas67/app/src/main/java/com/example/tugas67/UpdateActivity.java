package com.example.tugas67;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    EditText editTextNama, editTextNomor, editTextTanggal, editTextAlamat;
    Button buttonUpdate, buttonBatal;
    Database dbHelper;
    Calendar calendar;
    String selectedContact;
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
        buttonBatal = findViewById(R.id.batal);
        calendar = Calendar.getInstance();
        editTextTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        selectedContact = getIntent().getStringExtra("nama");
        displayContactDetails(selectedContact);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set tanggal yang dipilih ke EditText
                        editTextTanggal.setText(String.format("%02d-%02d-%02d", dayOfMonth, month + 1, year % 100));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void displayContactDetails(String contactName) {
        // Membuka database dalam mode baca
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Menentukan kolom yang akan diambil dari tabel kontak
        String[] projection = {"no", "tgl", "alamat"
        };

        // Menentukan kriteria seleksi (WHERE clause)
        String selection = "nama = ?";
        String[] selectionArgs = { contactName };
        Cursor cursor = db.query(
                "kontak", projection, selection, selectionArgs, null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            int nomorIndex = cursor.getColumnIndex("no");
            int tanggalIndex = cursor.getColumnIndex("tgl");
            int alamatIndex = cursor.getColumnIndex("alamat");
            if (nomorIndex != -1 && tanggalIndex != -1 && alamatIndex != -1) {
                String nomor = cursor.getString(nomorIndex);
                String tanggal = cursor.getString(tanggalIndex);
                String alamat = cursor.getString(alamatIndex);
                editTextNama.setText(contactName);
                editTextNomor.setText(nomor);
                editTextTanggal.setText(tanggal);
                editTextAlamat.setText(alamat);
            } else {
                Toast.makeText(this, "Kolom tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
    // Method untuk memperbarui kontak dalam database
    private void updateContact() {
        // Membuka database dalam mode tulis
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Membuat objek ContentValues untuk menyimpan pasangan nama kolom dan nilai
        ContentValues values = new ContentValues();
        values.put("no", editTextNomor.getText().toString());
        values.put("tgl", editTextTanggal.getText().toString());
        values.put("alamat", editTextAlamat.getText().toString());

        // Menentukan kriteria seleksi (WHERE clause)
        String selection = "nama" + " LIKE ?";
        String[] selectionArgs = { selectedContact }; // Menggunakan nama kontak yang dipilih sebelumnya

        // Menjalankan operasi update pada tabel kontak
        int count = db.update(
                "kontak", values, selection, selectionArgs
        );
        // Memeriksa apakah pembaruan berhasil
        if (count > 0) {
            Toast.makeText(this, "Kontak berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal memperbarui kontak", Toast.LENGTH_SHORT).show();
        }
    }
}
