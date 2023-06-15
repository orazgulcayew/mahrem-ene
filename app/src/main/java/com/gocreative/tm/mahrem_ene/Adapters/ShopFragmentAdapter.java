package com.gocreative.tm.mahrem_ene.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gocreative.tm.mahrem_ene.Fragments.BabyClothesShopFragment;
import com.gocreative.tm.mahrem_ene.Fragments.ToyShopFragment;

import org.jetbrains.annotations.NotNull;

public class ShopFragmentAdapter extends FragmentStateAdapter {
    public ShopFragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new BabyClothesShopFragment();
        }
        return new ToyShopFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
