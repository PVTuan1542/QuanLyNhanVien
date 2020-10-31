package sqlite_qlnhanvien.com;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String DATABASE_NAME = "QL.sqlite";
    SQLiteDatabase database ;

    ListView listView;
    ArrayList<NhanVien> list;
    AdapterNhanVien adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addControls();
        readData();

    }



    private void addControls() {
        listView = findViewById(R.id.listView);
        list= new ArrayList<>();
        adapter = new AdapterNhanVien(this,list);
        listView.setAdapter(adapter);
    }
    private void readData() {
        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("Select * from QLNhanVien ",null);
        list.clear();
        for( int i=0; i < cursor.getCount() ; i++)
        {
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String sdt = cursor.getString(1);
            String ten = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);

            list.add(new NhanVien(id,sdt,ten,anh));
        }
        adapter.notifyDataSetChanged();

    }
}