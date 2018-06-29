package solutions.masai.masai.android.my_tickets;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.TimeHelper;
import solutions.masai.masai.android.core.model.Reservations;
import solutions.masai.masai.android.core.model.TravelSegment;
import solutions.masai.masai.android.flight_reservation.FlightReservationController;
import solutions.masai.masai.android.hotel_reservation.HotelReservationController;
import solutions.masai.masai.android.train_reservation.TrainReservationController;

import java.util.List;

/**
 * Created by Semko on 2016-12-02.
 */

class MyTicketsAdapter extends ArrayAdapter<TravelSegment> {

    MyTicketsAdapter(Context context, List<TravelSegment> hostList) {
        super(context, R.layout.item_hotel_reservation, hostList);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (getItem(position).getFlight() != null) {
            convertView = loadFlightReservationItem(position, convertView, parent);
        }
        if (getItem(position).getHotelReservation() != null) {
            convertView = loadHotelReservationItem(position, convertView, parent);
        }
        if (getItem(position).getPublicTransportation() != null) {
            convertView = loadTrainTicketItem(position, convertView, parent);
        }
        return convertView;
    }

    private boolean shouldShowDate(int position) {
        if (position > 0) {
            TravelSegment previousItem = getItem(position - 1);
            TravelSegment item = getItem(position);
            String previousDate = "";
            String thisDate = "";
            if (previousItem.getFlight() != null) {
                previousDate = TimeHelper.getFormattedDateYMDHMS(getContext(), previousItem.getFlight().getDeparture().getUtcDateTime());
            }
            if (previousItem.getHotelReservation() != null) {
                previousDate = TimeHelper.getFormattedDateYMD(getContext(), previousItem.getHotelReservation().getCheckIn());
            }
            if (item.getFlight() != null) {
                thisDate = TimeHelper.getFormattedDateYMDHMS(getContext(), item.getFlight().getDeparture().getUtcDateTime());
            }
            if (item.getHotelReservation() != null) {
                thisDate = TimeHelper.getFormattedDateYMD(getContext(), item.getHotelReservation().getCheckIn());
            }
            if (!thisDate.isEmpty() && thisDate.equals(previousDate)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private View loadTrainTicketItem(final int position, View convertView, @NonNull ViewGroup parent) {
        TrainTicketViewHolder trainTicketViewHolder;
        if (convertView == null || convertView.getTag() == null) {
            trainTicketViewHolder = new TrainTicketViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_train_ticket_reservation, parent, false);
            trainTicketViewHolder.tvTrainSourceDestination = (TextView) convertView.findViewById(R.id.item_train_source_destination);
            trainTicketViewHolder.tvReservationDate = (TextView) convertView.findViewById(R.id.item_train_reservation_date);
        } else {
            trainTicketViewHolder = (TrainTicketViewHolder) convertView.getTag();
        }
        final Reservations.PublicTransportation train = getItem(position).getPublicTransportation();
        String departureName = train.getDeparture().getStationName();
        String arrivalName = train.getArrival().getStationName();
        if (departureName == null || departureName.isEmpty()) {
            departureName = "";
        }
        if (arrivalName == null || arrivalName.isEmpty()) {
            arrivalName = "";
        }
        String sourceDestination = departureName.concat(" - ").concat(arrivalName);
        trainTicketViewHolder.tvTrainSourceDestination.setText(sourceDestination);
        if (shouldShowDate(position)) {
            String time = TimeHelper.getFormattedDateYMDHMS(getContext(), train.getArrival().getUtcDateTime());
            trainTicketViewHolder.tvReservationDate.setVisibility(View.VISIBLE);
            trainTicketViewHolder.tvReservationDate.setText(time);
        } else {
            trainTicketViewHolder.tvReservationDate.setVisibility(View.GONE);
        }
        convertView.setTag(R.string.int_tag, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag(R.string.int_tag);
                startTrainReservation(pos);
            }
        });
        return convertView;
    }

