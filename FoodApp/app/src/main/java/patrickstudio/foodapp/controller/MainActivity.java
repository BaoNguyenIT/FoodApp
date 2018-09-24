package patrickstudio.foodapp.controller;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import patrickstudio.foodapp.Helper.GoogleApiHelper;
import patrickstudio.foodapp.R;
import patrickstudio.foodapp.model.FoodType;
import patrickstudio.foodapp.adapter.FoodTypeAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private final static String KIND_EXTRAS ="kind";
    private List<FoodType> data;
    private RecyclerView recyclerView;
    private FirebaseUser mUser;
    private NavigationView mNavigationView;
    private CircularImageView mAvatarNav;
    private TextView mNameNav;
    private TextView mEmailNav;
    private Toolbar toolbar;
    private Menu mMenu;
    private GoogleApiHelper mGoogleApiHelper;
    private String kindIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        init();
        getReferenceFromXml();
        // toolbar thay thế action bar
        setSupportActionBar(toolbar);
        data = new ArrayList<>();
        data.add(new FoodType("Vegetarian", "Số 3 đường lê thánh tông", "Total 10", R.drawable.vetterian_food_img));
        data.add(new FoodType("SeaFood", "Số 4 lê đại hành", "Total 15", R.drawable.sea_food_img));
        data.add(new FoodType("Drinking", "Sô 5 lê minh đảo", "Total 16", R.drawable.drinking_img));
        FoodTypeAdapter adapter = new FoodTypeAdapter(data, new FoodTypeAdapter.ListItemClickListener() {
            @Override
            public void onListItemClickListenter(int clickedItemIndex) {
                switch (clickedItemIndex){
                    case 0:
                        kindIndex = "Vegetarian" ;
                        break;
                    case 1:
                        kindIndex = "SeaFood";
                        break;
                    case 2:
                        kindIndex = "Drinking";
                        break;
                    default:
                        Log.i("Information", "error! not found this type");

                }
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(KIND_EXTRAS, kindIndex);
                startActivity(intent);
            }
        }, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager glm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(glm);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                MenuItem item = mMenu.getItem(0);
                MenuItem item1 = mMenu.getItem(1);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    item.setTitle("Đăng nhập");
                    item.setIcon(R.drawable.login_icon);
                    mAvatarNav.setImageResource(R.drawable.user_icon_2);
                    mNameNav.setText("user");
                    mEmailNav.setText("user@mail.com");
                    item1.setVisible(false);
                }else {
                    item.setTitle("Đăng xuất");
                    item.setIcon(R.drawable.logout_icon);
                    Picasso.with(MainActivity.this).load(user.getPhotoUrl().toString()).into(mAvatarNav);
                    mNameNav.setText(user.getDisplayName());
                    mEmailNav.setText(user.getEmail());
                    item1.setVisible(true);
                }
            }
        });
        setUpNavigationDrawer();
    }

    private void init(){
        mGoogleApiHelper = GoogleApiHelper.getInstance(this);
    }

    public void getReferenceFromXml()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        LinearLayout linearLayout = (LinearLayout) mNavigationView.getHeaderView(0);
        mAvatarNav = (CircularImageView) linearLayout.findViewById(R.id.avatar_nav);
        mNameNav = (TextView) linearLayout.findViewById(R.id.name_nav);
        mEmailNav = (TextView) linearLayout.findViewById(R.id.email_nav);
    }

    public void setUpNavigationDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mMenu = mNavigationView.getMenu();
        MenuItem item = mMenu.getItem(0);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser == null){
            item.setTitle("Đăng nhập");
            item.setIcon(R.drawable.login_icon);
        }else {
            item.setTitle("Đăng xuất");
            item.setIcon(R.drawable.logout_icon);
        }
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            if(mUser == null){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                FirebaseAuth.getInstance().signOut();
                mGoogleApiHelper.signOut();
                LoginManager.getInstance().logOut();
            }
        } else if (id == R.id.nav_pending) {
            Intent intent = new Intent(MainActivity.this, UserHistoryActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
