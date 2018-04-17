package solutions.masai.masai.android.core.model.journey;

import solutions.masai.masai.android.core.helper.DateTimeHelper;
import solutions.masai.masai.android.core.model.journey.segment_types.ActivitySegment;
import solutions.masai.masai.android.core.model.journey.segment_types.AirSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.CarSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.HotelSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.RailSegment;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 01/09/2017
 */

public class Segments extends LinkedList<Segment>{

    public String getSectionHeader(){

        List<Object> list = new LinkedList<>();
        list.add((Object) this.getFirst());
        list.add((Object) this.getLast());

        String header = "";
        boolean start = true;

        for (Object o : list) {
            if (o instanceof AirSegment) {
                header += DateTimeHelper.getDate(start ? ((AirSegment) o).getDepartureDatetime() : ((AirSegment) o).getArrivalDatetime());
            } else if (o instanceof CarSegment) {
                header += DateTimeHelper.getDate(start ? ((CarSegment) o).getPickupDatetime() : ((CarSegment) o).getDropoffDatetime());
            } else if (o instanceof RailSegment) {
                header += DateTimeHelper.getDate(start ? ((RailSegment) o).getDepartureDatetime() : ((RailSegment) o).getArrivalDatetime());
            } else if (o instanceof HotelSegment) {
                header += DateTimeHelper.getDate(start ? ((HotelSegment) o).getCheckinDate() : ((HotelSegment) o).getCheckoutDate());
            } else if (o instanceof ActivitySegment) {
                header += DateTimeHelper.getDate(start ? ((ActivitySegment) o).getStartDatetime() : ((ActivitySegment) o).getEndDatetime());
            }
            if(start)
                header += " - ";
            start = false;
        }

        return header;
    }

}
