package solutions.masai.masai.android.add_host;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.communication.HostConnection;

/**
 * Created by Semko on 2016-12-02.
 */

class AddHostListAdapter extends ArrayAdapter<HostConnection> {

    AddHostListAdapter(Context context, List<HostConnection> hostConList) {
        super(context, R.layout.item_host_list, hostConList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_host_list, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.host_list_name);
            viewHolder.tvLogoName = (TextView) convertView.findViewById(R.id.host_list_name_logo_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(getItem(position).getHost().getHumanName());
        viewHolder.tvLogoName.setText(getItem(position).getHost().getHumanName().substring(0, 2));
        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvLogoName;
    }
}
