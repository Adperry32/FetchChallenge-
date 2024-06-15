package com.example.fetch_codetest.Utilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemViewModel extends ViewModel {

    private final MutableLiveData<List<Items>> itemsLiveData;
    private final MutableLiveData<String> errorLiveData;

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
                    Collections.sort(items, (o1, o2) -> {

                        int listIdComparison = Integer.compare(o1.getListId(), o2.getListId());
                        if (listIdComparison != 0) {
                            return listIdComparison;
                        }

                        // If listId is the same, compare by name with natural sorting
                        return compareNames(o1.getName(), o2.getName());
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

    //need to add a helper class to sort the names and numbers
    private int compareNames(String name1, String name2) {
        // Regular expression to split the name into segments of text and numbers
        Pattern pattern = Pattern.compile("(\\D*)(\\d*)");
        Matcher matcher1 = pattern.matcher(name1);
        Matcher matcher2 = pattern.matcher(name2);

        while (matcher1.find() && matcher2.find()) {
            // Compare segments of text
            String text1 = matcher1.group(1);
            String text2 = matcher2.group(1);
            int textComparison = text1.compareTo(text2);
            if (textComparison != 0) {
                return textComparison;
            }

            // Compare segments of numbers (parse as integers)
            String num1 = matcher1.group(2);
            String num2 = matcher2.group(2);
            Integer num1Int = num1.isEmpty() ? 0 : Integer.parseInt(num1);
            Integer num2Int = num2.isEmpty() ? 0 : Integer.parseInt(num2);
            int numComparison = num1Int.compareTo(num2Int);
            if (numComparison != 0) {
                return numComparison;
            }
        }

        // If one name is a prefix of the other, shorter name should come first
        return Integer.compare(matcher1.group().length(), matcher2.group().length());
    }
}
