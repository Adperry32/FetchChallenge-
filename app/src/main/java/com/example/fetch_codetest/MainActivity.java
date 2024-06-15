package com.example.fetch_codetest;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fetch_codetest.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //adding function to and animations to button

        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulsate);
        binding.fetchDataButton.startAnimation(pulseAnimation);


        //listen for click to initiate transition to fragment
        binding.fetchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Begin the fragment transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace the existing  container (R.id.mainActivity) with the fragment
                transaction.replace(R.id.main_Activity, new ViewDataFragment());

                // Add the transaction to the back stack (optional, for back navigation)
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
    }

}