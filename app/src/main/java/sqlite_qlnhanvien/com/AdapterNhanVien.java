package sqlite_qlnhanvien.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterNhanVien extends BaseAdapter {

    Activity context;
    ArrayList<NhanVien> list;

    public AdapterNhanVien(Activity context, ArrayList<NhanVien> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row,null);
        ImageView imgHinhDaiDien = row.findViewById(R.id.imageView);
        TextView tv_id = row.findViewById(R.id.tv_id);
        TextView tv_sdt = row.findViewById(R.id.tv_sdt);
        TextView tv_ten = row.findViewById(R.id.tv_ten);
        Button bt_sua = row.findViewById(R.id.bt_sua);
        Button bt_xoa = row.findViewById(R.id.bt_xoa);

        final  NhanVien nhanVien = list.get(position);
        tv_id.setText(nhanVien.id +" ");
        tv_sdt.setText(nhanVien.sdt );
        tv_ten.setText(nhanVien.ten );

        Bitmap bmHinhDaiDien = BitmapFactory.decodeByteArray(nhanVien.anh,0,nhanVien.anh.length);
        imgHinhDaiDien.setImageBitmap(bmHinhDaiDien);

        bt_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,UpdateActivity.class);
                intent.putExtra("ID",nhanVien.id);
                context.startActivity(intent);
            }
        });

        bt_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Ban có muốn xóa không ?");
                builder.setMessage("Bạn có chắc chắn muốn xóa nhân viên này không");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(nhanVien.id);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return row;
    }

    private void delete(int idNhanVien) {
        SQLiteDatabase database = Database.initDatabase(context,"QL.sqlite");
        database.delete("QLNhanVien","ID = ?",new String[]{idNhanVien + ""});
        list.clear();
        Cursor cursor = database.rawQuery("Select * from QLNhanVien",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);

            list.add(new NhanVien(id,ten,sdt,anh ));
        }
        notifyDataSetChanged();
    }
}
