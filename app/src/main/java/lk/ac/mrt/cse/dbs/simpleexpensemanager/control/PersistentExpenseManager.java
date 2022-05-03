package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{

    private Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context = context;
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {

        SQLiteDatabase database = context.openOrCreateDatabase("190583V.db", context.MODE_PRIVATE, null);

        database.execSQL("CREATE TABLE IF NOT EXISTS ACCOUNT_TABLE (" +
                "ACCOUNT_NO TEXT PRIMARY KEY, " +
                "BANK_NAME TEXT, " +
                "ACCOUNT_HOLDER_NAME TEXT, " +
                "BALANCE REAL);");

        database.execSQL("CREATE TABLE IF NOT EXISTS TRANSACTION_TABLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ACCOUNT_NO TEXT ," +
                "EXPENSE_TYPE INT , " +
                "AMOUNT DOUBLE, " +
                "DATE LONG, " +
                "FOREIGN KEY(ACCOUNT_NO) REFERENCES ACCOUNT_TABLE(ACCOUNT_NO));");

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(database);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(database);
        setAccountsDAO(persistentAccountDAO);

    }
}
