package solutions.masai.masai.android.profile.controller.access_privileges;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import solutions.masai.masai.android.R;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccessPrivilegesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccessPrivilegesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccessPrivilegesFragment extends Fragment {

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
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//	private static final String ARG_PARAM1 = "param1";
//	private static final String ARG_PARAM2 = "param2";
	// TODO: Rename and change types of parameters
//	private String mParam1;
//	private String mParam2;
//	private OnFragmentInteractionListener mListener;

	public AccessPrivilegesFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment AccessPrivilegesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AccessPrivilegesFragment newInstance(String param1, String param2) {
		AccessPrivilegesFragment fragment = new AccessPrivilegesFragment();
//		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_access_privileges, container, false);

		TextViewRichDrawable textView = (TextViewRichDrawable) v.findViewById(R.id.fragment_access_privileges_open_textview);
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getActivity(), AccessPrivilegeSettingsController.class);
				startActivity(myIntent);
			}
		});

		return v;
	}

	@Override
	public void onDetach() {
		super.onDetach();
//		mListener = null;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
//		if (context instanceof OnFragmentInteractionListener) {
//			mListener = (OnFragmentInteractionListener) context;
//		} else {
//			throw new RuntimeException(context.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
	}
}
