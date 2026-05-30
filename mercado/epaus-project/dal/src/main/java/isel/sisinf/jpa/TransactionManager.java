package isel.sisinf.jpa;

import java.util.function.Consumer;

/**
 * TransactionManager simplifies transactional code by:
 *  - Opening a JPA-backed Transaction (Unit-of-work),
 *  - Automatically beginning the transaction,
 *  - Running the user's logic (which can call repositories on the provided Transaction),
 *  - Committing if everything succeeds, or rolling back on exception,
 *  - Finally closing the resources.
 */
public interface TransactionManager {
    /**
     * Run the given block of work inside a new JPA transaction.
     * The Transaction passed into the block already has its transaction begun.
     * If the block returns normally, the transaction is committed.
     * If the block throws, the transaction is rolled back.
     *
     * @param work a lambda that accepts a Transaction and does any repository calls needed
     */
    void run(Consumer<Transaction> work);
}

