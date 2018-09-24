package patrickstudio.foodapp.controller;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import patrickstudio.foodapp.R;
import patrickstudio.foodapp.model.FoodItem;

public class FoodDetailActivity extends AppCompatActivity {
    private final static String KIND_EXTRAS = "kind";
    private final static String NAME_EXTRAS = "name";
    private final static String DES_EXTRAS = "description";
    private final static String COST_EXTRAS = "cost";
    private final static String AMOUNT_EXTRAS = "amount";
    private final static String LOGO_EXTRAS = "logo";
    private Toolbar toolbar;
    private ActionBar actionBar;
    private FoodItem foodItem;
    private ImageView logoImg;
    private TextView descriptionTxt;
    private TextView nameTxt;
    private TextView costTxt;
    private TextView kindTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        getReferenceFromXml();
        getInformationFromIntent();
        Picasso.with(this).load(foodItem.getLogo()).fit().into(logoImg);
        nameTxt.setText(foodItem.getName());
        costTxt.setText(foodItem.getCost());
        kindTxt.setText(foodItem.getType());
        descriptionTxt.setText(foodItem.getDescription());
    }

    private void getReferenceFromXml(){
        toolbar = (Toolbar) findViewById(R.id.food_detail_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        logoImg = (ImageView) findViewById(R.id.food_detail_logo_img);
        descriptionTxt = (TextView) findViewById(R.id.decription_detail_txt);
        nameTxt = (TextView) findViewById(R.id.name_detail_txt);
        costTxt = (TextView) findViewById(R.id.cost_detail_txt);
        kindTxt = (TextView) findViewById(R.id.kind_detail_txt);

    }

    private void getInformationFromIntent(){
        Intent intent = getIntent();
        foodItem = new FoodItem();
        foodItem.setName(intent.getStringExtra(NAME_EXTRAS));
        foodItem.setType(intent.getStringExtra(KIND_EXTRAS));
        foodItem.setDescription(intent.getStringExtra(DES_EXTRAS));
        foodItem.setLogo(intent.getStringExtra(LOGO_EXTRAS));
        foodItem.setCost(intent.getStringExtra(COST_EXTRAS));
        foodItem.setAmount(intent.getStringExtra(AMOUNT_EXTRAS));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
