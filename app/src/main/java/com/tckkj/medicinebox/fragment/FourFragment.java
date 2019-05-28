package com.tckkj.medicinebox.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseFragment;

public class FourFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        setContainer(R.layout.fragment_four);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i("1111111111111", "onSaveInstanceState: four");
    }
}