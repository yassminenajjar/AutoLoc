package tn.esprit.autoloc.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl extends AbstractCrudService<Vehicule> implements IVehiculeService {
    private final IVehiculeRepository vehiculeRepository;

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

    private void validate(Vehicule vehicule) {
        if (vehicule.getTarifJournalier() != null
                && vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas etre negatif");
        }
    }
}
