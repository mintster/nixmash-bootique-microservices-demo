package com.nixmash.jangles.service;

import com.google.inject.ImplementedBy;

@ImplementedBy(JanglesApiServiceImpl.class)
public interface JanglesApiService {

    String showConfiguration();
    String sayHello();
}
