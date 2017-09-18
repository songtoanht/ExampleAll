package com.zalologin.animator;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zalologin.R;

import java.util.List;

/**
 * //Anim adapter
 * <p>
 * Created by HOME on 8/21/2017.
 */

public class AnimAdapter extends RecyclerView.Adapter<AnimAdapter.ViewHolder> {
    public interface OnHandelItem {
        void addItem(int position);

        void delItem(int position);
    }

    private static Context mContext;
    private List<TextModel> mDataSelected;
    private List<TextModel> mDataNonSelect;
    private OnHandelItem mListener;

    public AnimAdapter(Context context, List<TextModel> dataSet, OnHandelItem listener) {
        mContext = context;
        mDataSelected = dataSet;
        mDataNonSelect = dataSet;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.anim_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext).load(R.drawable.chip).into(holder.image);
        holder.setData(mDataNonSelect.get(position), mListener, position);
    }

    @Override
    public int getItemCount() {
        return mDataSelected.size();
    }

    public void remove(int position) {
        Log.d("ttt", "remove: " + position);
        mDataSelected.remove(position);
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }

    public void add(TextModel text, int position) {
        Log.d("ttt", "add: " + position);
        TextModel textModel = text.copy();
        textModel.setSelected(true);
        mDataSelected.add(position, textModel);
        notifyItemInserted(position);
//        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView text;
        public TextView tvAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            tvAdd = (TextView) itemView.findViewById(R.id.tvAdd);
        }

        public void setData(TextModel s, final OnHandelItem handelItem, final int position) {
            text.setText(s.getName());
            tvAdd.setText(s.isSelected() ? "Del" : "Add");
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, s.isSelected() ? android.R.color.holo_red_light : android.R.color.holo_blue_light));

            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvAdd.getText().toString().trim().equals("Add")) {
                        handelItem.addItem(position);
                    } else {
                        handelItem.delItem(position);
                    }
                }
            });
        }
    }
}
