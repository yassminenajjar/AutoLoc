package tn.esprit.autoloc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl extends AbstractCrudService<Vehicule> implements IVehiculeService {
    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        ensureNew(vehicule.getIdVehicule(), "vehicule");
        validate(vehicule);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule findById(Long id) {
        return findById(vehiculeRepository, id, "Vehicule");
    }

    @Override
    public List<Vehicule> findAll() {
        return findAll(vehiculeRepository);
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existing = findById(id);
        validate(vehicule);
        existing.setImmatriculation(vehicule.getImmatriculation());
        existing.setMarque(vehicule.getMarque());
        existing.setModele(vehicule.getModele());
        existing.setCategorie(vehicule.getCategorie());
        existing.setTarifJournalier(vehicule.getTarifJournalier());
        existing.setStatut(vehicule.getStatut());
        existing.setAgence(vehicule.getAgence());
        existing.setEquipements(vehicule.getEquipements());
        return vehiculeRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        ensureExists(vehiculeRepository, id, "Vehicule");
        vehiculeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Vehicule assignAgence(Long vehiculeId, Long agenceId) {
        Vehicule vehicule = findById(vehiculeId);
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
        vehicule.setAgence(agence);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule assignEquipment(Long idVehicule, Long idEquipment) {
        Vehicule vehicule = findById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipment)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipment));
        if (vehicule.getEquipements() == null) {
            vehicule.setEquipements(new ArrayList<>());
        }
        if (vehicule.getEquipements().stream()
                .noneMatch(existing -> idEquipment.equals(existing.getIdEquipement()))) {
            vehicule.getEquipements().add(equipement);
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule unassignEquipment(Long idVehicule, Long idEquipment) {
        Vehicule vehicule = findById(idVehicule);
        equipementRepository.findById(idEquipment)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipment));
        if (vehicule.getEquipements() != null) {
            vehicule.getEquipements()
                    .removeIf(equipement -> idEquipment.equals(equipement.getIdEquipement()));
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule addAndAssignAgence(Vehicule vehicule, Long idAgence) {
        ensureNew(vehicule.getIdVehicule(), "vehicule");
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", idAgence));
        validate(vehicule);
        vehicule.setAgence(agence);

        if (vehicule.getEquipements() != null && !vehicule.getEquipements().isEmpty()) {
            List<Equipement> resolvedEquipements = new ArrayList<>();
            for (Equipement equipement : vehicule.getEquipements()) {
                if (equipement.getIdEquipement() != null) {
                    Equipement existing = equipementRepository.findById(equipement.getIdEquipement())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Equipement", equipement.getIdEquipement()));
                    resolvedEquipements.add(existing);
                } else {
                    resolvedEquipements.add(equipement);
                }
            }
            vehicule.setEquipements(resolvedEquipements);
        }
        return vehiculeRepository.save(vehicule);
    }

    private void validate(Vehicule vehicule) {
        if (vehicule.getTarifJournalier() != null
                && vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas etre negatif");
        }
    }
}
