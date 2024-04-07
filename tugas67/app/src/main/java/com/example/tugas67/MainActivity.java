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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nama FROM kontak", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int namaIndex = cursor.getColumnIndex("nama");
                if (namaIndex >= 0) {
                    String nama = cursor.getString(namaIndex);
                    contactsList.add(nama);
                } else {
                    Toast.makeText(this, "Kolom 'nama' tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete("kontak", "nama = ?", new String[]{nama});
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
