package patrickstudio.foodapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import patrickstudio.foodapp.model.FoodType;
import patrickstudio.foodapp.R;

/**
 * Created by PATRICKLAGGER on 9/25/2017.
 */

public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.FoodTypeViewHolder>{
    final public ListItemClickListener mOnClickListener;
    private List<FoodType> data;
    private Context mContext;

    class FoodTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nameTxt;
        public ImageView logoImg;
        public FoodTypeViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.food_type_name_txt);
            logoImg = (ImageView) itemView.findViewById(R.id.food_type_logo_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int postion = getAdapterPosition();
            mOnClickListener.onListItemClickListenter(postion);
        }
    }

    public FoodTypeAdapter(List<FoodType> list, ListItemClickListener listener, Context context) {
        this.mOnClickListener = listener;
        data = list;
        mContext = context;
    }

    public interface ListItemClickListener {
        void onListItemClickListenter(int clickedItemIndex);
    }

    @Override
    public FoodTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_kind_view_holder, parent, false);
        FoodTypeViewHolder viewHolder = new FoodTypeViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodTypeViewHolder holder, int position) {
        holder.nameTxt.setText(data.get(position).getName());
        Picasso.with(mContext).load(data.get(position).getLogoImg()).fit().into(holder.logoImg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
