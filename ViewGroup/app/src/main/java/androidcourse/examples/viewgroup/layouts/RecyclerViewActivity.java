package androidcourse.examples.viewgroup.layouts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;

import androidcourse.example.viewgroup.layouts.R;

public class RecyclerViewActivity extends AppCompatActivity {

    private static final String TAG = "RecyclerViewActivity";

    private ArrayList<String> mImageText = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Log.d(TAG, "new List started");
        grabImages();
    }

    private void grabImages()
    {
        Log.d(TAG, "adding images");

        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/11/09/50/emoji-1585197_960_720.png");
        mImageText.add("angry emoji");
        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/21/18/48/emoticon-1610518_960_720.png");
        mImageText.add("cool emoji");
        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/11/10/52/emoji-1585401_960_720.png");
        mImageText.add("embarrassed emoji");
        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/29/11/55/emoticon-1628080_960_720.png");
        mImageText.add("spectacle emoji");
        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/22/10/17/emoticon-1611647_960_720.png");
        mImageText.add("shhh emoji");
        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/31/20/04/emoticon-1634515_960_720.png");
        mImageText.add("sad emoji");
        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/21/19/27/emoticon-1610573_960_720.png");
        mImageText.add("excited emoji");

        startRecyclerView();
    }

    private void startRecyclerView(){
        Log.d(TAG, "Starting recycler view");
        RecyclerView recyclerView = findViewById(R.id.recyclerView_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, mImageText, mImageUrls);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
