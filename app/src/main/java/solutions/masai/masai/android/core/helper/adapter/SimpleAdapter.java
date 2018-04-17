package solutions.masai.masai.android.core.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 01/09/2017
 */

public abstract class SimpleAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final Context mContext;
    private List<T> mData;

    public SimpleAdapter(Context context, Collection<T> data) {
        mContext = context;
        if (data != null)
            mData = new LinkedList<>(data);
        else mData = new LinkedList<>();
    }

    protected Context getmContext() {
        return mContext;
    }

    public void add(T s, int position) {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        listUpdateCallback();
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mData.remove(position);
            listUpdateCallback();
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mData = new LinkedList<>();
        listUpdateCallback();
        notifyDataSetChanged();
    }

    public void update(List<T> data) {
        mData = data;
        listUpdateCallback();
        notifyDataSetChanged();
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType, Context ctx);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent, viewType, mContext);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        onBindViewHolder(holder, mData.get(position));
    }

    public abstract void onBindViewHolder(VH holder, T item);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    T getItemAt(int index) {
        return mData.get(index);
    }

    public void listUpdateCallback() {
    }
}
