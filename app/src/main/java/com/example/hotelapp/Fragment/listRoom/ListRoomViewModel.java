package com.example.hotelapp.Fragment.listRoom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListRoomViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListRoomViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }

}