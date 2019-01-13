package app.kuatiseptiani.kenclengidapplication;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;

import app.kuatiseptiani.kenclengidapplication.helper.CurrentDate;
import app.kuatiseptiani.kenclengidapplication.helper.SqliteHelper;

import static android.R.attr.id;

public class EditActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_pemasukan, radio_pengeluaran;

    EditText edit_nominal, edit_catatan, edit_tanggal;
    Button btn_simpan;
//    RippleView rip_simpan;

    String status, tanggal;

    SqliteHelper sqliteHelper;
    Cursor cursor;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

//        sqliteHelper = new SqliteHelper(this);

        status = "";
        tanggal = "";

        radio_status = (RadioGroup) findViewById(R.id.radio_status);
        radio_pemasukan = (RadioButton) findViewById(R.id.radio_pemasukan);
        radio_pengeluaran = (RadioButton) findViewById(R.id.radio_pengeluaran);
        edit_nominal = (EditText) findViewById(R.id.edit_nominal);
        edit_catatan = (EditText) findViewById(R.id.edit_catatan);
        edit_tanggal = (EditText) findViewById(R.id.edit_tanggal);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        //      rip_simpan = findViewById(R.id.rip_simpan);

        sqliteHelper = new SqliteHelper(this);
        SQLiteDatabase database = sqliteHelper.getReadableDatabase();

//        cursor = database.rawQuery(
//                "SELECT *, strftime('%d/%m/%Y', tanggal) AS tanggal FROM tb_kencleng WHERE id ='" + MainActivity.id + "'"
//                , null);
//        cursor.moveToFirst();

//        status = cursor.getString(1);
//        switch (status) {
//            case "pemasukan":
//                radio_pemasukan.setChecked(true);
//                break;
//            case "pengeluaran":
//                radio_pengeluaran.setChecked(true);
//                break;
//        }

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

        edit_nominal.setText(cursor.getString(2));
        edit_catatan.setText(cursor.getString(3));

        tanggal = cursor.getString(4);
        edit_tanggal.setText(cursor.getString(5));

        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month_of_year, int day_of_month) {
                        // set day of month , month and year value in the edit text
                        NumberFormat numberFormat = new DecimalFormat("00");
                        tanggal = year + "-" + numberFormat.format((month_of_year + 1)) + "-" +
                                numberFormat.format(day_of_month);
                        edit_tanggal.setText(numberFormat.format(day_of_month) + "/" + numberFormat.format((month_of_year + 1)) +
                                "/" + year);
                    }
                }, CurrentDate.year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
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
                    database.execSQL(
                            "UPDATE tb_kencleng SET status='" + status + "', nominal='" + edit_nominal.getText().toString() +
                                    "', " + "catatan='" + edit_catatan.getText().toString() + "', tanggal='" + tanggal +
                                    "' WHERE id='" + id + "'"
                    );
                    Toast.makeText(getApplicationContext(), "Perubahan berhasil disimpan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}