package com.challenge.explorador_literario.services;

public interface IDataConverter {
    <T> T getData(String json, Class<T> clazz);
}
