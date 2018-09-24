package patrickstudio.foodapp.controller;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import patrickstudio.foodapp.R;
import patrickstudio.foodapp.adapter.ListFoodAdapter;
import patrickstudio.foodapp.model.FoodItem;

public class DetailActivity extends AppCompatActivity {
    private final static String KIND_EXTRAS = "kind";
    private final static String NAME_EXTRAS = "name";
    private final static String DES_EXTRAS = "description";
    private final static String COST_EXTRAS = "cost";
    private final static String AMOUNT_EXTRAS = "amount";
    private final static String LOGO_EXTRAS = "logo";

    private FloatingActionButton addFab;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private List<FoodItem> itemList;
    private String kindIndex;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getInformationFromIntent();
        getReferenceFromXml();
        init();
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, AddFoodActivity.class);
                intent.putExtra(KIND_EXTRAS, kindIndex);
                startActivity(intent);
            }
        });
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FoodItem item = new FoodItem();
                item.setName((String) dataSnapshot.child("name").getValue());
                item.setCost((String) dataSnapshot.child("price").getValue());
                item.setDescription((String) dataSnapshot.child("description").getValue());
                item.setAmount((String) dataSnapshot.child("amount").getValue());
                item.setLogo((String) dataSnapshot.child("logo").getValue());
                item.setUserId((String) dataSnapshot.child("userid").getValue());
                item.setType(kindIndex);
                itemList.add(0, item);
                ListFoodAdapter adapter = new ListFoodAdapter(itemList, new ListFoodAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClickListenter(int clickedItemIndex) {
                        Intent intent = new Intent(DetailActivity.this, FoodDetailActivity.class);
                        intent.putExtra(NAME_EXTRAS, itemList.get(clickedItemIndex).getName());
                        intent.putExtra(DES_EXTRAS, itemList.get(clickedItemIndex).getDescription());
                        intent.putExtra(COST_EXTRAS, itemList.get(clickedItemIndex).getCost());
                        intent.putExtra(KIND_EXTRAS, itemList.get(clickedItemIndex).getType());
                        intent.putExtra(AMOUNT_EXTRAS, itemList.get(clickedItemIndex).getAmount());
                        intent.putExtra(LOGO_EXTRAS, itemList.get(clickedItemIndex).getLogo());
                        startActivity(intent);
                    }
                }, DetailActivity.this);
                mRecyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager glm = new GridLayoutManager(DetailActivity.this, 1);
                mRecyclerView.setLayoutManager(glm);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(childEventListener);
    }
    private void init(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mDatabase.child("data").child(kindIndex);
        itemList = new ArrayList<>();
    }

    private void getReferenceFromXml(){
        addFab = (FloatingActionButton) findViewById(R.id.add_fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.food_list_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(kindIndex);
    }

    private void getInformationFromIntent(){
        Intent intent = getIntent();
        kindIndex = intent.getStringExtra(KIND_EXTRAS);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
