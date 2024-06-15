package com.example.fetch_codetest.Utilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemViewModel extends ViewModel {

    private MutableLiveData<List<Items>> itemsLiveData;
    private MutableLiveData<String> errorLiveData;

    public ItemViewModel() {
        itemsLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        fetchData();
    }

    public LiveData<List<Items>> getItemsLiveData() {
        return itemsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    private void fetchData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Items>> call = apiService.getItems();

        call.enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Items> items = response.body();

                    // Filter out items where name is blank or null
                    items = items.stream()
                            .filter(item -> item.getName() != null && !item.getName().trim().isEmpty())
                            .collect(Collectors.toList());

                    // Sort by listId, then by name
                    Collections.sort(items, new Comparator<Items>() {
                        @Override
                        public int compare(Items o1, Items o2) {
                            int listIdComparison = Integer.compare(o1.getListId(), o2.getListId());
                            if (listIdComparison != 0) {
                                return listIdComparison;
                            }
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });

                    itemsLiveData.setValue(items);
                } else {
                    errorLiveData.setValue("Failed to retrieve data");
                }
            }
            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                errorLiveData.setValue("An error occurred: " + t.getMessage());
            }
        });
    }
}
