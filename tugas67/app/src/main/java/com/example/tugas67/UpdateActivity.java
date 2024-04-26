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
    String selectedContact; // Variabel untuk menyimpan nama kontak yang akan diperbarui

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

        // Membuat objek Calendar
        calendar = Calendar.getInstance();

        // Set listener untuk editTextTanggal agar muncul DatePickerDialog saat diklik
        editTextTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Ambil data kontak yang dipilih dari database
        displayContactDetails();

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

    // Method untuk menampilkan DatePickerDialog
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

        // Tampilkan DatePickerDialog
        datePickerDialog.show();
    }

    private void displayContactDetails() {
        // Membuka database dalam mode baca
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Menentukan kolom yang akan diambil dari tabel kontak
        String[] projection = {
                "no", "tgl", "alamat"
        };

        // Menentukan kriteria seleksi (WHERE clause)
        String selection = "nama" + " LIKE ?";
        String[] selectionArgs = {getIntent().getStringExtra("nama")};

        // Menjalankan kueri untuk mengambil data kontak yang sesuai dengan nama yang dipilih
        Cursor cursor = db.query(
                "kontak",   // Nama tabel
                projection, // Kolom yang ingin diambil
                selection,   // Kriteria seleksi
                selectionArgs,   // Nilai kriteria seleksi
                null,   // Group by (tidak digunakan)
                null,   // Having (tidak digunakan)
                null    // Urutan pengurutan (tidak digunakan)
        );

        // Memeriksa apakah kursor berisi data
        if (cursor != null && cursor.moveToFirst()) {
            int nomorIndex = cursor.getColumnIndex("no");
            int tanggalIndex = cursor.getColumnIndex("tgl");
            int alamatIndex = cursor.getColumnIndex("alamat");

            if (nomorIndex != -1 && tanggalIndex != -1 && alamatIndex != -1) {
                String nomor = cursor.getString(nomorIndex);
                String tanggal = cursor.getString(tanggalIndex);
                String alamat = cursor.getString(alamatIndex);

                editTextNama.setText(getIntent().getStringExtra("nama")); // Menampilkan nama kontak yang sedang diupdate
                editTextNomor.setText(nomor);
                editTextTanggal.setText(tanggal);
                editTextAlamat.setText(alamat);
                selectedContact = getIntent().getStringExtra("nama"); // Menyimpan nama kontak yang akan diperbarui
            } else {
                Toast.makeText(this, "Kolom tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        }

        // Menutup cursor setelah selesai menggunakannya
        if (cursor != null) {
            cursor.close();
        }
    }

    // Method untuk memperbarui kontak dalam database
    private void updateContact() {
        // Membuka database dalam mode tulis
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nama", editTextNama.getText().toString()); // Memperbarui nama kontak
        values.put("no", editTextNomor.getText().toString());
        values.put("tgl", editTextTanggal.getText().toString());
        values.put("alamat", editTextAlamat.getText().toString());

        // Menentukan kriteria seleksi (WHERE clause)
        String selection = "nama" + " LIKE ?";
        String[] selectionArgs = {selectedContact}; // Menggunakan nama kontak yang disimpan sebelumnya

        // Menjalankan operasi update pada tabel kontak
        int count = db.update(
                "kontak",   // Nama tabel
                values,   // Nilai yang akan diperbarui
                selection,   // Kriteria seleksi
                selectionArgs   // Nilai kriteria seleksi
        );

        // Memeriksa apakah update berhasil
        if (count > 0) {
            Toast.makeText(this, "Kontak berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke MainActivity setelah memperbarui kontak
        } else {
            Toast.makeText(this, "Gagal memperbarui kontak", Toast.LENGTH_SHORT).show();
        }
    }
}
