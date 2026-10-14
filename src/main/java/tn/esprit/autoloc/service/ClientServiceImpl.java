package tn.esprit.autoloc.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl extends AbstractCrudService<Client> implements IClientService {
    private final IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        ensureNew(client.getIdClient(), "client");
        validate(client);
        return clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return findById(clientRepository, id, "Client");
    }

    @Override
    public List<Client> findAll() {
        return findAll(clientRepository);
    }

    @Override
    public Client update(Long id, Client client) {
        Client existing = findById(id);
        validate(client);
        existing.setNom(client.getNom());
        existing.setPrenom(client.getPrenom());
        existing.setEmail(client.getEmail());
        existing.setTelephone(client.getTelephone());
        existing.setNumPermis(client.getNumPermis());
        existing.setDateInscription(client.getDateInscription());
        return clientRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        ensureExists(clientRepository, id, "Client");
        clientRepository.deleteById(id);
    }

    private void validate(Client client) {
        if (client.getNom() == null || client.getNom().isBlank()
                || client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalArgumentException("Le nom et l'e-mail du client sont obligatoires");
        }
    }
}
