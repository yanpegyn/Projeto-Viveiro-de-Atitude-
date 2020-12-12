package com.monteCCer.projetoatitude.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monteCCer.projetoatitude.R;

import org.jetbrains.annotations.NotNull;

public class FragmentoInfo extends Fragment {

    private int ct;

    public FragmentoInfo() {
        // Required empty public constructor
    }

    static FragmentoInfo newInstance(int ct) {
        Bundle args = new Bundle();
        args.putInt("ct", ct);
        FragmentoInfo f = new FragmentoInfo();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ct = 0;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            this.ct = getArguments().getInt("ct");
        }
        if (ct == 0)
            return inflater.inflate(R.layout.fragment_fragmento_info, container, false);
        else if (ct == 1)
            return inflater.inflate(R.layout.fragment_fragmento_info1, container, false);
        else
            return inflater.inflate(R.layout.fragment_fragmento_info2, container, false);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
