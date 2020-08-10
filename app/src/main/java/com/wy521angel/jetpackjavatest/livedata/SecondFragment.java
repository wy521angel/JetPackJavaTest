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


public class SecondFragment extends Fragment {

    public static final String KEY_SECOND_FRAGMENT = "key_SecondFragment";
    private static final String TAG = "SecondFragment";

    private Observer<String> mObserver;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireActivity(), TAG + ":" + s, Toast.LENGTH_SHORT).show();
            }
        };

        LiveDataBus.getInstance().with(KEY_SECOND_FRAGMENT, String.class).observe(getViewLifecycleOwner(), mObserver);


        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.sendToFirstFragmentTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.getInstance().with(FirstFragment.KEY_FIRST_FRAGMENT, String.class).setValue(TAG + "发出消息");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveDataBus.getInstance().remove(KEY_SECOND_FRAGMENT);
    }
}