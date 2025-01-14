package com.exemple.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
// import lombok.RequiredArgsConstructor;   
import lombok.RequiredArgsConstructor;

@Document
@Data
@RequiredArgsConstructor
public class Garante {
    @Id
    private String id;
    @NonNull
    private String name;
    private double rate;
    private double minPrime;
    private int maxAge;

}