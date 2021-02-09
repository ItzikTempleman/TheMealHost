package com.example.therecipehost.Models;

public interface IResponse {
    void onSuccess(String success);
    void onLoading(boolean isLoading);
    void onError(String error);
}