    private View loadFlightReservationItem(final int position, View convertView, @NonNull ViewGroup parent) {
        FlightViewHolder flightViewHolder;
        if (convertView == null || convertView.getTag() == null) {
            flightViewHolder = new FlightViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_flight_reservation, parent, false);
            flightViewHolder.tvFlightAirlineName = (TextView) convertView.findViewById(R.id.item_flight_reservation_airlines);
            flightViewHolder.tvFlightSourceDestination = (TextView) convertView.findViewById(R.id.item_flight_reservation_source_destination);
            flightViewHolder.tvReservationDate = (TextView) convertView.findViewById(R.id.item_flight_reservation_date);
        } else {
            flightViewHolder = (FlightViewHolder) convertView.getTag();
        }
        final Reservations.FlightReservation flight = getItem(position).getFlight();
        String departureName = flight.getDeparture().getName();
        String arrivalName = flight.getArrival().getName();
        if (departureName == null || departureName.isEmpty()) {
            departureName = "";
        }
        if (arrivalName == null || arrivalName.isEmpty()) {
            arrivalName = "";
        }
        String sourceDestination = departureName.concat(" - ").concat(arrivalName);
        flightViewHolder.tvFlightSourceDestination.setText(sourceDestination);
        if (shouldShowDate(position)) {
            String time = TimeHelper.getFormattedDateYMDHMS(getContext(), flight.getArrival().getUtcDateTime());
            flightViewHolder.tvReservationDate.setVisibility(View.VISIBLE);
            flightViewHolder.tvReservationDate.setText(time);
        } else {
            flightViewHolder.tvReservationDate.setVisibility(View.GONE);
        }
        convertView.setTag(R.string.int_tag, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag(R.string.int_tag);
                startFlightReservation(pos);
            }
        });
        return convertView;
    }

    private View loadHotelReservationItem(final int position, View convertView, @NonNull ViewGroup parent) {
        HotelViewHolder hotelViewHolder;
        if (convertView == null || convertView.getTag() == null) {
            hotelViewHolder = new HotelViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_hotel_reservation, parent, false);
            hotelViewHolder.tvHotelAddress = (TextView) convertView.findViewById(R.id.item_hotel_reservation_address);
            hotelViewHolder.tvHotelName = (TextView) convertView.findViewById(R.id.item_hotel_reservation_name);
            hotelViewHolder.tvHotelReservationDate = (TextView) convertView.findViewById(R.id.item_hotel_reservation_date);
        } else {
            hotelViewHolder = (HotelViewHolder) convertView.getTag();
        }
        hotelViewHolder.tvHotelAddress.setText(getItem(position).getHotelReservation().getAddress().getStreet());
        hotelViewHolder.tvHotelName.setText(getItem(position).getHotelReservation().getHotelName());
        if (shouldShowDate(position)) {
            String time = TimeHelper.getFormattedDateYMD(getContext(), getItem(position).getHotelReservation().getCheckIn());
            hotelViewHolder.tvHotelReservationDate.setVisibility(View.VISIBLE);
            hotelViewHolder.tvHotelReservationDate.setText(time);
        } else {
            hotelViewHolder.tvHotelReservationDate.setVisibility(View.GONE);
        }
        convertView.setTag(R.string.int_tag, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag(R.string.int_tag);
                startHotelReservation(pos);
            }
        });
        return convertView;
    }

    private void startTrainReservation(int pos) {
        Reservations.PublicTransportation train = getItem(pos).getPublicTransportation();
        if (train != null) {
            String trainReservationJson = new Gson().toJson(train);
            Intent intent = new Intent(getContext(), TrainReservationController.class);
            intent.putExtra(C.EXTRA_TRAIN, trainReservationJson);
            getContext().startActivity(intent);
        }
    }

    private void startFlightReservation(int pos) {
        Reservations.FlightReservation flightReservation = getItem(pos).getFlight();
        if (flightReservation != null) {
            String flightReservationJson = new Gson().toJson(flightReservation);
            Intent intent = new Intent(getContext(), FlightReservationController.class);
            intent.putExtra(C.EXTRA_FLIGHT, flightReservationJson);
            getContext().startActivity(intent);
        }
    }

    private void startHotelReservation(int pos) {
        Reservations.HotelReservation hotelReservation = getItem(pos).getHotelReservation();
        if (hotelReservation != null) {
            String hotelReservationJson = new Gson().toJson(hotelReservation);
            Intent intent = new Intent(getContext(), HotelReservationController.class);
            intent.putExtra(C.EXTRA_HOTEL_RESERVATION, hotelReservationJson);
            getContext().startActivity(intent);
        }
    }

    private static class FlightViewHolder {
        TextView tvFlightSourceDestination;
        TextView tvFlightAirlineName;
        TextView tvReservationDate;
    }

    private static class TrainTicketViewHolder {
        TextView tvReservationDate;
        TextView tvTrainSourceDestination;
    }

    private static class HotelViewHolder {
        TextView tvHotelName;
        TextView tvHotelAddress;
        TextView tvHotelReservationDate;
    }
}
