package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeService {
    Vehicule create(Vehicule vehicule);
    Vehicule findById(Long id);
    List<Vehicule> findAll();
    Vehicule update(Long id, Vehicule vehicule);
    void deleteById(Long id);
    Vehicule assignAgence(Long vehiculeId, Long agenceId);
    Vehicule assignEquipment(Long idVehicule, Long idEquipment);
    Vehicule unassignEquipment(Long idVehicule, Long idEquipment);
    Vehicule addAndAssignAgence(Vehicule vehicule, Long idAgence);
}
