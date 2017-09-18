package com.zalologin.databingding.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zalologin.BR;
import com.zalologin.R;
import com.zalologin.databingding.model.User;

import java.util.List;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/29/2017.
 */

public class UserAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserViewHolder) holder).getBinding().setVariable(BR.user, users.get(position));
        ((UserViewHolder) holder).getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        UserViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
