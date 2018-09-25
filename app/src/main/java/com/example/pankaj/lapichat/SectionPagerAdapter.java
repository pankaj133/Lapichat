package com.example.pankaj.lapichat;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                ReguestsFragment reguestsFragment= new ReguestsFragment();
                return reguestsFragment;
            case 1:
                ChatsFragmentt chatsFragmentt = new ChatsFragmentt();
                return chatsFragmentt;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();

                return friendsFragment;
                default:
                    return null;
        }


    }

    @Override
    public int getCount() {
        return 3;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "REQUEST";
            case 1:
                return "CHATS";
            case 2:
                return "FRINDES";
                default:
                    return null;

    }}
}
