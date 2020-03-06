package com.example.comicreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.comicreader.Adapter.MyChapterAdapter;
import com.example.comicreader.Common.Common;
import com.example.comicreader.Model.Comic;

public class ChapterActivity extends AppCompatActivity {

    RecyclerView recycler_chapter;
    TextView txt_chapter_name;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        //View
        txt_chapter_name=findViewById(R.id.txt_chapter_name);
        recycler_chapter=findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.comicSelected.Name);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchChapter(Common.comicSelected);
    }

    private void fetchChapter(Comic comicSelected) {
        Common.chapterList = comicSelected.Chapters;
        recycler_chapter.setAdapter(new MyChapterAdapter(this,comicSelected.Chapters));
        txt_chapter_name.setText(new StringBuilder("CHAPTERS (").append(comicSelected.Chapters.size())
        .append(")"));
    }
}