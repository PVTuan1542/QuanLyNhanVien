package sqlite_qlnhanvien.com;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {


    final String DATABASE_NAME = "QL.sqlite";
    final int RESQUEST_TAKE_PHOTO = 123 ;
    final int RESQUEST_CHOOSE_PHOTO = 321;
    int id=-1;

    Button bt_chonHinh,bt_chupHinh,bt_luu,bt_thoat ;
    EditText et_sdt,et_name;
    ImageView anhDaiDien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        addControls();
        addEvents();
        initUI();
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        SQLiteDatabase database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("Select * from QLNhanVien where ID = ?",new String[]{id + "",});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String sdt = cursor.getString(2);
        byte[] img = cursor.getBlob(3);

        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
        anhDaiDien.setImageBitmap(bitmap);
        et_name.setText(ten);
        et_sdt.setText(sdt);
    }

    private void addControls() {
        bt_chonHinh = findViewById(R.id.bt_chonHinh);
        bt_chupHinh = findViewById(R.id.bt_chupHinh);
        et_sdt = findViewById(R.id.et_sdt);
        et_name = findViewById(R.id.et_name);
        anhDaiDien = findViewById(R.id.anhDaiDien);
        bt_luu = findViewById(R.id.bt_luu);
        bt_thoat = findViewById(R.id.bt_thoat);

    }

    private void addEvents(){
        bt_chonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        bt_chupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        bt_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    update();
            }
        });

        bt_thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,RESQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imgUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    anhDaiDien.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                anhDaiDien.setImageBitmap(bitmap);
            }

        }
    }

    private void update(){
        String ten = et_name.getText().toString();
        String sdt = et_sdt.getText().toString();

        byte[] anh = getByteArrayFromImageView(anhDaiDien);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten",ten);
        contentValues.put("SDT",sdt);
        contentValues.put("Anh",anh);

        SQLiteDatabase database = Database.initDatabase(this,"QL.sqlite");
        database.update("QLNhanVien",contentValues,"ID = ?",new String[] {id +""});
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    private  void cancel(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        return imageInByte ;
    }
}