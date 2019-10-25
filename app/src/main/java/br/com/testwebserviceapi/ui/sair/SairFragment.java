package br.com.testwebserviceapi.ui.sair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import br.com.testwebserviceapi.R;

public class SairFragment extends Fragment {

    private SairViewModel sairViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sairViewModel =
                ViewModelProviders.of(this).get(SairViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sair, container, false);
        final TextView textView = root.findViewById(R.id.text_sair);
        sairViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}