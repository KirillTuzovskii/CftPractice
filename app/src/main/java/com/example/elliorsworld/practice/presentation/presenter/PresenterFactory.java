package com.example.elliorsworld.practice.presentation.presenter;

import android.content.Context;

import com.example.elliorsworld.practice.domain.repository.ImageRepository;
import com.example.elliorsworld.practice.data.repository.ImageRepositoryImpl;
import com.example.elliorsworld.practice.domain.repository.OperationRepository;
import com.example.elliorsworld.practice.data.repository.OperationRepositoryImpl;
import com.example.elliorsworld.practice.domain.interactor.InteractorImpl;
import com.example.elliorsworld.practice.presentation.MainContract;

public class PresenterFactory {

    public MainContract.Presenter createPresenter(MainContract.View view, Context context) {
        final OperationRepository operationRepository = new OperationRepositoryImpl();
        final ImageRepository loadFromUrl = new ImageRepositoryImpl(context);
        final InteractorImpl interactorImpl = new InteractorImpl(operationRepository, loadFromUrl);
        return new Presenter(view, interactorImpl);
    }
}
