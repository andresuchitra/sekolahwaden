package com.andresuchitra.sekolahku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvTanggalLahir;
    TextView tvGender;
    TextView tvNoHp;
    TextView tvAlamat;
    TextView tvEmail;
    TextView tvHobi;
    TextView tvJenjang;

    DbHelper dbHelper = new DbHelper(DetailActivity.this, "sekolahku", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Pengambilan kirima ID dari MainActivity harus sama dengan detail activity
        int id = getIntent().getIntExtra("studentId", 0);

        if(id > 0 )
        {
            //casting to local text views
            tvName = findViewById(R.id.tv_detail_nama);
            tvNoHp = findViewById(R.id.tv_detail_no_hp);
            tvEmail = findViewById(R.id.tv_detail_email);
            tvAlamat = findViewById(R.id.tv_detail_alamat);
            tvGender = findViewById(R.id.tv_detail_gender);
            tvHobi = findViewById(R.id.tv_detail_hobi);
            tvJenjang = findViewById(R.id.tv_detail_jenjang);
            tvTanggalLahir = findViewById(R.id.tv_detail_tanggal_lahir);

            //Initiate student dengan memanggail dbHelper getStudent (sql query)
            Student student = dbHelper.getStudent(id);

            //set View Data
            tvName.setText(student.getNamaDepan()+" "+student.getNamaBelakang());
            tvTanggalLahir.setText(student.getTanggalLahir());
            tvJenjang.setText(student.getJenjang());
            tvGender.setText(student.getGender());
            tvAlamat.setText(student.getAlamat());
            tvEmail.setText(student.getEmail());
            tvNoHp.setText(student.getNoHp());
            tvHobi.setText(student.getHobi());

        }

    }
}
