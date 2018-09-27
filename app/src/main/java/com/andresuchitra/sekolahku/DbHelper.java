package com.andresuchitra.sekolahku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Step 8: Membuat Class DbHelper
 */
public class DbHelper extends SQLiteOpenHelper {

    // Step 8.1 : Declare SQLiteDatabase object
    private SQLiteDatabase sqLiteDatabase;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Step 9: Implement onCreate method of SQLiteOpenHelper
    // with table creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        //nama Sql object kecil semua (lowercase)
        db.execSQL(
                "CREATE TABLE student (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nama_depan TEXT," +
                        "nama_belakang TEXT," +
                        "no_hp TEXT," +
                        "gender TEXT," +
                        "jenjang TEXT," +
                        "hobi TEXT," +
                        "alamat TEXT," +
                        "email TEXT," +
                        "tanggal_lahir)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    /**
     * Step 11: Add Student method to allow insertion to SQLite
     */
    public void addStudent(Student student) {
        //create content for student table fields. Must provide ALL Fields declared by table
        ContentValues cv = new ContentValues();
        cv.put("nama_depan", student.getNamaDepan());
        cv.put("nama_belakang", student.getNamaBelakang());
        cv.put("no_hp", student.getNoHp());
        cv.put("gender", student.getGender());
        cv.put("jenjang", student.getJenjang());
        cv.put("hobi", student.getHobi());
        cv.put("alamat", student.getAlamat());
        // new fields
        cv.put("email", student.getEmail());
        cv.put("tanggal_lahir", student.getTanggalLahir());

        //buka akses database
        sqLiteDatabase = getWritableDatabase();
        //insert to table
        sqLiteDatabase.insert("student",null, cv);
        //close sql connection
        sqLiteDatabase.close();
    }

    // Update student record
    public void updateStudent(Student student)
    {
        //create content for student table fields. Must provide ALL Fields declared by table
        ContentValues cv = new ContentValues();
        cv.put("nama_depan", student.getNamaDepan());
        cv.put("nama_belakang", student.getNamaBelakang());
        cv.put("no_hp", student.getNoHp());
        cv.put("gender", student.getGender());
        cv.put("jenjang", student.getJenjang());
        cv.put("hobi", student.getHobi());
        cv.put("alamat", student.getAlamat());
        cv.put("email", student.getEmail());
        cv.put("tanggal_lahir", student.getTanggalLahir());

        //buka akses database
        sqLiteDatabase = getWritableDatabase();
        //update sql query
        sqLiteDatabase.update("student", cv, "id=?", new String[]{String.valueOf(student.getId())});
        //close sql connection
        sqLiteDatabase.close();
    }

    // get function to get number of total records in Student table
    public List<Student> getAllStudent(){
        sqLiteDatabase = getWritableDatabase();
        // Cursor wajib untuk read process
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student", new String[]{});
        cursor.moveToFirst();

        List<Student> studentList = new ArrayList<>();
        while(!cursor.isAfterLast())
        {
            Student student = new Student();
            student.setId(cursor.getInt(cursor.getColumnIndex("id")));
            student.setNamaDepan(cursor.getString(cursor.getColumnIndex("nama_depan")));
            student.setNamaBelakang(cursor.getString(cursor.getColumnIndex("nama_belakang")));
            student.setNoHp(cursor.getString(cursor.getColumnIndex("no_hp")));
            student.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            student.setJenjang(cursor.getString(cursor.getColumnIndex("jenjang")));
            student.setHobi(cursor.getString(cursor.getColumnIndex("hobi")));
            student.setAlamat(cursor.getString(cursor.getColumnIndex("alamat")));
            student.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            student.setTanggalLahir(cursor.getString(cursor.getColumnIndex("tanggal_lahir")));

            studentList.add(student);
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return studentList;
    }

    public Student getStudent(int id)
    {
        sqLiteDatabase = getWritableDatabase();
        // Cursor wajib untuk read process
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student WHERE id = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();

        //declar Student object to be returned to DetailActivity
        Student student = new Student();
        student.setId(cursor.getInt(cursor.getColumnIndex("id")));
        student.setNamaDepan(cursor.getString(cursor.getColumnIndex("nama_depan")));
        student.setNamaBelakang(cursor.getString(cursor.getColumnIndex("nama_belakang")));
        student.setNoHp(cursor.getString(cursor.getColumnIndex("no_hp")));
        student.setGender(cursor.getString(cursor.getColumnIndex("gender")));
        student.setJenjang(cursor.getString(cursor.getColumnIndex("jenjang")));
        student.setHobi(cursor.getString(cursor.getColumnIndex("hobi")));
        student.setAlamat(cursor.getString(cursor.getColumnIndex("alamat")));
        student.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        student.setTanggalLahir(cursor.getString(cursor.getColumnIndex("tanggal_lahir")));

        sqLiteDatabase.close();
        cursor.close();
        return student;
    }

    public List<Student> searchStudent (String searchQuery)
    {
        sqLiteDatabase = getWritableDatabase();
        // Pakai SQL query based on search query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student WHERE " +
                "nama_depan LIKE ? OR " +
                "nama_belakang LIKE ? OR " +
                "alamat LIKE ? OR " +
                "email LIKE ? OR " +
                "hobi LIKE ? OR " +
                "gender LIKE ?",
                new String[]{
                        "%"+ searchQuery +"%",
                        "%"+ searchQuery +"%",
                        "%"+ searchQuery +"%",
                        "%"+ searchQuery +"%",
                        "%"+ searchQuery +"%",
                        "%"+ searchQuery +"%",
        });
        cursor.moveToFirst();

        List<Student> studentList = new ArrayList<>();
        while(!cursor.isAfterLast())
        {
            Student student = new Student();
            student.setId(cursor.getInt(cursor.getColumnIndex("id")));
            student.setNamaDepan(cursor.getString(cursor.getColumnIndex("nama_depan")));
            student.setNamaBelakang(cursor.getString(cursor.getColumnIndex("nama_belakang")));
            student.setNoHp(cursor.getString(cursor.getColumnIndex("no_hp")));
            student.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            student.setJenjang(cursor.getString(cursor.getColumnIndex("jenjang")));
            student.setHobi(cursor.getString(cursor.getColumnIndex("hobi")));
            student.setAlamat(cursor.getString(cursor.getColumnIndex("alamat")));
            student.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            student.setTanggalLahir(cursor.getString(cursor.getColumnIndex("tanggal_lahir")));

            studentList.add(student);
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return studentList;
    }

    public void deleteStudent(Student student){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("student","id=?",new String[]{String.valueOf(student.getId())});
        sqLiteDatabase.close();

    }
}
