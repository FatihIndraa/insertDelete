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

    public class UpdateActivity extends AppCompatActivity {
        protected Cursor cursor;
        Database database;
        Button batal,update;
        EditText nama,nomor,tanggal,alamat;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_update);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            database = new Database(this);
            nama = findViewById(R.id.nama);
            nomor = findViewById(R.id.nomor);
            tanggal = findViewById(R.id.tanggal);
            alamat = findViewById(R.id.alamat);
            update = findViewById(R.id.update_button);


            SQLiteDatabase db = database.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM kontak WHERE nama = '" +
                    getIntent().getStringExtra("nama")+"'", null);


            cursor.moveToFirst();
            if(cursor.getCount() >0){
                cursor.moveToPosition(0);
                nama.setText(cursor.getString(1).toString());
                nomor.setText(cursor.getString(2).toString());
                tanggal.setText(cursor.getString(3).toString());
                alamat.setText(cursor.getString(4).toString());


            }
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase db = database.getWritableDatabase();
                    db.execSQL("UPDATE kontak SET nama='" +
                            nama.getText().toString() + "', tgl='" +
                            tanggal.getText().toString() + "', alamat='" +
                            alamat.getText().toString() + "' WHERE no='" +
                            getIntent().getStringExtra("no") + "'");
                    Toast.makeText(UpdateActivity.this, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
                    MainActivity.main.RefreshList();
                    finish();
                }
            });


        }
    }