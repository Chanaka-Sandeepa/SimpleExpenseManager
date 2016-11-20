package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Chanaka on 11/20/2016.
 */
public class PersistantExpenseManager extends ExpenseManager{

    public PersistantExpenseManager() {
        setup();
    }



        @Override
        public void setup() {
        TransactionDAO databaseTransactionDAO = new PersistantTransactionDAO();
        setTransactionsDAO(databaseTransactionDAO);


        AccountDAO databaseAccountDAO = new PersistantAccountDAO();
        setAccountsDAO(databaseAccountDAO);

        // Test Accounts
        Account acc1 = new Account("11111X", "Peoples Bank", "Jhon Walker", 50000.0);
        Account acc2 = new Account("22222Y", "Lanka Bank", "James Creamer", 10000.0);
        getAccountsDAO().addAccount(acc1);
        getAccountsDAO().addAccount(acc2);

    }
}
