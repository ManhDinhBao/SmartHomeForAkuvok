package com.lefatechs.smarthome.View.Fragment.Engergy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lefatechs.smarthome.Controller.ChartAdapter;
import com.lefatechs.smarthome.Controller.SearchAdapter;
import com.lefatechs.smarthome.R;
import com.squareup.picasso.Picasso;

public class EnergyFragment extends Fragment {
    private ChartAdapter mChartAdapter;
    private SearchAdapter mSearchAdapter;
    private ViewPager mVpChart, mVpSearch;
    private TabLayout mTlChart, mTlSearch;
    private ImageView mIvBackGround;
    public final static String PREF_BACKGROUND = "background";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_engergy, container, false);

        mVpChart = view.findViewById(R.id.viewPager_fragmentEnergy_Chart);
        mTlChart = view.findViewById(R.id.tabLayout_fragmentEnergy_Chart);
        mVpSearch = view.findViewById(R.id.viewPager_fragmentEnergy_Info);
        mTlSearch = view.findViewById(R.id.tabLayout_fragmentEnergy_Info);
        mIvBackGround = view.findViewById(R.id.imageView_FragmentEnergy_Background);

        mChartAdapter = new ChartAdapter(getChildFragmentManager(), getActivity());
        mVpChart.setAdapter(mChartAdapter);
        mTlChart.setupWithViewPager(mVpChart);

        mSearchAdapter = new SearchAdapter(getChildFragmentManager(), getActivity());
        mVpSearch.setAdapter(mSearchAdapter);
        mTlSearch.setupWithViewPager(mVpSearch);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set background
        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String backgroundValue = sharedConfig.getString(PREF_BACKGROUND, "");
        switch (backgroundValue) {
            case "2":
                Picasso.get().load(R.drawable.home_background2).fit().into(mIvBackGround);
                break;
            case "3":
                Picasso.get().load(R.drawable.home_background3).fit().into(mIvBackGround);
                break;
            case "4":
                Picasso.get().load(R.drawable.home_background4).fit().into(mIvBackGround);
                break;
            default:
                Picasso.get().load(R.drawable.home_background).fit().into(mIvBackGround);
                break;
        }
    }
}
