package com.example.chef;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class viewholderMenu extends RecyclerView.ViewHolder{



    TextView dishname;
    TextView price;
    TextView describe;
    TextView rating;
    ImageView imgpath;
    Button edit;
    Button delete;


    public viewholderMenu(@NonNull View itemView) {
        super(itemView);

        this.dishname = itemView.findViewById(R.id.tv_menu_name);
        this.price = itemView.findViewById(R.id.tv_menu_price);
        this.describe = itemView.findViewById(R.id.tv_menu_describe);
        this.imgpath = itemView.findViewById(R.id.img_menu_item);
        this.edit = itemView.findViewById(R.id.btn_menu_edit);
        this.delete = itemView.findViewById(R.id.btn_menu_delete);
        this.rating = itemView.findViewById(R.id.tv_menu_raiting);

    }

}
