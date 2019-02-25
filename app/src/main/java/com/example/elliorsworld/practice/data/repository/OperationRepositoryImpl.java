package com.example.elliorsworld.practice.data.repository;

import com.example.elliorsworld.practice.domain.model.ResultImage;
import com.example.elliorsworld.practice.domain.listener.HistoryListener;
import com.example.elliorsworld.practice.domain.repository.OperationRepository;

import java.util.ArrayList;

public class OperationRepositoryImpl implements OperationRepository {

    private final ArrayList<ResultImage> resultImages = new ArrayList<>();
    private HistoryListener listener;

    @Override
    public void addResultImage(ResultImage image) {
        resultImages.add(image);
        updateHistory();
    }

    @Override
    public void removeImage(ResultImage image) {
        resultImages.remove(image);
        updateHistory();
    }

    @Override
    public void listenHistory(HistoryListener listener) {
        this.listener = listener;
    }

    private void updateHistory() {
        if (listener != null) {
            listener.onHistoryUpdate(resultImages);
        }
    }
}
