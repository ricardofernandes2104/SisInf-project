package isel.sisinf.jpa.repo;

import isel.sisinf.model.Portefolio;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface PortefolioRepository extends CrudRepository<Portefolio, Long> {

    List<Portefolio> findByCliente(String nif);

    final class Jpa implements PortefolioRepository {

        private final EntityManager em;

        public Jpa(EntityManager em) {
            this.em = em;
        }

        @Override
        public Optional<Portefolio> findById(Long id) {
            return Optional.ofNullable(em.find(Portefolio.class, id));
        }

        @Override
        public List<Portefolio> findAll() {
            return em.createQuery("SELECT p FROM Portefolio p", Portefolio.class).getResultList();
        }

        @Override
        public List<Portefolio> findByCliente(String nif) {
            return em.createQuery("SELECT p FROM Portefolio p WHERE p.cliente.nif = :nif", Portefolio.class)
                    .setParameter("nif", nif)
                    .getResultList();
        }

        @Override
        public Portefolio save(Portefolio portefolio) {
            if (portefolio.getPortefolioId() == null) {
                em.persist(portefolio);
                return portefolio;
            }
            return em.merge(portefolio);
        }

        @Override
        public void deleteById(Long id) {
            Portefolio portefolio = findById(id).orElseThrow(
                    () -> new IllegalArgumentException("Portefólio com ID " + id + " não encontrado")
            );
            em.remove(portefolio);
        }
    }
}