package com.owosuperadmin.owoshop;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

public class ManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        setTitle("Shops");

        recyclerView=findViewById(R.id.management_recyclerviewid);

        //here will be code for firebase recycler adapter and it will use management_sample.xml to inflate
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView= (SearchView) item.getActionView();
        return super.onCreateOptionsMenu(menu);
    }
}
