package ir.hesanfarzi.proxy.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ir.hesanfarzi.proxy.R;
import ir.hesanfarzi.proxy.utility.ProxyModel;

public class ProxyAdapter extends ListAdapter<ProxyModel, ProxyAdapter.ViewHolder> {

    private final ClickListener listener;

    public ProxyAdapter(DiffUtil.ItemCallback<ProxyModel> diffCallback, ClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_main, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProxyModel proxy = getItem(position);
        String ip = (position+1) + ": " + proxy.getIp();
        holder.text.setText(ip);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView text;
        public ViewHolder(View view, ClickListener listener) {
            super(view);
            card = view.findViewById(R.id.card);
            text = view.findViewById(R.id.text);
            card.setOnClickListener(v ->
                    listener.onClick(getItem(getLayoutPosition()).getIp()));
        }
    }


    public interface ClickListener {
        void onClick(String ip);
    }


    public static class ProxyDiff extends DiffUtil.ItemCallback<ProxyModel> {
        @Override
        public boolean areItemsTheSame(@NonNull ProxyModel oldItem, @NonNull ProxyModel newItem) {
            return oldItem == newItem;
        }
        @Override
        public boolean areContentsTheSame(@NonNull ProxyModel oldItem, @NonNull ProxyModel newItem) {
            return oldItem.getIp().equals(newItem.getIp());
        }
    }


}