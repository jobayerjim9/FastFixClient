package com.servicing.jobaer.fastfixclient.controller.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.servicing.jobaer.fastfixclient.view.fragment.AboutUsFragment;
import com.servicing.jobaer.fastfixclient.view.fragment.ContactFragment;
import com.servicing.jobaer.fastfixclient.view.fragment.HomeFragment;
import com.servicing.jobaer.fastfixclient.view.fragment.ServicesFragment;

public class BottomNavigationPageAdapter extends FragmentStatePagerAdapter {


    public BottomNavigationPageAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        }
        else if (position==1) {
            return new AboutUsFragment();
        }
        else if (position==2) {
            return new ContactFragment();
        }
        else if (position==3) {
            return new ServicesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
