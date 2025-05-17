package com.example.demo.svc;

import com.example.demo.repo.RedirectCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedirectCounterService {

    @Autowired
    private RedirectCountRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementRedirectCountAsync(Long id) {
        repository.incrementCount(id);
    }
}