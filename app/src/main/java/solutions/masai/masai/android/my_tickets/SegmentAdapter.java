package solutions.masai.masai.android.my_tickets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.DateTimeHelper;
import solutions.masai.masai.android.core.helper.adapter.SimpleAdapter;
import solutions.masai.masai.android.core.model.journey.Segment;
import solutions.masai.masai.android.core.model.journey.Segments;
import solutions.masai.masai.android.core.model.journey.segment_types.ActivitySegment;
import solutions.masai.masai.android.core.model.journey.segment_types.AirSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.CarSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.HotelSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.RailSegment;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 01/09/2017
 */

public class SegmentAdapter extends SimpleAdapter<Segment, SegmentViewHolder> {


    private Segments mDataSet = new Segments();

    private int mLastAnimatedItemPosition = -1;

    private List<Integer> steps = new LinkedList<>();

    private int position;
    private int stepCount = 0;
    private SegmentAdapter.OnItemClickListener mItemsOnClickListener;

    public SegmentAdapter(Context context, Collection<Segment> data) {
        super(context, data);
    }

    public void setItemsOnClickListener(SegmentAdapter.OnItemClickListener onClickListener){
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public SegmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType, Context ctx) {
        return new SegmentViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_segment,parent,false));
    }

    @Override
    public void onBindViewHolder(SegmentViewHolder holder, final Segment segment) {

        int total = getItemCount();

        position++;

        if(position == steps.get(stepCount)){
            holder.dot.setImageResource(R.drawable.line_end);
            stepCount++;
        }else if (position > 1) {
            holder.dot.setImageResource(R.drawable.line_mid);
        }

        if (stepCount > 0) {
            if(steps.get(stepCount-1)+1 == position)
                holder.dot.setImageResource(R.drawable.line_start);
        }




        switch (segment.getType()){
            case "Air":
                AirSegment air = (AirSegment) segment;
                holder.destination.setText(air.getOriginCityName() + " - " + air.getDestinationCityName());
                holder.date.setText(DateTimeHelper.getDate(air.getDepartureDatetime()));
                holder.time.setText(DateTimeHelper.getTime(air.getDepartureDatetime()));
                holder.imageView.setImageResource(R.drawable.ic_action_icon_flights);
                break;
            case "Car":
                CarSegment car = (CarSegment) segment;
                holder.destination.setText(car.getDropoffCityName());
                holder.date.setText(DateTimeHelper.getDate(car.getPickupDatetime()));
                holder.time.setText(DateTimeHelper.getTime(car.getDropoffDatetime()));
                holder.imageView.setImageResource(R.drawable.ic_action_icon_rental_car);
                break;
            case "Hotel":
                HotelSegment hotel = (HotelSegment) segment;
                holder.destination.setText(hotel.getCityName());
                holder.date.setText(DateTimeHelper.getDate(hotel.getCheckinDate()));
                holder.time.setText(DateTimeHelper.getTime(hotel.getCheckinDate()));
                holder.imageView.setImageResource(R.drawable.ic_action_icon_hotel);
                break;
            case "Rail":
                RailSegment rail = (RailSegment) segment;
                holder.destination.setText(rail.getOriginCityName() + " - " + rail.getDestinationCityName());
                holder.date.setText(DateTimeHelper.getDate(rail.getDepartureDatetime()));
                holder.time.setText(DateTimeHelper.getTime(rail.getDepartureDatetime()));
                holder.imageView.setImageResource(R.drawable.ic_action_icon_train);
                break;
            case "Activity":
                ActivitySegment activity = (ActivitySegment) segment;
                holder.destination.setText(activity.getActivityName());
                holder.date.setText(DateTimeHelper.getDate(activity.getStartDatetime()));
                holder.time.setText(DateTimeHelper.getTime(activity.getEndDatetime()));
                holder.imageView.setImageResource(R.drawable.icon_activities);
                break;
            default:
                break;
        }

        if(mItemsOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsOnClickListener.onClick(segment);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(Segment segment);
    }

    public void addStep(Integer i){
            steps.add(i);
    }

    public void clearSteps(){
        steps = new LinkedList<>();
        stepCount = 0;
        position = 0;
    }


}
