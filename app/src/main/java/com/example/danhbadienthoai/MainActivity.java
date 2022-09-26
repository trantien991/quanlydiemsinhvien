package com.example.danhbadienthoai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewDanhBa;
   public static DanhBaAdapter danhBaAdapter;
    ArrayList<DanhBa> danhBaArrayList = new ArrayList<>();
    TextView txtTenBan, txtTenBanKT;
    EditText edtTimKiem;
    ImageButton imgThem;
    public  static SQLiteDatabase DB;
   public static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Init();
        Action();
        CaNhan();
        TimKiem();
    }


    private void Init() {
       recyclerViewDanhBa = findViewById(R.id.recyclerviewDanhBa);
       txtTenBan = findViewById(R.id.textViewTenBan);
       edtTimKiem= findViewById(R.id.edtTimKiem);
       imgThem   = findViewById(R.id.imageButtonThem);
       txtTenBanKT= findViewById(R.id.textViewTenCN);
        createOpentDatabse();
        cursor = null;
    }
    private  void Action()
    {
        createOpentDatabse();
        MakteTable();
        GanGiaoDienRecyclerView();
        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                player.start();
                ThemDanhBa();
            }
        });
        ShowDanhBa();
    }

    private void GanGiaoDienRecyclerView() {
        recyclerViewDanhBa.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewDanhBa.setLayoutManager(linearLayoutManager);
        danhBaAdapter = new DanhBaAdapter(getApplicationContext(),danhBaArrayList);
        recyclerViewDanhBa.setAdapter(danhBaAdapter);
    }

    // them danh bạ bạn bè
    private void ThemDanhBa()
    {
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.themdanhba);
        Button btnNo = (Button) dialog.findViewById(R.id.buttonHuy);
        Button btnYes = (Button) dialog.findViewById(R.id.buttonLuu);
        final EditText tenDB = dialog.findViewById(R.id.editTextTenDanhBa);
        final EditText sdt   = dialog.findViewById(R.id.editTextSoDienThoai);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                player.start();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                player.start();
                if(tenDB.getText().toString().isEmpty() || sdt.getText().toString().isEmpty() )
                {
                    Toast.makeText(MainActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ContentValues values = new ContentValues();
                    values.put("TenDB",tenDB.getText().toString().trim());
                    values.put("SDT",sdt.getText().toString().trim());
                    DB.insert("DanhSach","_Id",values);
                    Toast.makeText(MainActivity.this, "Thêm Thành Công!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ShowDanhBa();
                }


            }
        });
        dialog.show();
    }

    // tạo bảng database danh sách để lưu trữ thông tin bạn bè: id, tên, sdt
    private void MakteTable() {
        String sql = "CREATE TABLE IF NOT EXISTS DanhSach"+
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "TenDB VARCHAR(100),"+
                "SDT VARCHAR(100) )";
        DB.execSQL(sql);
    }

    private  void createOpentDatabse()
    {
        DB = openOrCreateDatabase("qldb.sqlite",MODE_PRIVATE,null);
    }

    // show danh sách bạn bè
    private void ShowDanhBa()
    {
        cursor = DB.rawQuery("SELECT * FROM DanhSach ORDER BY TenDB ASC",null);
        if(cursor!=null)
        {
            danhBaArrayList.clear();
            while (cursor.moveToNext())
            {
                String[] alpha = { "#8a2be2", "#7fff00","#d2691e","#ff1493","#f08080","#778899","#cd853f" };
                ArrayList list = new ArrayList();
                for (int i = 0; i<7; i++)
                {
                    list.add(alpha[i]);
                }
                Collections.shuffle(list);

                int id = cursor.getInt(0);
                String tenDB = cursor.getString(1);
                String sdt = cursor.getString(2);

                String tenKt = String.valueOf(tenDB.charAt(0));

                danhBaArrayList.add(new DanhBa(id,tenKt,list.get(1).toString(),tenDB,sdt));
            }
            danhBaAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(this, "Data is empty!", Toast.LENGTH_SHORT).show();
        }
    }

    // cái này là cá nhân
    private void CaNhan() {
        createOpentDatabse();
      //  cursor = null;
        MakteTableCaNhan();
       ShowCaNhan();
        txtTenBanKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                player.start();
                ThemCaNhan();
            }
        });

        txtTenBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                player.start();
                ThemCaNhan();
            }
        });
    }

    // thêm thông tin cá nhân , nếu đã thêm rồi thì chuyển sang cập nhật
    private void ThemCaNhan()
    {
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.themdanhba);
        Button btnNo = (Button) dialog.findViewById(R.id.buttonHuy);
        Button btnYes = (Button) dialog.findViewById(R.id.buttonLuu);
        final EditText tenDB = dialog.findViewById(R.id.editTextTenDanhBa);
        final EditText sdt   = dialog.findViewById(R.id.editTextSoDienThoai);
        TextView tieude = dialog.findViewById(R.id.textViewTenTieuDe);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                player.start();
                dialog.dismiss();
            }
        });

        cursor = DB.rawQuery("SELECT * FROM CaNhan",null);
            if (cursor.moveToNext())
            {
                // cập nhật cá nhân nếu đã thêm r
                tieude.setText("Cá Nhân");
                tenDB.setText(cursor.getString(1));
                sdt.setText(cursor.getString(2));
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                        player.start();
                        if(tenDB.getText().toString().isEmpty() || sdt.getText().toString().isEmpty() )
                        {
                            Toast.makeText(MainActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {   int idUpdate =  cursor.getInt(0);
                            String newTen = tenDB.getText().toString().trim();
                            String newSDT = sdt.getText().toString().trim();
                            ContentValues values = new ContentValues();
                            values.put("TenCN",newTen);
                            values.put("SDTCN",newSDT);
                            String[] whereArgs = {idUpdate+""};
                            DB.update("CaNhan",values,"Id=?",whereArgs);
                            Toast.makeText(MainActivity.this, "Sửa Thành Công!!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            ShowCaNhan();
                        }
                    }
                });
            }
        else
        {

            // thêm cá nhân khi chưa có dữ liệu, nếu có dữ liệu cá nhân rồi,thì cập nhật lại
            tieude.setText("Thêm Cá Nhân");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ting);
                    player.start();
                    if(tenDB.getText().toString().isEmpty() || sdt.getText().toString().isEmpty() )
                    {
                        Toast.makeText(MainActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ContentValues values = new ContentValues();
                        values.put("TenCN",tenDB.getText().toString().trim());
                        values.put("SDTCN",sdt.getText().toString().trim());
                        DB.insert("CaNhan","_Id",values);
                        Toast.makeText(MainActivity.this, "Thêm Thành Công!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        ShowCaNhan();
                    }
                }
            });
        }

        dialog.show();
    }

    // tạo bảng cá nhân, lưu thông tin cá nhân
    private void MakteTableCaNhan() {
        String sql = "CREATE TABLE IF NOT EXISTS CaNhan"+
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "TenCN VARCHAR(100),"+
                "SDTCN VARCHAR(100) )";
        DB.execSQL(sql);
    }


    // show ra thông tin cá nhân
    private void ShowCaNhan()
    {
        cursor = DB.rawQuery("SELECT * FROM CaNhan",null);
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String tenCN = cursor.getString(1);
                String sdt = cursor.getString(2);
                String tenKT = String.valueOf(tenCN.charAt(0));
                txtTenBan.setText(tenCN);
                txtTenBanKT.setText(tenKT);
            }
        }
    }

    // cập nhật thông tin danh bạ + kết hợp cái code bên DanhBaAdapter.java ... code bên DanhBaDapter gọi hàm này .
    public static void UpDateDanhBa(final Context context,int id)
    {
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.themdanhba);
        Button btnNo = (Button) dialog.findViewById(R.id.buttonHuy);
        Button btnYes = (Button) dialog.findViewById(R.id.buttonLuu);
        final EditText tenDB = dialog.findViewById(R.id.editTextTenDanhBa);
        final EditText sdt   = dialog.findViewById(R.id.editTextSoDienThoai);
        TextView tieude = dialog.findViewById(R.id.textViewTenTieuDe);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                player.start();
                dialog.dismiss();
            }
        });

        cursor = DB.rawQuery("SELECT * FROM DanhSach WHERE Id ="+id+"",null);
        if (cursor.moveToNext())
        {
            tieude.setText("Cá Nhân");
            tenDB.setText(cursor.getString(1));
            sdt.setText(cursor.getString(2));
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                    player.start();
                    if(tenDB.getText().toString().isEmpty() || sdt.getText().toString().isEmpty() )
                    {
                        Toast.makeText(context, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {   int idUpdate =  cursor.getInt(0);
                        String newTen = tenDB.getText().toString().trim();
                        String newSDT = sdt.getText().toString().trim();
                        ContentValues values = new ContentValues();
                        values.put("TenDB",newTen);
                        values.put("SDT",newSDT);
                        String[] whereArgs = {idUpdate+""};
                        DB.update("DanhSach",values,"Id=?",whereArgs);
                        Toast.makeText(context, "Sửa Thành Công!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                       Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
        dialog.show();
    }

    // tương tự cập nhật danh bạ
    public static void DeleteDanhBa(final Context context, final int id, String ten)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông Báo Xóa");
        builder.setMessage("Bạn có muốn xóa "+ten+" ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                player.start();
                if(cursor == null)
                {
                    Toast.makeText(context, "Data is empty", Toast.LENGTH_SHORT).show();
                }else
                {
                    String[] whereArgs = {id+""};
                    DB.delete("DanhSach","Id=?",whereArgs);
                    Toast.makeText(context, "Xóa Thành Công!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                player.start();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    // tìm kiếm, tương tự cập,xóa danh bạ
    private void TimKiem() {
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filter(s.toString());
            }
        });
    }

    //tìm kiếm
    private void filter(String text) {
        ArrayList<DanhBa> list = new ArrayList<>();
        for (DanhBa item: danhBaArrayList)
        {
            if(item.getTenNguoi().toLowerCase().contains(text.toLowerCase())) {
                list.add(item);
            }

            if(item.getSdt().contains(text.toLowerCase()))
                {
                    list.add(item);
                }
            }
        danhBaAdapter.filterList(list);
    }

}
