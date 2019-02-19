package app.kuatiseptiani.kenclengidapplication;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

import java.util.Calendar;

import app.kuatiseptiani.kenclengidapplication.helper.SqliteHelper;

public class EditActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_pemasukan, radio_pengeluaran;

    EditText edit_nominal, edit_catatan;
    Button edit_tanggal;
    Button btn_simpan;

    SqliteHelper sqliteHelper;
    Cursor cursor;
    String idTransaksi;
    int id;

    String status, tanggal;

    private DatePickerDialog datePickerDialog;
    private TextView tvDateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        status = ""; tanggal = "";

        radio_status = findViewById(R.id.radio_status);
        radio_pemasukan = findViewById(R.id.radio_pemasukan);
        radio_pengeluaran = findViewById(R.id.radio_pengeluaran);

        edit_nominal = findViewById(R.id.edit_nominal);
        edit_catatan = findViewById(R.id.edit_catatan);
        edit_tanggal = findViewById(R.id.edit_tanggal);
        btn_simpan = findViewById(R.id.btn_simpan);

        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        edit_tanggal = (Button) findViewById(R.id.edit_tanggal);

        idTransaksi = getIntent().getStringExtra("idTransaksi");
        id = Integer.parseInt(idTransaksi);
        Log.d("Transaksi",id+"");

        sqliteHelper = new SqliteHelper(this);
        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT *, strftime('%d/%m/%Y', tanggal) AS tanggal FROM tb_kencleng WHERE id =" + id + ""
                , null
        );
        cursor.moveToFirst();

        status = cursor.getString(1);
        switch (status){
            case "pemasukan":
                radio_pemasukan.setChecked(true); break;
            case "pengeluaran":
                radio_pengeluaran.setChecked(true); break;
        }

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
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

        edit_nominal.setText( cursor.getString(2) );
        edit_catatan.setText( cursor.getString(3) );

        tanggal = cursor.getString(4);
        String delim = "[-]";
        String[] parse = tanggal.split(delim);
        int year = Integer.parseInt(parse[0]);
        int monthOfYear = Integer.parseInt(parse[1])-1;
        int dayOfMonth= Integer.parseInt(parse[2]);

        tvDateResult.setText("Tanggal transaksi : "+year+"-"+((monthOfYear)+1)+"-"+dayOfMonth);
        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });


//        edit_tanggal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month_of_year, int day_of_month) {
//                        // set day of month , month and year value in the edit text
//                        NumberFormat numberFormat = new DecimalFormat("00");
//                        tanggal = year + "-" + numberFormat.format(( month_of_year +1 )) + "-" +
//                                numberFormat.format(day_of_month);
////                        edit_tanggal.setText(numberFormat.format(day_of_month) + "/" + numberFormat.format(( month_of_year +1 )) +
////                                "/" + year );
//                    }
//                }, CurrentDate.year, CurrentDate.month, CurrentDate.day);
//                datePickerDialog.show();
//            }
//        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status.equals("") || edit_nominal.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar",
                            Toast.LENGTH_LONG).show();
                } else {
                    SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                    database.execSQL(
                            "UPDATE tb_kencleng SET status='" + status + "', nominal='" + edit_nominal.getText().toString() +
                                    "', " + "catatan='" + edit_catatan.getText().toString() + "', tanggal='" + tanggal +
                                    "' WHERE id=" + id + ";"
                    );
                    Toast.makeText(getApplicationContext(), "Perubahan berhasil disimpan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Transaksi");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tanggal =year+"-"+((monthOfYear)+1)+"-"+dayOfMonth;

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tvDateResult.setText("Kamu mengubah tanggal transaksi menjadi : "+year+"-"+((monthOfYear)+1)+"-"+dayOfMonth);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

}