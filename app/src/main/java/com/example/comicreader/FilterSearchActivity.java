package com.example.comicreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicreader.Adapter.MyComicAdapter;
import com.example.comicreader.Common.Common;
import com.example.comicreader.Model.Comic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        recycler_filter_search=findViewById(R.id.recycler_filter_search);
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(this,2));

        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.action_filter:
                        showFiltersDialog();
                        break;
                    case R.id.action_search:
                        showSearchDialog();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Search");

        final LayoutInflater inflater=this.getLayoutInflater();
        View search_layout=inflater.inflate(R.layout.dialog_search,null);

        final EditText edt_search = search_layout.findViewById(R.id.edt_search);

        alertDialog.setView(edt_search);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                  fetchSearchComic(edt_search.getText().toString());
            }
        });

        alertDialog.show();
    }

    private void fetchSearchComic(String query) {
        List<Comic> comic_search = new ArrayList<>();
        for (Comic comic:Common.comicList)
        {
            if (comic.Name.contains(query))
                comic_search.add(comic);
        }
        if (comic_search.size()>0)
            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(),comic_search));
        else
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
    }

    private void showFiltersDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Select Category");

        final LayoutInflater inflater=this.getLayoutInflater();
        View filter_layout=inflater.inflate(R.layout.dialog_options,null);

        final AutoCompleteTextView txt_category=filter_layout.findViewById(R.id.txt_category);
        final ChipGroup chipGroup=filter_layout.findViewById(R.id.chipGroup);

        //create autocomplete
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.select_dialog_item, Common.categories);
        txt_category.setAdapter(adapter);
        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Clear
                txt_category.setText("");

                //create tags
                Chip chip= (Chip) inflater.inflate(R.layout.chip_item,null,false);
                chip.setText(((TextView)view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chipGroup.removeView(view);
                    }
                });
                chipGroup.addView(chip);
            }
        });

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");
                for (int j=0;j<chipGroup.getChildCount();j++)
                {
                    Chip chip= (Chip) chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }

                //Because in our Database , category will be sort from A-->Z and split by .
                //so we need sort our filter key
                Collections.sort(filter_key);
                //convert list to string
                for (String key:filter_key)
                {
                    filter_query.append(key).append(",");
                }
                //Remove last ","
                filter_query.setLength(filter_query.length()-1);

                //Filter by this query
                fetchFilterCategory(filter_query.toString());
            }
        });
        alertDialog.show();
    }

    private void fetchFilterCategory(String query) {
        List<Comic> comic_filtered = new ArrayList<>();
        for (Comic comic:Common.comicList) {
            if (comic.Category != null) {
                if (comic.Category.contains(query))
                    comic_filtered.add(comic);
            }
        }
        if (comic_filtered.size()>0)
            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(),comic_filtered));
        else
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
    }
}