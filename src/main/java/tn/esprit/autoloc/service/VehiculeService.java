package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;
import java.util.Optional;

public interface VehiculeService {

    Vehicule add(Vehicule vehicule);

    Vehicule update(Vehicule vehicule);

    Optional<Vehicule> findById(Long idVehicule);

    List<Vehicule> findAll();

    void deleteById(Long idVehicule);
}
