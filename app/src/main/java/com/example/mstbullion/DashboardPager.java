package com.example.mstbullion;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DashboardPager extends FragmentPagerAdapter {
    public DashboardPager(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainActivity();
            case 1:
                return new PamperPackActivity();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}
