package isel.sisinf;

import isel.sisinf.jpa.Dal;
import isel.sisinf.jpa.JpaTransaction;
import isel.sisinf.model.Portefolio;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AppTest {

    private static final String TEST_NIF = "501964843";

    @AfterClass
    public static void tearDown() {
        Dal.close();
    }

    @Test
    public void test1_CreateClientAndContact() {
        long uniqueId = System.currentTimeMillis();
        String cc = "CC" + (uniqueId % 1000000);
        String email = "test" + uniqueId + "@junit.pt";

        try {
            Dal.createClient(TEST_NIF, cc, "JUnit Client", "EMAIL", "Automated Test", email);
            assertTrue("Client and contact created without throwing exceptions.", true);
        } catch (Exception e) {
            fail("Failed to create client: " + getRootCause(e));
        }
    }

    @Test
    public void test2_CreatePortfolio() {
        String uniquePortfolioName = "Portfolio " + System.currentTimeMillis();

        try {
            Dal.createPortfolio(TEST_NIF, uniquePortfolioName);
            assertTrue("Portfolio created successfully.", true);
        } catch (Exception e) {
            fail("Failed to create portfolio: " + getRootCause(e));
        }
    }

    @Test
    public void test3_ListPositions() {
        try {
            List<Portefolio> portfolios = Dal.listPositions(TEST_NIF);

            assertNotNull("The portfolio list should not be null.", portfolios);

            System.out.println("Found " + portfolios.size() + " portfolios for NIF " + TEST_NIF);
        } catch (Exception e) {
            fail("Failed to list positions: " + getRootCause(e));
        }
    }

    @Test
    public void test4_UpdateDailyValues() {
        try {
            Dal.updateDailyValues();
            assertTrue("Stored Procedure executed successfully.", true);
        } catch (Exception e) {
            fail("Failed to execute p_actualizaValorDiario: " + getRootCause(e));
        }
    }

    @Test
    public void test5_UpdateClientName() {
        String newName = "JUnit Name " + System.currentTimeMillis();

        try {
            Dal.updateClientName(TEST_NIF, newName);
            assertTrue("Name updated successfully to: " + newName, true);
        } catch (Exception e) {
            fail("Failed to update client name: " + getRootCause(e));
        }
    }

    @Test
    public void test6_OptimisticLockingConflict() {
        // TX1: reads the client but doesn´t commit yet
        System.out.println("[TX1] Starting transaction 1...");
        JpaTransaction tx1 = new JpaTransaction();
        tx1.begin();
        isel.sisinf.model.Cliente clienteTx1 = tx1.clienteRepository().findById(TEST_NIF)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + TEST_NIF));
        System.out.println("[TX1] Read client: NIF=" + clienteTx1.getNif() + ", Name='" + clienteTx1.getNome() + "'");
        System.out.println("[TX1] Holding transaction open (not committed yet)...");

        // TX2: reads, modifies and commits BEFORE TX1
        System.out.println("\n[TX2] Starting transaction 2...");
        JpaTransaction tx2 = new JpaTransaction();
        tx2.begin();
        isel.sisinf.model.Cliente clienteTx2 = tx2.clienteRepository().findById(TEST_NIF)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + TEST_NIF));
        System.out.println("[TX2] Read client: NIF=" + clienteTx2.getNif() + ", Name='" + clienteTx2.getNome() + "'");

        String newNameTx2 = "Modified by TX2 - " + System.currentTimeMillis();
        clienteTx2.setNome(newNameTx2);
        System.out.println("[TX2] Changing name to: '" + newNameTx2 + "'");

        tx2.clienteRepository().save(clienteTx2);
        tx2.commit();
        System.out.println("[TX2] Committed successfully! Database is now updated.");
        tx2.close();
        System.out.println("[TX2] Transaction 2 closed.");

        // TX1: tries to commit with old snapshot → should fail
        System.out.println("\n[TX1] Attempting to commit with old snapshot...");
        try {
            String newNameTx1 = "Modified by TX1 - " + System.currentTimeMillis();
            clienteTx1.setNome(newNameTx1);
            System.out.println("[TX1] Changing name to: '" + newNameTx1 + "'");
            System.out.println("[TX1] Sending UPDATE with original column values (ALL_COLUMNS strategy)...");

            tx1.clienteRepository().save(clienteTx1);
            tx1.commit();

            fail("ERROR: Optimistic locking was NOT detected — TX1 should not have committed!");

        } catch (jakarta.persistence.OptimisticLockException | jakarta.persistence.RollbackException e) {
            System.out.println("[TX1] Commit FAILED as expected!");
            System.out.println("[TX1] Root cause: " + getRootCause(e));
            System.out.println("\n[ERROR] Concurrent modification conflict detected:");
            System.out.println("Client '" + TEST_NIF + "' was modified by another operation.");
            System.out.println("Please reload the record and try again.");
            assertTrue(true);
        } finally {
            tx1.rollback();
            tx1.close();
            System.out.println("[TX1] Transaction 1 rolled back and closed.");
        }
    }

    private String getRootCause(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}