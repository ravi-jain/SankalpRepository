package com.ravijain.sankalp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpContactUsFragment extends Fragment {


    public SpContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sp_contact_us, container, false);

        Button contactUs = (Button) v.findViewById(R.id.contactUsButton);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SpUtils.getEmailIntent();
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        return v;
    }

}
