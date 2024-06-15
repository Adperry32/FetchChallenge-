package com.example.fetch_codetest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fetch_codetest.Utilities.ItemViewModel;
import com.example.fetch_codetest.Utilities.Items;
import com.example.fetch_codetest.Utilities.RecycleViewAdapter;

import java.util.List;


public class ViewDataFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecycleViewAdapter rvAdapter;
    private ItemViewModel itemViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_data, container, false);

        //find recycle view in layout
        recyclerView = view.findViewById(R.id.items_RV);

        //setup recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAdapter = new RecycleViewAdapter();
        recyclerView.setAdapter(rvAdapter);

        //initializing view model
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        //observe livedata items
        itemViewModel.getItemsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                if (items != null && !items.isEmpty()) {
                    rvAdapter.setItems(items);
                }
            }
        });

        itemViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;

    }
}