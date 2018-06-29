package solutions.masai.masai.android.profile.controller.journey_preferences;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.databinding.ActivityJourneyPreferencesBinding;

import solutions.masai.masai.android.profile.view.journey_preferences.JourneyPreferencesView;

/**
 * Created by tom on 12.09.17.
 */

public class JourneyPreferencesController extends Fragment implements JourneyPreferencesView.JourneyPreferencesViewListener {

	public interface OnFragmentInteractionListener {

		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

	JourneyPreferencesView journeyPreferencesView;
	ActivityJourneyPreferencesBinding binding;
	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types and number of parameters
	public static JourneyPreferencesController newInstance(String userId) {
		JourneyPreferencesController fragment = new JourneyPreferencesController();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

		}

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.activity_journey_preferences, container, false);

		journeyPreferencesView = new JourneyPreferencesView(binding.getRoot(), this.getContext(), this, binding);

		return binding.getRoot();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void openPassportSettings() {
		Intent myIntent = new Intent(getActivity(), PassportSettingsController.class);
		startActivity(myIntent);
	}

	@Override
	public void openEstaSettings() {
		Intent myIntent = new Intent(getActivity(), EstaSettingsController.class);
		startActivity(myIntent);
	}

	@Override
	public void openHotelSettings() {
		Intent myIntent = new Intent(getActivity(), HotelSettingsController.class);
		startActivity(myIntent);
	}

	@Override
	public void openFlightSettings() {
		Intent myIntent = new Intent(getActivity(), FlightSettingsController.class);
		startActivity(myIntent);
	}

	@Override
	public void openCarSettings() {
		Intent myIntent = new Intent(getActivity(), CarSettingsController.class);
		startActivity(myIntent);
	}

	@Override
	public void openTrainSettings() {
		Intent myIntent = new Intent(getActivity(), RailSettingsController.class);
		startActivity(myIntent);
	}
}
