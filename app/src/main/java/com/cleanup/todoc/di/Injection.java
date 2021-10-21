package com.cleanup.todoc.di;

import com.cleanup.todoc.repository.Repository;
import com.cleanup.todoc.service.FakeApiService;

public class Injection {
    public static Repository createRepository() {
        return new Repository(new FakeApiService());
    }
}
