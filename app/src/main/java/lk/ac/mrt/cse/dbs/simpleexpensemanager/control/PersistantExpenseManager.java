package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
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
        TransactionDAO databaseTransactionDAO = new DatabaseTransactionsDAO();
        setTransactionsDAO(databaseTransactionDAO);


        AccountDAO databaseAccountDAO = new DatabaseAccountDAO();
        setAccountsDAO(databaseAccountDAO);

        // Test Accounts
        Account acc1 = new Account("11111X", "Peoples Bank", "Jhon Walker", 50000.0);
        Account acc2 = new Account("22222Y", "Lanka Bank", "James Creamer", 10000.0);
        getAccountsDAO().addAccount(acct1);
        getAccountsDAO().addAccount(acct2);

    }
}
