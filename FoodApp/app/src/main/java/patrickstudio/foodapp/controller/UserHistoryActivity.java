package patrickstudio.foodapp.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import patrickstudio.foodapp.R;
import patrickstudio.foodapp.adapter.ListFoodHistoryAdapter;
import patrickstudio.foodapp.model.FoodItem;

public class UserHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private FirebaseUser user;
    private DatabaseReference mRef;
    private List<FoodItem> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);
        init();
        getReferenceFromXml();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FoodItem item = new FoodItem();
                item.setName((String) dataSnapshot.child("name").getValue());
                item.setCost((String) dataSnapshot.child("price").getValue());
                item.setLogo((String) dataSnapshot.child("logo").getValue());
                item.setUserId((String) dataSnapshot.child("userid").getValue());
                item.setType((String) dataSnapshot.child("kind").getValue());
                itemList.add(item);
                Log.i("Check", "so dong la:" + Integer.toString(itemList.size()));
                ListFoodHistoryAdapter adapter = new ListFoodHistoryAdapter(itemList, UserHistoryActivity.this);
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager glm = new GridLayoutManager(UserHistoryActivity.this, 1);
                recyclerView.setLayoutManager(glm);

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
        mRef.addChildEventListener(listener);

    }
    private void init(){
        itemList = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRef = database.child("user").child(user.getUid());
    }
    private void getReferenceFromXml(){
        recyclerView = (RecyclerView) findViewById(R.id.user_history_recycler_view);
    }
}
