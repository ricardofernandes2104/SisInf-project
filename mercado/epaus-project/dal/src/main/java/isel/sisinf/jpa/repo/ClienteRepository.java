package isel.sisinf.jpa.repo;

import isel.sisinf.model.Cliente;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends CrudRepository<Cliente, String> {

    final class Jpa implements ClienteRepository {

        private final EntityManager em;

        public Jpa(EntityManager em) {
            this.em = em;
        }

        @Override
        public Optional<Cliente> findById(String nif) {
            return Optional.ofNullable(em.find(Cliente.class, nif));
        }

        @Override
        public List<Cliente> findAll() {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        }

        @Override
        public Cliente save(Cliente cliente) {
            // Se o cliente não existir, faz persist. Caso contrário, faz merge.
            if (findById(cliente.getNif()).isEmpty()) {
                em.persist(cliente);
                return cliente;
            }
            return em.merge(cliente);
        }

        @Override
        public void deleteById(String nif) {
            Cliente cliente = findById(nif).orElseThrow(
                    () -> new IllegalArgumentException("Cliente com NIF " + nif + " não encontrado")
            );
            em.remove(cliente);
        }
    }
}