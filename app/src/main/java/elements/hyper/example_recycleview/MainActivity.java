package elements.hyper.example_recycleview;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import adapters.SampleAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import listeners.EndlessGridScrollListener;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    View header;
    private SampleAdapter sampleAdapter;
    //private LinearLayoutManager linearLayoutManager;//Use this for LinearLayoutManager
    private GridLayoutManager gridLayoutManager;//Use this for GridLayoutManager
    private String TAG = "MAINACTIVITY ";
    public static ArrayList<String> arrayList;
    FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        prepareData();

        //recyclerView.setHasFixedSize(true);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;//
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);//Use this for GridLayoutManager
        //recyclerView.setLayoutManager(linearLayoutManager);//Use this for LinearLayoutManager

        sampleAdapter.setHeader(header);
        final SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(sampleAdapter);
        alphaAdapter.setFirstOnly(true);

        recyclerView.setAdapter(alphaAdapter);
        //recyclerView.setAdapter(sampleAdapter);

        recyclerView.addOnScrollListener(new EndlessGridScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.e("Load more","Current page"+current_page);
            }
        });

        sampleAdapter.setOnItemClickListener(new SampleAdapter.onClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                position = position+1;//As we are adding header
                Log.e(TAG + "ON ITEM CLICK", position + "");
                Snackbar.make(v, "On item click "+position, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(int position, View v) {
                position = position+1;//As we are adding header
                Log.e(TAG + "ON ITEM LONG CLICK", position + "");
                Snackbar.make(v, "On item longclick  "+position, Snackbar.LENGTH_LONG).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(alphaAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
    }



    private void init(){
        header = LayoutInflater.from(this).inflate(R.layout.custom_recycler_row_sample_header, recyclerView, false);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        gridLayoutManager = new GridLayoutManager(this, 2);
        //linearLayoutManager = new LinearLayoutManager(getApplicationContext());//Use this for LinearLayoutManager
        arrayList = new ArrayList<>();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        sampleAdapter = new SampleAdapter(getApplicationContext());//Use this for GridLayoutManager
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
    }

    private void prepareData() {
     for(int i = 0; i<200; i++){
         arrayList.add(i+" POS ");
     }
    }
}
