package com.example.ecoguardians.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ecoguardians.Fragment.ResourceFragment;
import com.example.ecoguardians.R;

public class ResourcePagerAdapter extends FragmentStateAdapter {
private Context context;
    public ResourcePagerAdapter(@NonNull FragmentActivity fragmentActivity,Context context) {
        super(fragmentActivity);
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return ResourceFragment.newInstance(context.getString(R.string.recycling));
            case 1:
                return ResourceFragment.newInstance(context.getString(R.string.gardening));
            case 2:
                return ResourceFragment.newInstance(context.getString(R.string.energy));
            default:
                return ResourceFragment.newInstance(context.getString(R.string.recycling));
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
