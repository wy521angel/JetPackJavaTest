package com.wy521angel.jetpackjavatest.livedata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.wy521angel.jetpackjavatest.R;

public class FirstFragment extends Fragment {

    public static final String KEY_FIRST_FRAGMENT = "key_FirstFragment";
    private static final String TAG = "FirstFragment";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveDataBus.getInstance().with(KEY_FIRST_FRAGMENT, String.class).observe(
                getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(requireActivity(), TAG + ":" + s, Toast.LENGTH_SHORT).show();
                    }
                }

        );

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.sendToSecondFragmentTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.getInstance().with(SecondFragment.KEY_SECOND_FRAGMENT, String.class).setValue(TAG + "发出消息");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveDataBus.getInstance().remove(KEY_FIRST_FRAGMENT);
    }
}