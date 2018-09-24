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

public class ListFoodHistoryAdapter extends  RecyclerView.Adapter<ListFoodHistoryAdapter.ListFoodViewHolder>{
    private List<FoodItem> data;
    private Context mContext;
    class ListFoodViewHolder extends RecyclerView.ViewHolder{
        private ImageView logoImg;
        private TextView nameTxt;
        private TextView kindFoodTxt;
        private TextView costTxt;
        public ListFoodViewHolder(View itemView) {
            super(itemView);
            logoImg = (ImageView) itemView.findViewById(R.id.food_list_logo_img);
            nameTxt = (TextView) itemView.findViewById(R.id.name_food_txt);
            kindFoodTxt = (TextView) itemView.findViewById(R.id.kind_food_txt);
            costTxt = (TextView) itemView.findViewById(R.id.cost_txt);
        }


    }

    public ListFoodHistoryAdapter(List<FoodItem> list, Context context) {
        data = list;
        mContext = context;
    }

    @Override
    public ListFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_pending_view_holder, parent, false);
        ListFoodHistoryAdapter.ListFoodViewHolder viewHolder = new ListFoodHistoryAdapter.ListFoodViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListFoodViewHolder holder, final int position) {
        holder.nameTxt.setText(data.get(position).getName());
        holder.kindFoodTxt.setText(data.get(position).getType());
        holder.costTxt.setText(data.get(position).getCost());
        holder.kindFoodTxt.setText(data.get(position).getType());
        Picasso.with(mContext).load(data.get(position).getLogo()).fit().into(holder.logoImg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
