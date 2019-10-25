package br.com.testwebserviceapi.ui.atividades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AtividadesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AtividadesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is activity fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}