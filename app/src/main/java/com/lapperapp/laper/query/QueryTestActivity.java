package com.lapperapp.laper.query;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lapperapp.laper.R;

public class QueryTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_submit);


//        List<CategoryModeln> list = new ArrayList<>();
//        TagView<CategoryModeln> tags = findViewById(R.id.tagview);
//
//        list.add(new CategoryModeln("Python","",""));
//        list.add(new CategoryModeln("C++","",""));
//        list.add(new CategoryModeln("Java","",""));
//        list.add(new CategoryModeln("Rust","",""));
//        list.add(new CategoryModeln("Python","",""));
//        list.add(new CategoryModeln("C++","",""));
//        list.add(new CategoryModeln("Java","",""));
//        list.add(new CategoryModeln("Rust","",""));
//
//        tags.setTags(list, new DataTransform<CategoryModeln>() {
//            @NotNull
//            @Override
//            public String transfer(CategoryModeln item) {
//                return item.name;
//            }
//        });
//
//        tags.setClickListener(new TagView.TagClickListener<CategoryModeln>() {
//            @Override
//            public void onTagClick(CategoryModeln item) {
//                item.getId();
//                item.name = "done";
//                Toast.makeText(QueryTestActivity.this, "> "+item.name, Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}