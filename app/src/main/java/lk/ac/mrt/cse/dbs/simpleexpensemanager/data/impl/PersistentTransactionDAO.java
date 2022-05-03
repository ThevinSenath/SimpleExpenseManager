package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase database;

    public PersistentTransactionDAO(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("ACCOUNT_NO", accountNo);
        contentValues.put("EXPENSE_TYPE", (expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        contentValues.put("AMOUNT", amount);
        contentValues.put("DATE", String.valueOf(date.getTime()));
        database.insert("TRANSACTION_TABLE", null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> allTransactionLogsList = new ArrayList<>();
        String queryString = "SELECT * FROM TRANSACTION_TABLE";
        Cursor cursor = database.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do {
                String accountNumber = cursor.getString(cursor.getColumnIndex("ACCOUNT_NO"));
                ExpenseType expenseType = (cursor.getInt(cursor.getColumnIndex("EXPENSE_TYPE")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME;
                Double amount = cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
                Date date = new Date(cursor.getLong(cursor.getColumnIndex("DATE")));
                Transaction transaction = new Transaction(date, accountNumber, expenseType, amount);
                allTransactionLogsList.add(transaction);
            } while(cursor.moveToNext());
        }
        return allTransactionLogsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> paginatedTransactionsList = new ArrayList<>();
        String queryString = "SELECT * FROM TRANSACTION_TABLE LIMIT " + limit;
        Cursor cursor = database.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do {
                String accountNumber = cursor.getString(cursor.getColumnIndex("ACCOUNT_NO"));
                ExpenseType expenseType = (cursor.getInt(cursor.getColumnIndex("EXPENSE_TYPE")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME;
                Double amount = cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
                Date date = new Date(cursor.getLong(cursor.getColumnIndex("DATE")));
                Transaction transaction = new Transaction(date, accountNumber, expenseType, amount);
                paginatedTransactionsList.add(transaction);
            } while(cursor.moveToNext());
        }
        return paginatedTransactionsList;
    }
}
