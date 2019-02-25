package com.example.elliorsworld.practice.domain.repository;

import com.example.elliorsworld.practice.domain.model.ResultImage;
import com.example.elliorsworld.practice.domain.listener.HistoryListener;

public interface OperationRepository {

    void addResultImage(ResultImage image);

    void removeImage(ResultImage image);

    void listenHistory(HistoryListener listener);

}
