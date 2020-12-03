package Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserOrderListAdapter extends RecyclerView.Adapter<UserOrderListAdapter.UserOrderListViewHolder> {
    @NonNull
    @Override
    public UserOrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class UserOrderListViewHolder extends RecyclerView.ViewHolder{

        public UserOrderListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}


