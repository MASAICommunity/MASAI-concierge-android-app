package solutions.masai.masai.android.profile.controller.my_data;

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
import solutions.masai.masai.android.databinding.FragmentMyDataBinding;
import solutions.masai.masai.android.profile.view.my_data.MyDataView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyDataController.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDataController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDataController extends Fragment implements MyDataView.MyDataViewListener {

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {

		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
	// TODO: Rename parameter arguments, choose names that match

	private static final String ARG_USER_ID = "user_id";
	private static final String ARG_TOKEN = "token";

	private MyDataView myDataView;

	private FragmentMyDataBinding binding;
	private String userId;
	private String token;
	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types and number of parameters
	public static MyDataController newInstance(String userId) {
		MyDataController fragment = new MyDataController();
		Bundle args = new Bundle();
		args.putString(ARG_USER_ID, userId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			userId = getArguments().getString(ARG_USER_ID);
			token = getArguments().getString(ARG_TOKEN);
		}

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_data, container, false);

		myDataView = new MyDataView(binding.getRoot(), this.getContext(), this, binding);

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
	public void openPersonalData() {
		Intent myIntent = new Intent(getActivity(), PersonalInfoController.class);
		myIntent.putExtra(ARG_TOKEN, token);
		startActivity(myIntent);
	}

	@Override
	public void openContactInfo() {
		Intent myIntent = new Intent(getActivity(), ContactInfoController.class);
		//myIntent.putExtra("key", value); //Optional parameters
		startActivity(myIntent);
	}

	@Override
	public void openPrivateAddress() {
		Intent myIntent = new Intent(getActivity(), PrivateAddressController.class);
		startActivity(myIntent);
	}

	@Override
	public void openBillingAddress() {
		Intent myIntent = new Intent(getActivity(), BillingAddressController.class);
		startActivity(myIntent);
	}

	@Override
	public void onBackArrowPressed() {
		getActivity().onBackPressed();
	}
}
