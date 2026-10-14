package tn.esprit.autoloc.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends AbstractCrudService<Reservation> implements IReservationService {
    private final IReservationRepository reservationRepository;

    @Override
    public Reservation create(Reservation reservation) {
        ensureNew(reservation.getIdReservation(), "reservation");
        validate(reservation);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return findById(reservationRepository, id, "Reservation");
    }

    @Override
    public List<Reservation> findAll() {
        return findAll(reservationRepository);
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existing = findById(id);
        validate(reservation);
        existing.setDateDebut(reservation.getDateDebut());
        existing.setDateFin(reservation.getDateFin());
        existing.setStatut(reservation.getStatut());
        existing.setAgence(reservation.getAgence());
        existing.setClient(reservation.getClient());
        existing.setVehicule(reservation.getVehicule());
        return reservationRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        ensureExists(reservationRepository, id, "Reservation");
        reservationRepository.deleteById(id);
    }

    private void validate(Reservation reservation) {
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null
                && reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas preceder la date de debut");
        }
    }
}
