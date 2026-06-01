package isel.sisinf.jpa;

import isel.sisinf.jpa.repo.ClienteRepository;
import isel.sisinf.jpa.repo.PortefolioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaTransaction implements Transaction {

    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("sisinf");

    private final EntityManager em;
    private final ClienteRepository clienteRepo;
    private final PortefolioRepository portefolioRepo;

    public JpaTransaction() {
        this.em = EMF.createEntityManager();
        this.clienteRepo = new ClienteRepository.Jpa(em);
        this.portefolioRepo = new PortefolioRepository.Jpa(em);
    }

    @Override public void begin()   { em.getTransaction().begin();  }
    @Override public void commit()  { em.getTransaction().commit(); }

    @Override
    public void rollback() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @Override public ClienteRepository clienteRepository() { return clienteRepo; }
    @Override public PortefolioRepository portefolioRepository() { return portefolioRepo; }

    @Override public void close() { em.close(); }
    public static void shutdown() { EMF.close(); }

    public EntityManager getEntityManager() {
        return em;
    }
}