package patrickstudio.foodapp.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import patrickstudio.foodapp.R;

public class AddFoodActivity extends AppCompatActivity {
    private final static String KIND_EXTRAS ="kind";
    private static final int PICK_IMAGE = 1;
    private ImageView addButton;
    private ImageView logoImg;
    private EditText nameEdt;
    private EditText priceEdt;
    private EditText amountEdt;
    private EditText desEdt;
    private Button submitBtn;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageRef;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference ref1;
    private DatabaseReference ref2;
    private FirebaseUser firebaseUser;
    private String kindIndex;

    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getInformationFromIntent();
        getReferenceFromXml();
        init();
        StorageReference imagesRef = storageRef.child("images");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = ref1.push().getKey();
                uploadPicToFirebase(key);
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", nameEdt.getText().toString());
                map.put("price", priceEdt.getText().toString());
                map.put("amount", amountEdt.getText().toString());
                map.put("description", desEdt.getText().toString());
                map.put("userid", firebaseUser.getUid());
                ref1.child(key).setValue(map);
                finish();
            }
        });
    }
    private void getReferenceFromXml(){
        addButton = (ImageView) findViewById(R.id.add_image_button);
        logoImg = (ImageView) findViewById(R.id.logo_food_img);
        nameEdt = (EditText) findViewById(R.id.name_food_edt);
        priceEdt = (EditText) findViewById(R.id.price_food_edt);
        amountEdt = (EditText) findViewById(R.id.amount_food_edt);
        desEdt = (EditText) findViewById(R.id.des_food_edt);
        submitBtn = (Button) findViewById(R.id.summit_button);
    }

    private void init(){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = database.getReference();
        ref1 = ref.child("data").child(kindIndex);
        ref2 = ref.child("user").child(firebaseUser.getUid());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            imageRef = storageRef.child("images/"+uri.getLastPathSegment());;
            logoImg.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext()).load(uri).fit().into(logoImg);
            Log.i("AddFood", "co chay");
        }
    }

    private void uploadPicToFirebase(final String key){
        UploadTask uploadTask = imageRef.putFile(uri);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String urlImg = downloadUrl.toString();
                ref1.child(key).child("logo").setValue(urlImg);
            }
        });
    }

    private void getInformationFromIntent(){
        Intent intent = getIntent();
        kindIndex = intent.getStringExtra(KIND_EXTRAS);

    }
}
