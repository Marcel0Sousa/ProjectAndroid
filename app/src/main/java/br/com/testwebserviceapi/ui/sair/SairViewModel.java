package br.com.testwebserviceapi.ui.sair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SairViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SairViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is exit fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}