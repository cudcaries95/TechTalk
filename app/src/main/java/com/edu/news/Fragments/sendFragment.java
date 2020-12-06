package com.edu.news.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edu.news.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link sendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link sendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditText etUsername, etEmail, etGopY;
    public Button btnGuiGopY;
    public ProgressBar sendProgessBar;

    private OnFragmentInteractionListener mListener;

    public sendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static sendFragment newInstance(String param1, String param2) {
        sendFragment fragment = new sendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send2, container, false);
        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etGopY = view.findViewById(R.id.etGopY);
        btnGuiGopY = view.findViewById(R.id.btnGuiGopY);
        sendProgessBar = view.findViewById(R.id.progressBarGopY);
        sendProgessBar.setVisibility(View.INVISIBLE);

        btnGuiGopY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       String username = etUsername.getText().toString();
                       String email = etEmail.getText().toString();
                       String gopY = etGopY.getText().toString();

                        if ( username.isEmpty() || email.isEmpty() || gopY.isEmpty()){
                            Toast.makeText(getContext(), "Xin vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        }else {
                            btnGuiGopY.setVisibility(View.INVISIBLE);
                            sendProgessBar.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnGuiGopY.setVisibility(View.VISIBLE);
                                    sendProgessBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Đã gửi góp ý thành công!", Toast.LENGTH_SHORT).show();

                                    etUsername.setText("");
                                    etEmail.setText("");
                                    etGopY.setText("");
                                }
                            }, 3000);
                        }
                }
        });

        return view;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
}
