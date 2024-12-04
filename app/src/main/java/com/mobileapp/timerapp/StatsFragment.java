package com.mobileapp.timerapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobileapp.timerapp.databinding.FragmentStatsBinding;

public class StatsFragment extends Fragment {
    private FragmentStatsBinding binding;

    private final Bundle bundle = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextView focusText = binding.focus;
        TextView breakText = binding.breakTime;
        TextView percentText = binding.percent;

        TimerViewModel timerViewModel = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);

        timerViewModel.getFocusCount().observe(getViewLifecycleOwner(), focusCount -> {
            focusText.setText(focusCount + " seconds");
            bundle.putInt("focusCount", focusCount);
            getPercent();
        });

        timerViewModel.getBreakCount().observe(getViewLifecycleOwner(), breakCount -> {
            breakText.setText(breakCount + " seconds");
            bundle.putInt("breakCount", breakCount);
            getPercent();
        });
        return view;
    }

    private void getPercent() {
        int focusCount = bundle.getInt("focusCount", 0);
        int breakCount = bundle.getInt("breakCount", 0);
        TextView percentText = binding.percent;

        if (focusCount + breakCount != 0) {
            int percent = (int) ((double) focusCount / (focusCount + breakCount) * 100);
            percentText.setText(percent + "%");
        } else {
            percentText.setText("0%");
        }
    }
}


