package tn.esprit.autoloc.service;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

abstract class AbstractCrudService<T> {

    protected T findById(JpaRepository<T, Long> repository, Long id, String resource) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(resource, id));
    }

    protected List<T> findAll(JpaRepository<T, Long> repository) {
        return repository.findAll();
    }

    protected void ensureNew(Long id, String resource) {
        if (id != null) {
            throw new IllegalArgumentException("Un nouveau " + resource.toLowerCase()
                    + " ne doit pas avoir d'identifiant");
        }
    }

    protected void ensureExists(JpaRepository<T, Long> repository, Long id, String resource) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(resource, id);
        }
    }
}
