package isel.sisinf.jpa;

import isel.sisinf.model.Cliente;
import isel.sisinf.model.ContactoCliente;
import isel.sisinf.model.Portefolio;
import isel.sisinf.model.Posicao;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Dal {
    private static final TransactionManager tm = new JpaTransactionManager();

    public static String version() { return "1.0"; }

    // 6a) Create a client and contact
    public static void createClient(String nif, String cc, String nome, String tipo, String descricao, String contacto) {
        tm.run(tx -> {
            Cliente cliente = new Cliente(nif, cc, nome);
            tx.clienteRepository().save(cliente);

            EntityManager em = ((JpaTransaction) tx).getEntityManager();
            ContactoCliente novoContacto = new ContactoCliente(cliente, tipo, descricao, contacto);
            em.persist(novoContacto);
        });
    }

    // 6b) Create a portfolio for a client
    public static void createPortfolio(String nif, String portfolioName) {
        tm.run(tx -> {
            Cliente cliente = tx.clienteRepository().findById(nif)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o NIF: " + nif));

            Portefolio portefolio = new Portefolio(cliente, portfolioName, BigDecimal.ZERO);
            tx.portefolioRepository().save(portefolio);
        });
    }

    // 6c) List positions of a client
    public static List<Portefolio> listPositions(String nif) {
        final List<Portefolio> result = new ArrayList<>();
        tm.run(tx -> {
            List<Portefolio> portefolios = tx.portefolioRepository().findByCliente(nif);
            for (Portefolio p : portefolios) {
                // Força a hidratação das coleções Lazy antes de fechar o EntityManager
                p.getPosicoes().size();
                for (Posicao pos : p.getPosicoes()) {
                    pos.getInstrumento().getDescricao();
                }
                result.add(p);
            }
        });
        return result;
    }

    // 6d) Update daily value of an instrument using the stored procedure
    public static void updateDailyValues() {
        tm.run(tx -> {
            EntityManager em = ((JpaTransaction) tx).getEntityManager();
            em.createNativeQuery("CALL p_actualizaValorDiario()").executeUpdate();
        });
    }

    // 6e) Update client data
    public static void updateClientName(String nif, String newName) {
        tm.run(tx -> {
            Cliente cliente = tx.clienteRepository().findById(nif)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o NIF: " + nif));

            cliente.setNome(newName);
            tx.clienteRepository().save(cliente);
        });
    }

    public static void close() {
        JpaTransaction.shutdown();
    }
}