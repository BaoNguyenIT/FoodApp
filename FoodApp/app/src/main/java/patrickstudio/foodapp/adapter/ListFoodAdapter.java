package patrickstudio.foodapp.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import patrickstudio.foodapp.R;
import patrickstudio.foodapp.model.FoodItem;
import patrickstudio.foodapp.model.FoodType;

/**
 * Created by PATRICKLAGGER on 11/1/2017.
 */

public class ListFoodAdapter extends  RecyclerView.Adapter<ListFoodAdapter.ListFoodViewHolder>{
    final public ListFoodAdapter.ListItemClickListener mOnClickListener;
    private List<FoodItem> data;
    private Context mContext;
    private FirebaseUser user;
    private DatabaseReference database;
    private DatabaseReference mRef;
    class ListFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView logoImg;
        private TextView nameTxt;
        private TextView kindFoodTxt;
        private TextView costTxt;
        private ImageView cartBtn;
        public ListFoodViewHolder(View itemView) {
            super(itemView);
            logoImg = (ImageView) itemView.findViewById(R.id.food_list_logo_img);
            nameTxt = (TextView) itemView.findViewById(R.id.name_food_txt);
            kindFoodTxt = (TextView) itemView.findViewById(R.id.kind_food_txt);
            costTxt = (TextView) itemView.findViewById(R.id.cost_txt);
            cartBtn = (ImageView) itemView.findViewById(R.id.add_to_cart_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int postion = getAdapterPosition();
            mOnClickListener.onListItemClickListenter(postion);
        }
    }

    public ListFoodAdapter(List<FoodItem> list, ListFoodAdapter.ListItemClickListener listener, Context context) {
        this.mOnClickListener = listener;
        data = list;
        mContext = context;
        database = FirebaseDatabase.getInstance().getReference();
        mRef = database.child("user");
    }

    public interface ListItemClickListener {
        void onListItemClickListenter(int clickedItemIndex);
    }

    @Override
    public ListFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_view_holder, parent, false);
        ListFoodAdapter.ListFoodViewHolder viewHolder = new ListFoodAdapter.ListFoodViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListFoodViewHolder holder, final int position) {
        holder.nameTxt.setText(data.get(position).getName());
        holder.kindFoodTxt.setText(data.get(position).getType());
        holder.costTxt.setText(data.get(position).getCost());
        holder.kindFoodTxt.setText(data.get(position).getType());
        Picasso.with(mContext).load(data.get(position).getLogo()).fit().into(holder.logoImg);
        user = FirebaseAuth.getInstance().getCurrentUser();
        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getUid().equals(data.get(position).getUserId())){
                    Toast.makeText(mContext, "Bạn không thể đặt món ăn của chính bạn! vui lòng đăng nhập tài khoản khác", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", data.get(position).getName());
                    map.put("price", data.get(position).getCost());
                    map.put("logo", data.get(position).getLogo());
                    map.put("kind", data.get(position).getType());
                    map.put("userid", user.getUid());
                    mRef.child(data.get(position).getUserId()).push().setValue(map);
                    Toast.makeText(mContext, "Mua thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
