package solutions.masai.masai.android.my_tickets;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import solutions.masai.masai.android.R;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 01/09/2017
 */

public class SegmentViewHolder extends RecyclerView.ViewHolder{

    public View itemView;

    ImageView imageView;
    ImageView dot;
    TextView date;
    TextView time;
    TextView destination;

    public SegmentViewHolder(View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.item_image);
        dot = (ImageView) itemView.findViewById(R.id.dot);
        date = (TextView) itemView.findViewById(R.id.item_date);
        time = (TextView) itemView.findViewById(R.id.item_time);
        destination = (TextView) itemView.findViewById(R.id.item_destination);

        this.itemView = itemView;
    }
}
