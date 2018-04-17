package solutions.masai.masai.android.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.model.Train;

class TrainAdapter extends ArrayAdapter<Train> {

    TrainAdapter(Context context, Train[] train) {
        super(context, R.layout.item_train_card, train);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_train_card, parent, false);
            viewHolder.tvTravelTime = (TextView) convertView.findViewById(R.id.train_card_travel_time);
            viewHolder.tvTravelAB = (TextView) convertView.findViewById(R.id.train_card_place_a_to_b);
            viewHolder.tvPriceHigh = (TextView) convertView.findViewById(R.id.train_card_price_high);
            viewHolder.tvPriceLow = (TextView) convertView.findViewById(R.id.train_card_price_low);
            viewHolder.btnBook = (TextView) convertView.findViewById(R.id.train_card_book);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String travelTime = String.format("%s - %s", getItem(position).getDepartureTime(), getItem(position).getArrivalTime());
        String travelAB = String.format("%s - %s", getItem(position).getDepartureLocation(), getItem(position).getArrivalLocation());
        String priceHigh = String.format(getContext().getString(R.string.price_high), Float.toString(getItem(position).getStdPrice()));
        String priceLow = String.format(getContext().getString(R.string.price_low), Float.toString(getItem(position).getLowPrice()));
        viewHolder.tvTravelTime.setText(travelTime);
        viewHolder.tvTravelAB.setText(travelAB);
        viewHolder.tvPriceHigh.setText(priceHigh);
        viewHolder.tvPriceLow.setText(priceLow);
        viewHolder.btnBook.setTag(position);
        viewHolder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if (pos >= 0 && pos < getCount()) {
                    startBrower(getItem(pos).getBookingLink());
                }
            }
        });
        return convertView;
    }

    private void startBrower(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        getContext().startActivity(i);
    }

    private static class ViewHolder {
        TextView tvTravelTime;
        TextView tvTravelAB;
        TextView tvPriceHigh;
        TextView tvPriceLow;
        TextView btnBook;
    }
}
