package sda.oscail.edu.actiontabs;
/**
 * Created by ckirwan on 18/06/2016.
 * Updated by Christ Coughlan on 15/06/2020
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter
{
    private int mNumOfTabs;
    private static final int CHAT = 0;
    private static final int FIND = 1;
    private static final int MEET = 2;
    private static final int PARTY = 3;

    //NOTE THESE STRINGS SHOULLD BE RESOURCED BUT FOR PURPOSED OF THIS EXAMPLE THEY REMAIN HARDCODED.
    private static final String UI_TAB_CHAT = "CHAT";
    private static final String UI_TAB_FIND = "FIND";
    private static final String UI_TAB_MEET = "MEET";
    private static final String UI_TAB_PARTY = "PARTY";

    public MyPageAdapter(FragmentManager fm, int NumOfTabs)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        //each of this depending on the position will return the associated fragment
        switch (position)
        {
            case CHAT:
                return new ChatFragment();
            case FIND:
                return new FindFragment();
            case MEET:
                return new MeetFragment();
            case PARTY:
                return new PartyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return mNumOfTabs;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case CHAT:
                return UI_TAB_CHAT;
            case FIND:
                return UI_TAB_FIND;
            case MEET:
                return UI_TAB_MEET;
            case PARTY:
                return UI_TAB_PARTY;
            default:
                break;
        }
        return null;
    }



}