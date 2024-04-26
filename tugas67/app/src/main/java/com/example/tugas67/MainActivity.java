package com.example.tugas67;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> contactsList;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new Database(this);
        listView = findViewById(R.id.listView);

        contactsList = new ArrayList<>();
        displayContacts();

        // Set listener untuk item yang dipilih pada ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedContact = contactsList.get(position);
                // Menampilkan dialog pilihan (lihat, edit, delete)
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Pilihan")
                        .setItems(new String[]{"Lihat Kontak", "Edit Kontak", "Delete Kontak"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // Lihat kontak
                                        Intent intentDetail = new Intent(MainActivity.this, DetailActivity.class);
                                        intentDetail.putExtra("nama", selectedContact);
                                        startActivity(intentDetail);
                                        break;
                                    case 1:
                                        // Edit kontak
                                        Intent intentUpdate = new Intent(MainActivity.this, UpdateActivity.class);
                                        intentUpdate.putExtra("nama", selectedContact);
                                        startActivity(intentUpdate);
                                        break;
                                    case 2:
                                        // Hapus kontak
                                        deleteContact(selectedContact);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

        // Set listener untuk float button
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method untuk menampilkan daftar kontak dari database
    private void displayContacts() {
        // Membuka database dalam mode baca
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Menentukan kolom yang akan diambil dari tabel kontak
        String[] projection = {
                "nama"
        };

        // Menjalankan kueri untuk mengambil semua nama kontak
        Cursor cursor = db.query(
                "kontak",   // Nama tabel
                projection, // Kolom yang ingin diambil
                null,   // Kriteria seleksi (tidak digunakan)
                null,   // Nilai kriteria seleksi (tidak digunakan)
                null,   // Group by (tidak digunakan)
                null,   // Having (tidak digunakan)
                null    // Urutan pengurutan (tidak digunakan)
        );

        // Memeriksa apakah kursor berisi data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Menetapkan nilai yang diperoleh dari database ke ArrayList
                int namaIndex = cursor.getColumnIndex("nama");
                if (namaIndex >= 0) {
                    String nama = cursor.getString(namaIndex);
                    contactsList.add(nama);
                } else {
                    Toast.makeText(this, "Kolom 'nama' tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());

            // Menutup cursor setelah selesai menggunakannya
            cursor.close();

            // Buat ArrayAdapter untuk menghubungkan data ArrayList dengan ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Tidak ada kontak yang tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    // Method untuk menghapus kontak dari database berdasarkan nama
    private void deleteContact(String nama) {
        // Membuka database dalam mode tulis
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Menentukan kriteria seleksi (WHERE clause)
        String selection = "nama = ?";
        String[] selectionArgs = { nama };

        // Menjalankan operasi delete pada tabel kontak
        int deletedRows = db.delete(
                "kontak",   // Nama tabel
                selection,  // Kriteria seleksi
                selectionArgs   // Nilai kriteria seleksi
        );

        // Memeriksa apakah penghapusan berhasil
        if (deletedRows > 0) {
            Toast.makeText(this, "Kontak berhasil dihapus", Toast.LENGTH_SHORT).show();
            // Perbarui daftar kontak setelah penghapusan
            contactsList.remove(nama);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Gagal menghapus kontak", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Bersihkan daftar kontak sebelum menampilkan lagi
        contactsList.clear();
        // Tampilkan daftar kontak yang diperbarui
        displayContacts();
    }
}
