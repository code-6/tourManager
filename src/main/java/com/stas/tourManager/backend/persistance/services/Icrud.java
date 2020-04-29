package com.stas.tourManager.backend.persistance.services;

import com.stas.tourManager.backend.persistance.pojos.Participant;

import java.util.Optional;

public interface Icrud {
    void add(String firstName, String lastName);
    boolean delete(long id);
    Optional<Participant> get(long id);
    void update(long id, String firstName, String middleName, String lastName);
    boolean exist(long id);
}
