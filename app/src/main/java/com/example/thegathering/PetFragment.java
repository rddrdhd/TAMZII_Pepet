package com.example.thegathering;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class PetFragment extends Fragment {

    ImageView pet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pet = (ImageView) getView().findViewById(R.id.imageViewPet);
        pet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "yes", Toast.LENGTH_LONG);
                            toast.show();
                }
                return true;
            }
        });
    }
}
