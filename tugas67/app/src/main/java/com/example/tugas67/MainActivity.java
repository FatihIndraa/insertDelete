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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final String selectedContact = contactsList.get(position);
            showOptionsDialog(selectedContact);
        });

        findViewById(R.id.fab).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TambahActivity.class);
            startActivity(intent);
        });
    }

    private void showOptionsDialog(final String selectedContact) {
        String[] options = {"Lihat Kontak", "Edit Kontak", "Hapus Kontak"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilihan");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    startDetailActivity(selectedContact);
                    break;
                case 1:
                    startUpdateActivity(selectedContact);
                    break;
                case 2:
                    deleteContact(selectedContact);
                    break;
            }
        });
        builder.show();
    }
    private void startDetailActivity(String selectedContact) {
        Intent intentDetail = new Intent(MainActivity.this, DetailActivity.class);
        intentDetail.putExtra("nama", selectedContact);
        startActivity(intentDetail);
    }

    private void startUpdateActivity(String selectedContact) {
        Intent intentUpdate = new Intent(MainActivity.this, UpdateActivity.class);
        intentUpdate.putExtra("nama", selectedContact);
        startActivity(intentUpdate);
    }

    private void displayContacts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nama FROM kontak", null);
        if (cursor != null) {
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Tidak ada kontak yang tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteContact(String nama) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete("kontak", "nama = ?", new String[]{nama});
        if (deletedRows > 0) {
            Toast.makeText(this, "Kontak berhasil dihapus", Toast.LENGTH_SHORT).show();
            contactsList.remove(nama);
            ((ArrayAdapter<String>) listView.getAdapter()).notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Gagal menghapus kontak", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        contactsList.clear();
        displayContacts();
    }
}
