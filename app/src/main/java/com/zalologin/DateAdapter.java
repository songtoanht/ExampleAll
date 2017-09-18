package com.zalologin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zalologin.model.Date;

import java.util.List;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/29/2017.
 */

public class DateAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Date> dates;
    private int size;

    public DateAdapter(Context context, List<Date> dates) {
        this.context = context;
        this.dates = dates;
        size = ScreenUtil.getWidthScreen(context) / 7;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).setDataItem(dates.get(position));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDay;
        private TextView tvWeek;
        private View vToday;

        ItemViewHolder(View itemView) {
            super(itemView);

            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvWeek = (TextView) itemView.findViewById(R.id.tvWeek);
            vToday = itemView.findViewById(R.id.vToday);
            itemView.getLayoutParams().width = size;
        }

        void setDataItem(Date date) {
            tvDay.setText(String.valueOf(date.getDay()));
            tvWeek.setText(String.valueOf(date.getWeek()));
        }
    }
}
