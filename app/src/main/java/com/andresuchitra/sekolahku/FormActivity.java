package com.andresuchitra.sekolahku;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {

    /**
     * Step 1: Declare variable for the views in layout.xml
     */
    EditText etNamaDepan, etNamaBelakang, etNomorHp, etAlamat;
    //new fields: Email and birthdate
    EditText etEmail, etTanggalLahir;
    RadioButton rbPria, rbWanita;
    Spinner spinJenjang;
    CheckBox cbMembaca, cbMenulis, cbMenggambar;
    Button btnSimpan;

    //Step 4: Declare variable untuk values masing2 field
    String namaDepan, namaBelakang, nomorHp, alamat, gender, jenjang, hobi;
    //tambah email & tgl lahir
    String email, tanggalLahir;

    //Step 12: Declare DbHelper class and Student model object
    DbHelper dbHelper = new DbHelper(FormActivity.this, "sekolahku", null,
            1);
    Student student = new Student();

    // Step 18: Calender object for tanggal lahir view
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        /**
         * Step 19: Calender getInstance to get the current system date/time (default)
         */
        calendar = Calendar.getInstance();

        // Step 2: Casting View WAJIB setelah bikin View!!!
        etNamaDepan = findViewById(R.id.et_nama_depan);
        etNamaBelakang = findViewById(R.id.et_nama_belakang);
        etNomorHp = findViewById(R.id.et_nomor_hp);
        etAlamat = findViewById(R.id.et_alamat);
        rbPria = findViewById(R.id.rb_pria);
        rbWanita = findViewById(R.id.rb_wanita);
        spinJenjang = findViewById(R.id.spin_jenjang);
        cbMembaca = findViewById(R.id.cb_membaca);
        cbMenggambar = findViewById(R.id.cb_menggambar);
        cbMenulis = findViewById(R.id.cb_menulis);
        btnSimpan = findViewById(R.id.btn_simpan);
        etEmail = findViewById(R.id.et_email);
        etTanggalLahir = findViewById(R.id.et_tanggal_lahir);

        // Step 22++ Handle edit record task (from Edit context menu)
        int id = getIntent().getIntExtra("studentId", 0);
        // if id being passed from previous activity is 0, it means no edit request made before,
        // because id is generated from selected record in database (in listview). First record in
        // listview / database is always 1
        if( id > 0)
        {
            student = dbHelper.getStudent(id);

            //update the form view element
            etNamaDepan.setText(student.getNamaDepan());
            etNamaBelakang.setText(student.getNamaBelakang());
            etNomorHp.setText(student.getNoHp());
            etEmail.setText(student.getEmail());
            etAlamat.setText(student.getAlamat());
            etTanggalLahir.setText(student.getTanggalLahir());

            //utk membuat calendar otomatis set ke tgl dari record
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
            try {
                Date date = simpleDateFormat.parse(student.getTanggalLahir());
                calendar.setTime(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            // set radio button based on update query
            switch(student.getGender()){
                case "Pria":
                    rbPria.setChecked(true);
                    break;
                case "Wanita":
                    rbWanita.setChecked(true);
                    break;
            }

            // set hobi checkbox from update query data
            String hobi = student.getHobi();
            if(hobi.contains("Menggambar")){
                cbMenggambar.setChecked(true);
            }
            if(hobi.contains("Menulis")){
                cbMenulis.setChecked(true);
            }
            if(hobi.contains("Membaca")){
                cbMembaca.setChecked(true);
            }

            // set jenjang pendidikan based on update query
            // use array adapter to find position of Array resource (R.array.jenjang), then set
            // selection of Spinner view 'spinJenjang' to the position based on current student's
            // jenjang record
            ArrayAdapter<String> adapter = new ArrayAdapter<>(FormActivity.this, android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.jenjang));
            spinJenjang.setSelection(adapter.getPosition(student.getJenjang()));

        }


        // Step 17: Set edittext to listen for click event
        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Step 20: Show Date Picker dialog when user click tanggal lahir EditText
                final DatePickerDialog datePickerDialog = new DatePickerDialog(FormActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                               //ketika datepicker di set, set juga tanggal di calendar object
                                calendar.set(Calendar.YEAR, i);
                                calendar.set(Calendar.MONTH, i1);
                                calendar.set(Calendar.DAY_OF_MONTH, i2);

                                //create date format to show the selected date on calendar object
                                // string pattern is case sensitive, M = month, m = minute
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                                //update text di tanggal lahir view
                                etTanggalLahir.setText(sdf.format(calendar.getTime()));
                            }
                        },
                        // select current year
                        calendar.get(Calendar.YEAR),
                        // select current month
                        calendar.get(Calendar.MONTH),
                        // select current day of month
                        calendar.get(Calendar.DAY_OF_MONTH));
                // to show date picker dialog when clicked
                datePickerDialog.show();
            }
        });

        // Step 3: mengaktifkan click pada button SIMPAN (menyimpan record FORM)
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Step 5: Pengambilan values dari views
                namaDepan = etNamaDepan.getText().toString().trim();
                namaBelakang = etNamaBelakang.getText().toString().trim();
                nomorHp = etNomorHp.getText().toString().trim();
                alamat = etAlamat.getText().toString().trim();
                //add email & tgl lahir
                email = etEmail.getText().toString().trim();
                tanggalLahir = etTanggalLahir.getText().toString();

                // gender condition checked
                gender = "";
                if (rbPria.isChecked()) {
                    gender = "Pria";
                } else if (rbWanita.isChecked()) {
                    gender = "Wanita";
                }

                // checkbox from hobi view
                hobi = "";
                if (cbMembaca.isChecked()) {
                    hobi += (cbMembaca.getText().toString() + ", ");
                }
                if (cbMenggambar.isChecked()) {
                    hobi += (cbMenggambar.getText().toString() + ", ");
                }
                if (cbMenulis.isChecked()) {
                    hobi += (cbMenulis.getText().toString());
                }

                // get values from spinner jenjang
                jenjang = spinJenjang.getSelectedItem().toString();


                // validate klo ada mandatory field
                if (namaDepan.isEmpty()) {
                    etNamaDepan.setError("Nama depan wajib diisi!");
                    etNamaDepan.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    etEmail.setError("Email wajib dikasih!");
                    etEmail.requestFocus();
                    return;
                }
                if (tanggalLahir.isEmpty()) {
                    etTanggalLahir.setError("Tanggal Lahir wajib dikasih!");
                    etTanggalLahir.requestFocus();
                    return;
                }
                if (gender.isEmpty()) {
                    Toast.makeText(FormActivity.this, "Gender HARUS Dipilih!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinJenjang.getSelectedItemPosition() == 0) {
                    Toast.makeText(FormActivity.this, "Jenjang Pendidikan HARUS Dipilih!", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * Step 13: Assign form values to model and insert new record to SQLite - POJO
                 */
                // create Student model object
                student.setNamaDepan(namaDepan);
                student.setNamaBelakang(namaBelakang);
                student.setAlamat(alamat);
                student.setGender(gender);
                student.setJenjang(jenjang);
                student.setNoHp(nomorHp);
                student.setHobi(hobi);
                student.setEmail(email);
                student.setTanggalLahir(tanggalLahir);

                int studentId = getIntent().getIntExtra("studentId", 0);
                if(studentId >0)
                {
                    dbHelper.updateStudent(student);
                    Toast.makeText(FormActivity.this, "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbHelper.addStudent(student);
                    Toast.makeText(FormActivity.this, "Data Inserted Successfully!", Toast.LENGTH_SHORT).show();
                }
                // destroy main activity
                finish();
            }
        });
    }
}
