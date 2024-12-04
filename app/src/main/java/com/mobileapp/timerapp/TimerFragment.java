package com.mobileapp.timerapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.core.content.ContextCompat;

import com.mobileapp.timerapp.databinding.FragmentTimerBinding;

import java.util.Objects;

public class TimerFragment extends Fragment {
    private FragmentTimerBinding binding;
    private TimerViewModel timerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        timerViewModel = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);

        binding.startPauseButton.setOnClickListener(v -> {
            if (timerViewModel.isRunning()) {
                timerViewModel.pauseCountdown();
                binding.startPauseButton.setImageResource(R.drawable.start);
            } else {
                timerViewModel.startCountdown();
                binding.startPauseButton.setImageResource(R.drawable.pause);
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String level = binding.spinner.getSelectedItem().toString();
                switch (level) {
                    case "1 min":
                        timerViewModel.addTime(1);
                        break;
                    case "5 mins":
                        timerViewModel.addTime(5);
                        break;
                    case "10 mins":
                        timerViewModel.addTime(10);
                        break;
                }
                timerViewModel.pauseCountdown();
                binding.startPauseButton.setImageResource(R.drawable.start);
            }
        });

        binding.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String level = binding.spinner.getSelectedItem().toString();
                switch (level) {
                    case "1 min":
                        timerViewModel.subTime(1);
                        break;
                    case "5 mins":
                        timerViewModel.subTime(5);
                        break;
                    case "10 mins":
                        timerViewModel.subTime(10);
                        break;
                }
                timerViewModel.pauseCountdown();
                binding.startPauseButton.setImageResource(R.drawable.start);
            }
        });

        binding.focusBreakButton.setOnClickListener(v -> {
            timerViewModel.updateSkip();
            if (timerViewModel.getBreak()) {
                timerViewModel.setFocus();
            } else {
                timerViewModel.setBreak();
            }
            timerViewModel.pauseCountdown();
            timerViewModel.updateTitle();
            binding.startPauseButton.setImageResource(R.drawable.start);
        });

        binding.endResetButton.setOnClickListener(v -> {
            timerViewModel.resetCountdown();
            binding.startPauseButton.setImageResource(R.drawable.start);

        });

        if (timerViewModel.isRunning()) {
            binding.startPauseButton.setImageResource(R.drawable.pause);
            if(timerViewModel.getBreak() && timerViewModel.isRunning()) {
                timerViewModel.updateBreak();

            } else {
                timerViewModel.updateFocus();
            }
        } else {
            binding.startPauseButton.setImageResource(R.drawable.start);
        }

        timerViewModel.getTitle().observe(getViewLifecycleOwner(), text -> {
            binding.focusBreakLabel.setText(text);
        });

        timerViewModel.getTime().observe(getViewLifecycleOwner(), remainingTime -> {
            binding.chronometer.setText(String.format("%02d:%02d", (remainingTime / 1000 / 60), (remainingTime / 1000 % 60)));
        });

        timerViewModel.getBackgroundColor().observe(getViewLifecycleOwner(), color -> {
            binding.getRoot().setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), color));
        });

        return binding.getRoot();
    }
}
