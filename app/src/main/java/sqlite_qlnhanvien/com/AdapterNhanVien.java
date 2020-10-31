package sqlite_qlnhanvien.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
                context.startActivity(intent);
            }
        });

        return row;
    }
}
