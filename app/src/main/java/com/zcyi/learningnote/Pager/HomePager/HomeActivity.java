package com.zcyi.learningnote.Pager.HomePager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.zcyi.learningnote.Pager.HomePager.NotePager.NoteFragment;
import com.zcyi.learningnote.R;
import com.zcyi.learningnote.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    NoteFragment noteFragment;
    PicFragment picFragment;
    ProfileFragment profileFragment;
    Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMethod();
    }

    private void initMethod() {
        noteFragment = new NoteFragment();
        profileFragment = new ProfileFragment();
        picFragment = new PicFragment();
        changeFragment(null, noteFragment);
        current = noteFragment;
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_note:
                    changeFragment(current, noteFragment);
                    break;
                case R.id.menu_pic:
                    changeFragment(current, picFragment);
                    break;
                case R.id.menu_profile:
                    changeFragment(current, profileFragment);
                    break;
            }
            item.setChecked(true);
            return false;
        });
    }

    public void changeFragment(Fragment from, Fragment to) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!to.isAdded()) {
            if (from != null) {
                fragmentTransaction.hide(from);
            }
            fragmentTransaction.add(binding.navFragment.getId(), to).commit();
        } else {
            if (from != null) {
                fragmentTransaction.hide(from);
            }
            fragmentTransaction.show(to).commit();
        }
        current = to;
    }
}