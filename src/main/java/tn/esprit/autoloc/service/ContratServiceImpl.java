package tn.esprit.autoloc.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl extends AbstractCrudService<Contrat> implements IContratService {
    private final IContratRepository contratRepository;

    @Override
    public Contrat create(Contrat contrat) {
        ensureNew(contrat.getIdContrat(), "contrat");
        validate(contrat);
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findById(Long id) {
        return findById(contratRepository, id, "Contrat");
    }

    @Override
    public List<Contrat> findAll() {
        return findAll(contratRepository);
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        Contrat existing = findById(id);
        validate(contrat);
        existing.setDateSignature(contrat.getDateSignature());
        existing.setMontantTotal(contrat.getMontantTotal());
        existing.setValide(contrat.getValide());
        existing.setReservation(contrat.getReservation());
        return contratRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        ensureExists(contratRepository, id, "Contrat");
        contratRepository.deleteById(id);
    }

    private void validate(Contrat contrat) {
        if (contrat.getMontantTotal() != null
                && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas etre negatif");
        }
    }
}
