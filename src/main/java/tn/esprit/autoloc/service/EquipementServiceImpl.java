package tn.esprit.autoloc.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl extends AbstractCrudService<Equipement> implements IEquipementService {
    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement create(Equipement equipement) {
        ensureNew(equipement.getIdEquipement(), "equipement");
        validate(equipement);
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement findById(Long id) {
        return findById(equipementRepository, id, "Equipement");
    }

    @Override
    public List<Equipement> findAll() {
        return findAll(equipementRepository);
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        Equipement existing = findById(id);
        validate(equipement);
        existing.setLibelle(equipement.getLibelle());
        return equipementRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        ensureExists(equipementRepository, id, "Equipement");
        equipementRepository.deleteById(id);
    }

    private void validate(Equipement equipement) {
        if (equipement.getLibelle() == null || equipement.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libelle de l'equipement est obligatoire");
        }
    }
}
