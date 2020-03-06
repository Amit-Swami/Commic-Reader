package com.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicreader.Adapter.MyViewPagerAdapter;
import com.example.comicreader.Common.Common;
import com.example.comicreader.Model.Chapter;
import com.example.comicreader.R;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

public class ViewComicActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView txt_chapter_name;
    View back,next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        viewPager=findViewById(R.id.view_pager);
        txt_chapter_name=findViewById(R.id.txt_chapter_name);
        back=findViewById(R.id.chapter_back);
        next=findViewById(R.id.chapter_next);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.chapterIndex == 0)
                    Toast.makeText(ViewComicActivity.this, "You are reading first chapter", Toast.LENGTH_SHORT).show();
                else
                {
                    Common.chapterIndex--;
                    fetchLinks(Common.chapterList.get(Common.chapterIndex));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.chapterIndex == Common.chapterList.size()-1)
                    Toast.makeText(ViewComicActivity.this, "You are reading last chapter", Toast.LENGTH_SHORT).show();
                else
                {
                    Common.chapterIndex++;
                    fetchLinks(Common.chapterList.get(Common.chapterIndex));
                }
            }
        });
        fetchLinks(Common.chapterSelected);
    }

    private void fetchLinks(Chapter chapter) {
        if (chapter.Links != null)
        {
            if (chapter.Links.size()>0)
            {
                MyViewPagerAdapter adapter=new MyViewPagerAdapter(getBaseContext(),chapter.Links);
                viewPager.setAdapter(adapter);

                txt_chapter_name.setText(Common.formatString(Common.chapterSelected.Name));

                //Animation
                BookFlipPageTransformer bookFlipPageTransformer=new BookFlipPageTransformer();
                bookFlipPageTransformer.setScaleAmountPercent(10f);
                viewPager.setPageTransformer(true,bookFlipPageTransformer);
            }
        }
        else {
            Toast.makeText(this, "No image here", Toast.LENGTH_SHORT).show();
        }
    }
}
