package app.kuatiseptiani.kenclengidapplication;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import app.kuatiseptiani.kenclengidapplication.helper.SqliteHelper;

//import com.andexert.library.RippleView;

public class AddActivity extends AppCompatActivity {

    RadioGroup radio_status;
    EditText edit_nominal, edit_catatan;
    Button btn_simpan;

//    RadioButton radio_pemasukan, radio_pengeluaran;
//    RippleView rip_simpan;

    String status;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        status = "";
        sqliteHelper = new SqliteHelper(this);

        radio_status = (RadioGroup) findViewById(R.id.radio_status);
        edit_nominal = (EditText) findViewById(R.id.edit_nominal);
        edit_catatan = (EditText) findViewById(R.id.edit_catatan);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
//        radio_pemasukan = findViewById (R.id.radio_pemasukan);
//        radio_pengeluaran = findViewById (R.id.radio_pengeuaran);
//        rip_simpan = findViewById (R.id.rip_simpan);

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_pemasukan:
                        status = "pemasukan";
                        break;
                    case R.id.radio_pengeluaran:
                        status = "pengeluaran";
                        break;
                }
                Log.d("Log status", status);
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
              if (status.equals("") || edit_nominal.getText().toString().equals("")) {
                  Toast.makeText(getApplicationContext(), "Isi data dengan benar",
                          Toast.LENGTH_LONG).show();
              } else {
                  SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                  database.execSQL("INSERT INTO tb_kencleng(status, nominal, catatan) VALUES('" +
                          status + "','" +
                          edit_nominal.getText().toString() + "','" +
                          edit_catatan.getText().toString() + "')"
                  );
                  Toast.makeText(getApplicationContext(), "Transaksi berhasil disimpan", Toast.LENGTH_LONG).show();
                  finish();
              }
          }
      });

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Transaksi");

            }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
