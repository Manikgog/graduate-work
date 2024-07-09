package ru.skypro.homework.mapper;

public interface Mapper<Input, Output> {
    Output perform(Input input);
}