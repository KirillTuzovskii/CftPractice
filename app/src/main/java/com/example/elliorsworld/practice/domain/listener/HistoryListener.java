package com.example.elliorsworld.practice.domain.listener;

import com.example.elliorsworld.practice.domain.model.ResultImage;

import java.util.ArrayList;

public interface HistoryListener {

    void onHistoryUpdate(ArrayList<ResultImage> resultImages);
}
