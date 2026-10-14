package tn.esprit.autoloc.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl extends AbstractCrudService<Employe> implements IEmployeService {
    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        ensureNew(employe.getIdEmploye(), "employe");
        validate(employe);
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return findById(employeRepository, id, "Employe");
    }

    @Override
    public List<Employe> findAll() {
        return findAll(employeRepository);
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existing = findById(id);
        validate(employe);
        existing.setNom(employe.getNom());
        existing.setPrenom(employe.getPrenom());
        existing.setRole(employe.getRole());
        existing.setAgence(employe.getAgence());
        return employeRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        ensureExists(employeRepository, id, "Employe");
        employeRepository.deleteById(id);
    }

    private void validate(Employe employe) {
        if (employe.getNom() == null || employe.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'employe est obligatoire");
        }
    }
}
