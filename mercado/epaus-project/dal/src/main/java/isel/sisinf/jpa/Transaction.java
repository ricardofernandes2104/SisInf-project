package isel.sisinf.jpa;

import isel.sisinf.jpa.repo.ClienteRepository;
import isel.sisinf.jpa.repo.PortefolioRepository;

public interface Transaction extends AutoCloseable {
    void begin();
    void commit();
    void rollback();

    ClienteRepository clienteRepository();
    PortefolioRepository portefolioRepository();

    @Override
    void close();
}