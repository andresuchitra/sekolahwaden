package com.andresuchitra.sekolahku;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Customer Array Adapter for Student object, to be shown in ListView class
 */
public class StudentItemAdapter extends ArrayAdapter<Student>{

    public StudentItemAdapter(@NonNull Context context, @NonNull List<Student> objects) {
        super(context, R.layout.item_student, objects);
    }

    // penggunaan method getView untuk menampilkan layout item_student serta mengubah value View
    // sesuai dengan object Student
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
        {
            LayoutInflater inflater =LayoutInflater.from(getContext());
            listItemView = inflater.inflate(R.layout.item_student,parent,false);
        }

        //casting view ke internal variable
        TextView tvNama = listItemView.findViewById(R.id.tv_item_nama);
        TextView tvNoHp = listItemView.findViewById(R.id.tv_item_no_hp);
        TextView tvGender = listItemView.findViewById(R.id.tv_item_gender);
        TextView tvJenjang = listItemView.findViewById(R.id.tv_item_jenjang);

        //get reference to current student object in Adapter's own list (list of Students)
        Student itemStudent = getItem(position);

        //Set value pada view
        tvNama.setText(itemStudent.getNamaDepan()+" "+itemStudent.getNamaBelakang());
        tvNoHp.setText(itemStudent.getNoHp());
        tvGender.setText(itemStudent.getGender());
        tvJenjang.setText(itemStudent.getJenjang());

        return listItemView;
    }
}
