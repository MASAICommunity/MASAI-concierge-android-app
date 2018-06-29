package solutions.masai.masai.android.my_tickets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.activity_reservation.ActivityReservationController;
import solutions.masai.masai.android.car_reservation.CarReservationController;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.adapter.SimpleSectionedRecyclerViewAdapter;
import solutions.masai.masai.android.core.model.Reservations;
import solutions.masai.masai.android.core.model.TravelSegment;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.journey.Journey;
import solutions.masai.masai.android.core.model.journey.Segment;
import solutions.masai.masai.android.core.model.journey.Segments;
import solutions.masai.masai.android.core.model.journey.segment_types.ActivitySegment;
import solutions.masai.masai.android.core.model.journey.segment_types.AirSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.CarSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.HotelSegment;
import solutions.masai.masai.android.core.model.journey.segment_types.RailSegment;
import solutions.masai.masai.android.flight_reservation.FlightReservationController;
import solutions.masai.masai.android.hotel_reservation.HotelReservationController;
import solutions.masai.masai.android.train_reservation.TrainReservationController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTicketsController extends AppCompatActivity implements MyTicketsView.MyTicketsViewListener {
    private MyTicketsView view;
    private Context context;
    private Handler handler;

    public SegmentAdapter segmentAdapter;
    public Segments segments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_my_tickets);

        view = new MyTicketsView(findViewById(android.R.id.content), this, this);

        segments = new Segments();
        segmentAdapter = new SegmentAdapter(this, segments);

        segmentAdapter.setItemsOnClickListener(new SegmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(Segment segment) {
                if (segment instanceof AirSegment) {
                    String json = new Gson().toJson(segment);
                    Intent intent = new Intent(getContext(), FlightReservationController.class);
                    intent.putExtra(C.EXTRA_FLIGHT, json);
                    getContext().startActivity(intent);
                } else if (segment instanceof CarSegment) {
                    String json = new Gson().toJson(segment);
                    Intent intent = new Intent(getContext(), CarReservationController.class);
                    intent.putExtra(C.EXTRA_CAR, json);
                    getContext().startActivity(intent);
                } else if (segment instanceof RailSegment) {
                    String json = new Gson().toJson(segment);
                    Intent intent = new Intent(getContext(), TrainReservationController.class);
                    intent.putExtra(C.EXTRA_TRAIN, json);
                    getContext().startActivity(intent);

                } else if (segment instanceof HotelSegment) {
                    String json = new Gson().toJson(segment);
                    Intent intent = new Intent(getContext(), HotelReservationController.class);
                    intent.putExtra(C.EXTRA_HOTEL_RESERVATION, json);
                    getContext().startActivity(intent);
                } else if (segment instanceof ActivitySegment) {
                    String json = new Gson().toJson(segment);
                    Intent intent = new Intent(getContext(), ActivityReservationController.class);
                    intent.putExtra(C.EXTRA_ACTIVITY, json);
                    getContext().startActivity(intent);
                }
            }
        });

        final User user = ConnectionManager.getInstance().getUser();
        BackendManager.getInstance().getBackendService().getJourneys(C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken(), null, null)
                .enqueue(new Callback<List<Journey>>() {
                    @Override
                    public void onResponse(Call<List<Journey>> call, Response<List<Journey>> response) {
                        if (response.errorBody() == null) {
                            setUpSectionedTreeList(response.body());
                        }else {
                            view.showNoJounrey(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Journey>> call, Throwable t) {
                        view.showNoJounrey(true);
                    }
                });
    }

    private List<TravelSegment> getHotelReservations(Reservations reservations) {
        List<TravelSegment> hotelReservations = new ArrayList<>();
        for (Reservations.TravelItem item : reservations.getItems()) {
            if (item.getSegments() != null) {
                for (TravelSegment travelSegment : item.getSegments()) {
                    if (travelSegment.getHotelReservation() != null || travelSegment.getFlight() != null || travelSegment.getPublicTransportation() != null) {
                        hotelReservations.add(travelSegment);
                    }
                }
            }
            if (item.getPublicTransportation() != null) {
                TravelSegment segment = new TravelSegment();
                segment.setPublicTransportation(item.getPublicTransportation());
                hotelReservations.add(segment);
            }
            if (item.getFlight() != null) {
                TravelSegment segment = new TravelSegment();
                segment.setFlight(item.getFlight());
                hotelReservations.add(segment);
            }
            if (item.getHotelReservation() != null) {
                TravelSegment segment = new TravelSegment();
                segment.setHotelReservation(item.getHotelReservation());
                hotelReservations.add(segment);
            }
        }
        return hotelReservations;
    }

    public void setUpSectionedTreeList(List<Journey> journeyList) {

        if(journeyList.size() == 0){
            view.showNoJounrey(true);
        }else view.showNoJounrey(false);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<>();

        int index = 0;
        segments = new Segments();
        segmentAdapter.clearSteps();

        for (Journey journey : journeyList) {
            segments.addAll(journey.getSegments());
            segmentAdapter.addStep(segments.size());
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(index, journey.getSegments().getSectionHeader()));
            index += journey.getSegments().size();
        }

        segmentAdapter.update(segments);


        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(this.context, R.layout.section_journey, R.id.journey_section, segmentAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        view.setAdapter(mSectionedAdapter);
    }

    @Override
    protected void onStop() {
        view.hideProgress();
        super.onStop();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onBackArrowPressed() {
        onBackPressed();
    }

    @Override
    public void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.nav_tickets);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void search(String time, String query){
        final User user = ConnectionManager.getInstance().getUser();
        BackendManager.getInstance().getBackendService().getJourneys(C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken(), Integer.parseInt(time), query)
                .enqueue(new Callback<List<Journey>>() {
                    @Override
                    public void onResponse(Call<List<Journey>> call, Response<List<Journey>> response) {
                        if (response.errorBody() == null) {
                            setUpSectionedTreeList(response.body());
                        }else view.showNoJounrey(true);
                    }

                    @Override
                    public void onFailure(Call<List<Journey>> call, Throwable t) {
                        view.showNoJounrey(true);
                    }
                });
    }
}
