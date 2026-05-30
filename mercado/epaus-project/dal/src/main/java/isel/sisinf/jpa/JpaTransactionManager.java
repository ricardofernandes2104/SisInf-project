package isel.sisinf.jpa;

import java.util.function.Consumer;


/**
 * JPA-based implementation of TransactionManager.
 * Internally creates a new JPATransaction, begins it, invokes the block,
 * commits on success, rolls back on exception, and always closes.
 */
public final class JpaTransactionManager implements TransactionManager {

    @Override
    public void run(Consumer<Transaction> work) {
        JpaTransaction tx = new JpaTransaction();
        try {
            tx.begin();
            work.accept(tx);
            tx.commit();
        } catch (RuntimeException | Error ex) {
            tx.rollback();
            throw ex;
        } finally {
            tx.close();
        }
    }
}
