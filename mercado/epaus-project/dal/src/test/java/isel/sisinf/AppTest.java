package isel.sisinf;

import isel.sisinf.jpa.Dal;
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

    private String getRootCause(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}