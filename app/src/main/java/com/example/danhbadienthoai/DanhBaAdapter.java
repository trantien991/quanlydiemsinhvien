package com.example.danhbadienthoai;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;
import static androidx.core.app.ActivityCompat.requestPermissions;

public class DanhBaAdapter extends RecyclerView.Adapter<DanhBaAdapter.MyHolder> {
    private Context context;
    private ArrayList<DanhBa> danhBaArrayList;
    public  static  final int REQUEST_CALL = 1;
    public DanhBaAdapter(Context context, ArrayList<DanhBa> danhBaArrayList) {
        this.context = context;
        this.danhBaArrayList = danhBaArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_danhba, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.TenNguoi.setText(danhBaArrayList.get(position).getTenNguoi());
        holder.txtkytu.setText(danhBaArrayList.get(position).getKytu());
        holder.txtkytu.setBackgroundColor(Color.parseColor(danhBaArrayList.get(position).getMau()));
        final int id = danhBaArrayList.get(position).getId();
        holder.giaodien.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.giaodien);
                popupMenu.getMenuInflater().inflate(R.menu.menu_sua_xoa,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.item1:  // cập nhật lại danh bạ bạn bè
                            {
                                MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                                player.start();
                                MainActivity.UpDateDanhBa(context,id);
                                break;
                            }
                            case R.id.item2: // xóa danh bạ bạn bè
                            {
                                MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                                player.start();
                                MainActivity.DeleteDanhBa(context,id, danhBaArrayList.get(position).getTenNguoi());
                                break;
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });


        holder.giaodien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.danhba_banbe);
                ImageButton btnCall = dialog.findViewById(R.id.imageButtonCall);
                ImageButton btnMess = dialog.findViewById(R.id.imageButtonMess);
                ImageButton btnBack = dialog.findViewById(R.id.imageButtonBack);
                final TextView tenDBBB = dialog.findViewById(R.id.textViewTenTieuDeDB);
                final TextView sdtDBBB   = dialog.findViewById(R.id.textViewTenTieuDeSDT);

                tenDBBB.setText(danhBaArrayList.get(position).getTenNguoi());
                sdtDBBB.setText(danhBaArrayList.get(position).getSdt());
                final String sdt = danhBaArrayList.get(position).getSdt();

                // gọi điện thoại
                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                        player.start();
                       String number = "tel:"+sdt;
                       MakeCall(number, context);


                    }
                });

                // gửi tin nhắn
                btnMess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                        player.start();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.putExtra("sms_body","");
                        intent.setData(Uri.parse("sms:"+sdt+""));
                        context.startActivity(intent);
                    }
                });

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer player = MediaPlayer.create(context, R.raw.ting);
                        player.start();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return danhBaArrayList.size();
    }
     public  class MyHolder extends RecyclerView.ViewHolder
    {
        TextView txtkytu;
        TextView TenNguoi;
        LinearLayout giaodien;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtkytu = itemView.findViewById(R.id.textViewKyTuDanhBa);
            TenNguoi= itemView.findViewById(R.id.textViewTenDanhBa);
            giaodien= itemView.findViewById(R.id.giaodien);

        }
    }


    // tìm kiếm
    public  void  filterList(ArrayList<DanhBa> filteredList)
    {
        danhBaArrayList = filteredList;
        notifyDataSetChanged();
    }

    // xin quyền và gọi điện thoại
    public  static  void MakeCall(String number, Context context)
    {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }
        else
        {
            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(number)));
        }
    }


}
