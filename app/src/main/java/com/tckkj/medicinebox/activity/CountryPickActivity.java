package com.tckkj.medicinebox.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.util.country.Country;
import com.tckkj.medicinebox.util.country.LetterHolder;
import com.tckkj.medicinebox.util.country.PyAdapter;
import com.tckkj.medicinebox.util.country.PyEntity;
import com.tckkj.medicinebox.util.country.SideBar;
import com.tckkj.medicinebox.util.country.VH;

import java.util.ArrayList;
import java.util.List;

public class CountryPickActivity extends BaseActivity {

    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private ArrayList<Country> allCountries = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_pick);
        RecyclerView rvPick = (RecyclerView) findViewById(R.id.rv_pick);
        SideBar side = (SideBar) findViewById(R.id.side);
        EditText etSearch = (EditText) findViewById(R.id.et_search);
        TextView tvLetter = (TextView) findViewById(R.id.tv_letter);
        allCountries.clear();
        allCountries.addAll(Country.getAll(this, null));
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final CAdapter adapter = new CAdapter(selectedCountries);
        rvPick.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvPick.setLayoutManager(manager);
        rvPick.setAdapter(adapter);
        rvPick.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                selectedCountries.clear();
                for (Country country : allCountries) {
                    if(country.name.toLowerCase().contains(string.toLowerCase()))
                        selectedCountries.add(country);
                }
                adapter.update(selectedCountries);
            }
        });

        side.addIndex("#", side.indexes.size());
        side.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(letter);
                int  position = adapter.getLetterPosition(letter);
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }

            @Override
            public void onReset() {
                tvLetter.setVisibility(View.GONE);
            }
        });
        title.setText(R.string.select_countries_and_regions);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    class CAdapter extends PyAdapter<RecyclerView.ViewHolder> {

        public CAdapter(List<? extends PyEntity> entities) {
            super(entities);
        }

        @Override
        public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup parent, int viewType) {
            return new LetterHolder(getLayoutInflater().inflate(R.layout.item_letter, parent, false));
        }

        @Override
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.item_country_large_padding, parent, false));
        }

        @Override
        public void onBindHolder(RecyclerView.ViewHolder holder, PyEntity entity, int position) {
            VH vh = (VH)holder;
            final Country country = (Country)entity;
            vh.tvName.setText(country.name);
            vh.tvCode.setText("+" + country.code);
            holder.itemView.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra("country", country.toJson());
                setResult(Activity.RESULT_OK , data);
                finish();
            });
        }

        @Override
        public void onBindLetterHolder(RecyclerView.ViewHolder holder, LetterEntity entity, int position) {
            ((LetterHolder)holder).textView.setText(entity.letter.toUpperCase());
        }
    }
}
