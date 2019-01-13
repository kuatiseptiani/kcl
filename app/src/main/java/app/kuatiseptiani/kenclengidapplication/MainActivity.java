package app.kuatiseptiani.kenclengidapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import app.kuatiseptiani.kenclengidapplication.helper.SqliteHelper;

public class MainActivity extends AppCompatActivity {

    TextView text_masuk, text_keluar, text_total;
    ListView list_kencleng;
    SwipeRefreshLayout swipe_refresh;
    ArrayList<HashMap<String, String>> aruskencleng = new ArrayList<HashMap<String, String>>();

    String id;
    //   String transaksi_id;

    String query_kencleng, query_total;
    SqliteHelper sqliteHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//           }
//       });

        id = "";
        query_kencleng = "";
        query_total = "";

        sqliteHelper = new SqliteHelper(this);

        text_masuk = (TextView) findViewById(R.id.text_masuk);
        text_keluar = (TextView) findViewById(R.id.text_keluar);
        text_total = (TextView) findViewById(R.id.swipe_refresh);
        list_kencleng = (ListView) findViewById(R.id.list_kencleng);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query_kencleng   =
                        "SELECT *, strftime('%d/%m/%Y', tanggal) AS tgl FROM tb_kencleng ORDER BY id DESC";
                query_total =
                        "SELECT SUM(nominal) AS total, " +
                                "(SELECT SUM(nominal) FROM tb_kencleng WHERE status='pemasukan') AS pemasukan, " +
                                "(SELECT SUM(nominal) FROM tb_kencleng WHERE status='pengeluaran') AS pengeluaran " +
                                "FROM tb_kencleng";
                kenclengAdapter();
            }
        });

//     aruskencleng = new ArrayList<>();

//     sqliteHelper = new SqliteHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here
                startActivity(new Intent(MainActivity.this, AddActivity.class));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            }
        });

    }


    private void kenclengAdapter() {
//        swipe_refresh.setRefreshing(false);
        aruskencleng.clear();
        list_kencleng.setAdapter(null);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
//        cursor = database.rawQuery( SELECT *,  strftime('%d/%m/%Y', tanggal) AS tanggal FROM db_kencleng WHERE id= '"'+ MainActivity.id + ',' null);
        cursor = database.rawQuery(query_kencleng, null);
        cursor.moveToFirst();

        int i;
        for (i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            //Log.d("status", cursor.getString(1));

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", cursor.getString(0));
            map.put("status", cursor.getString(1));
            map.put("nominal", cursor.getString(2));
            map.put("catatan", cursor.getString(3));
            map.put("tanggal", cursor.getString(5));
            aruskencleng.add(map);
        }

        if (i == 0) {
            Toast.makeText(getApplicationContext(), "Tidak ada transaksi untuk ditampilkan",
                    Toast.LENGTH_LONG).show();
        }
    }

//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskencleng, R.layout.list_kencleng,
//                new String[] { "id", "status", "nominal", "catatan", "tanggal"},
//                new int[] {R.id.text_id, R.id.text_status, R.id.text_nominal, R.id.text_catatan,
//                        R.id.text_tanggal});

//        list_kencleng.setAdapter(simpleAdapter);
//        list_kencleng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                id = ((TextView) view.findViewById(R.id.text_id)).getText().toString();
//                ListMenu();
//            }
//        });

//        kenclengTotal();
//    }

    private void kenclengTotal() {
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(query_total, null);
        cursor.moveToFirst();
//        text_total.setText( rupiahFormat.format(cursor.getDouble(0)) );
        text_masuk.setText(rupiahFormat.format(cursor.getDouble(1)));
        text_keluar.setText(rupiahFormat.format(cursor.getDouble(2)));
        text_total.setText(
                rupiahFormat.format(cursor.getDouble(1) - cursor.getDouble(2))
        );

        swipe_refresh.setRefreshing(false);

//        if (!filter) { text_filter.setVisibility(View.GONE); }
//        filter = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        query_kencleng =
                "SELECT *, strftime ('%d/%m/%Y', tanggal) AS tanggal FROM tb_kencleng ORDER BY id DESC";
        query_total =
                "SELECT SUM(jumlah) AS total, " +
                        "(SELECT SUM(nominal) FROM tb_kencleng WHERE status='pemasukan') AS pemasukan, " +
                        "(SELECT SUM(nominal) FROM tb_kencleng WHERE status='pengeluaran') AS pengeluaran " +
                        "FROM tb_kencleng";

        kenclengAdapter();
    }

    private void ListMenu() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.list_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView text_edit = (TextView) dialog.findViewById(R.id.text_edit);
        TextView text_hapus = (TextView) dialog.findViewById(R.id.text_hapus);
        dialog.show();

        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });
        text_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hapus();
            }
        });
    }


    private void hapus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Yakin untuk menghapus transaksi ini?");
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                        database.execSQL("DELETE FROM tb_kencleng WHERE id = '" + id + "'");
                        Toast.makeText(getApplicationContext(), "Tranksaki berhasil dihapus",
                                Toast.LENGTH_LONG).show();

                        kenclengAdapter();

                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}


//    SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskencleng, R.layout.list_kencleng,
 //           new String[]("id", "status", "nominal", "catatan", "tanggal"),
 //           new int[] (R.id.text_id, R.id.text_status, R.id.text_nominal, R.id.text_catatan,R.id.text_tanggal) );


//        list_kencleng.setAdapter(simpleAdapter);
  //      list_kencleng.setOnItemClickListener(new AdapterView.OnItemClickListener() {

 //       @Override
 //       public void onItemClick (AdapterView< ? > parent, View view, int position, long id){
   //     id = ((TextView) findViewById().toString(R.id.id)).getText().toString();
  //      Log.e("id", id);

 //       private void ListMenu();
 //   }
 //   });

//    KenclengTotal();

  //      }




//AnggapTakAda


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
  //      int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
 //           return true;
 //       }

//        return super.onOptionsItemSelected(item);
 //   }
//}