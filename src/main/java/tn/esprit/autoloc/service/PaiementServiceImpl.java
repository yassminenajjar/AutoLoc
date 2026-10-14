package tn.esprit.autoloc.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl extends AbstractCrudService<Paiement> implements IPaiementService {
    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement findById(Long id) {
        return findById(paiementRepository, id, "Paiement");
    }

    @Override
    public List<Paiement> findAll() {
        return findAll(paiementRepository);
    }
}
