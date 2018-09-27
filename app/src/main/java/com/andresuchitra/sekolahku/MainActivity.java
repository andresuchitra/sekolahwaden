package com.andresuchitra.sekolahku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Declare Class for DbHelper and Student
    DbHelper dbHelper = new DbHelper(MainActivity.this, "sekolahku", null, 1);
    Student student = new Student();
    List<Student> studentList = new ArrayList<>();
    // Step 22: Declare list view to show student list
    ListView lvStudentListView;
    // Create Student Adapter to allow showing list of Studens in MainActivity's ListView object
    StudentItemAdapter studentAdapter;
    //Search View
    SearchView studentSearchView;
    // scroll arrow
    ImageView ivScrollArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //casting list view (casting can be on create)
        lvStudentListView = findViewById(R.id.lv_student_list);
        studentSearchView = findViewById(R.id.sv_search_student);
        ivScrollArrow = findViewById(R.id.img_scroll_indicator);

        //Log.d("MainActivity","=== DEBUG === Jumlah Siswa: "+studentList.size()+" orang");

        //membuat listview mendapatkan akses ContextMenu
        registerForContextMenu(lvStudentListView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // get object of Students first, before creating Adapter for ListView
        studentList = dbHelper.getAllStudent();
        // assign student adapter with to list view object, and the actual array list of Students
        studentAdapter = new StudentItemAdapter(MainActivity.this, studentList);
        lvStudentListView.setAdapter(studentAdapter);

        // set listView item such that click will move to Detail Activity
        lvStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // need to know which student being selected
                student = studentAdapter.getItem(i);

                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                // additional information passed from list view for Read DB access when opening Detail
                // activity
                detailIntent.putExtra("studentId", student.getId());
                // memulai Detail intent
                startActivity(detailIntent);
            }
        });

        // set search view to listen to click event
        studentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // onQueryTextSubmit - active when user click enter, usually for internet app
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // onQueryTextChange - active when there is any text change in the query view
            @Override
            public boolean onQueryTextChange(String newText) {
                studentList.clear();
                studentList.addAll(dbHelper.searchStudent(newText));
                studentAdapter.notifyDataSetChanged();
                return false;
            }
        });

        // handle the visibility of scroll down arrow in every swipe
        // to hide the arrow once bottom of the visible view is reached
        lvStudentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if(lvStudentListView != null && lvStudentListView.getChildAt(lvStudentListView.getChildCount() - 1) != null) {
                        if (lvStudentListView.getLastVisiblePosition() ==
                                lvStudentListView.getAdapter().getCount() - 1 &&
                                lvStudentListView.getChildAt(lvStudentListView.getChildCount() - 1).getBottom() <= lvStudentListView.getHeight()) {
                            ivScrollArrow.setVisibility(View.INVISIBLE);
                        } else {
                            ivScrollArrow.setVisibility(View.VISIBLE);
                        }
                }
            }
        });

    }

    //

    // Step 21: Membuat menu pada activity - SOP code utk bikin menu
    // onCreateOptionsMenu -> utk menampilkan menu pada Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menampilkan menu main
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // onOptionsItemSelected - membaca pilihan user pada menu, serta melakukan action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Membaca pilihan user
        switch (item.getItemId()) {
            //If user selects actions tambah (yg telah di bikin di menu_main.xml
            case R.id.action_tambah:
                //action disini untuk pindah ke FormActivity
                Intent intentAddStudent = new Intent(getApplicationContext(), FormActivity.class);
                startActivity(intentAddStudent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * call Contextmenu pop up
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_main, menu);
    }

    // membaca pilihan user di ListView, dan mendapatkan posisi entry di list view
    // lalu dikasih ke object student
    // utk mendapatkan current Id dan pass Id ini ke DB nanti
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        student = studentList.get(info.position);

        // deteksi pilihan context menu user:  edit or delete
        switch (item.getItemId()) {
            case R.id.action_edit:
                // move to FormActivity to perform update
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                // add student id info to the new activity
                intent.putExtra("studentId", student.getId());
                Log.d("MainActivity", "Student ID for edit: " + student.getId());
                startActivity(intent);
                break;

            case R.id.action_delete:
                // delete in database
                dbHelper.deleteStudent(student);
                // delete from main student list object in MainActivity
                studentList.remove(student);
                studentAdapter.notifyDataSetChanged();
                // create Toast for succesful delete
                Toast.makeText(MainActivity.this, "Record has been deleted successfully!", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onContextItemSelected(item);
    }
}
