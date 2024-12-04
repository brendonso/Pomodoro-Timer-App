package com.mobileapp.timerapp;

import android.os.CountDownTimer; //https://developer.android.com/reference/android/os/CountDownTimer#java need time up
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {
    public static final long mins = 60 * 1000;
    private final MutableLiveData<Long> time = new MutableLiveData<>();
    private final MutableLiveData<Integer> backgroundColor = new MutableLiveData<>();
    private final MutableLiveData<Integer> skipCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> focusCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> breakCount = new MutableLiveData<>(0);
    private final MutableLiveData<String> title = new MutableLiveData<>("Pomodoro");

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private boolean inBreakMode = false;
    private long timeLeft = 25 * mins;

    public void addTime(int num) {
        timeLeft += num * mins;
        time.setValue(timeLeft);
    }

    public void subTime(int num) {
        timeLeft -= num * mins;
        time.setValue(timeLeft);
    }

    public void updateTitle() {
        if (inBreakMode) {
            title.setValue("Break Time");
        } else {
            title.setValue("Focus");
        }
    }

    public void updateSkip() {
        Integer count = skipCount.getValue();
        if (count == null) {
            count = 0;
        }
        skipCount.setValue(count + 1);
    }

    public void updateFocus() {
        Integer count = focusCount.getValue();
        if (count == null) {
            count = 0;
        }
        focusCount.setValue(count + 1);
    }

    public void updateBreak() {
        Integer count = breakCount.getValue();
        if (count == null) {
            count = 0;
        }
        breakCount.setValue(count + 1);
    }

    public void resetCountdown() {
        if (inBreakMode) {
            timeLeft = 5 * mins;
        } else {
            timeLeft = 25 * mins;
        }

        time.setValue(timeLeft);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
    }

    public void setBreak() {
        inBreakMode = true;
        backgroundColor.setValue(R.color.blue);
        timeLeft = 5 * mins;
        time.setValue(timeLeft);

        if (isRunning) {
            countDownTimer.cancel();
        }
        startCountdown();
    }

    public void setFocus() {
        inBreakMode = false;
        backgroundColor.setValue(R.color.red);
        timeLeft = 25 * mins;
        time.setValue(timeLeft);
        if (isRunning) {
            countDownTimer.cancel();
        }
        startCountdown();
    }

    void startCountdown() {
        if (!isRunning) {
            isRunning = true;
            countDownTimer = new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                    time.setValue(timeLeft);
                    if(inBreakMode) {
                        updateBreak();

                    } else {
                        updateFocus();
                    }

                }
                @Override
                public void onFinish() {
                    isRunning = false;
                    time.setValue(0L);
                }
            };
            countDownTimer.start();
        }
    }

    void pauseCountdown() {
        if (isRunning && countDownTimer != null) {
            countDownTimer.cancel();
            isRunning = false;
        }
    }

    public TimerViewModel() {
        time.setValue(timeLeft);
    }

    public LiveData<Long> getTime() {
        return time;
    }

    public LiveData<Integer> getBackgroundColor() {
        return backgroundColor;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<Integer> getSkipCount() {
        return skipCount;
    }

    public LiveData<Integer> getFocusCount() {
        return focusCount;
    }

    public LiveData<Integer> getBreakCount() {
        return breakCount;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean getBreak() {
        return inBreakMode;
    }
}
