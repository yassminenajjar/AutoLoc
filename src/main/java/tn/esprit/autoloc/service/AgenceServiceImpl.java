package tn.esprit.autoloc.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl extends AbstractCrudService<Agence> implements IAgenceService {
    private final IAgenceRepository agenceRepository;

    @Override
    public Agence create(Agence agence) {
        ensureNew(agence.getIdAgence(), "agence");
        validate(agence);
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return findById(agenceRepository, id, "Agence");
    }

    @Override
    public List<Agence> findAll() {
        return findAll(agenceRepository);
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existing = findById(id);
        validate(agence);
        existing.setNom(agence.getNom());
        existing.setVille(agence.getVille());
        existing.setAdresse(agence.getAdresse());
        existing.setTelephone(agence.getTelephone());
        return agenceRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        ensureExists(agenceRepository, id, "Agence");
        agenceRepository.deleteById(id);
    }

    private void validate(Agence agence) {
        if (agence.getNom() == null || agence.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
    }
}
