package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    SQLiteDatabase database;

    public PersistentAccountDAO(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();
        String queryString = "SELECT ACCOUNT_NO FROM ACCOUNT_TABLE";
        Cursor cursor = database.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do {
                String accountNumber = cursor.getString(cursor.getColumnIndex("ACCOUNT_NO"));
                accountNumbersList.add(accountNumber);
            } while(cursor.moveToNext());
        }
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList = new ArrayList<>();
        String queryString = "SELECT * FROM ACCOUNT_TABLE";
        Cursor cursor = database.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do {
                String accountNumber = cursor.getString(cursor.getColumnIndex("ACCOUNT_NO"));
                String bankName = cursor.getString(cursor.getColumnIndex("BANK_NAME"));
                String accountHolderName = cursor.getString(cursor.getColumnIndex("ACCOUNT_HOLDER_NAME"));
                Double balance = cursor.getDouble(cursor.getColumnIndex("BALANCE"));
                Account account = new Account(accountNumber, bankName, accountHolderName, balance);
                accountsList.add(account);
            } while(cursor.moveToNext());
        }
        return accountsList;
    }


    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String queryString = "SELECT * FROM ACCOUNT_TABLE WHERE ACCOUNT_NO = " + accountNo;
        Cursor cursor = database.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            String accountNumber = cursor.getString(cursor.getColumnIndex("ACCOUNT_NO"));
            String bankName = cursor.getString(cursor.getColumnIndex("BANK_NAME"));
            String accountHolderName = cursor.getString(cursor.getColumnIndex("ACCOUNT_HOLDER_NAME"));
            Double balance = cursor.getDouble(cursor.getColumnIndex("BALANCE"));
            Account account = new Account(accountNumber, bankName, accountHolderName, balance);
            return account;
        }while(cursor.moveToNext());
        return null;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("ACCOUNT_NO", account.getAccountNo());
        contentValues.put("BANK_NAME", account.getBankName());
        contentValues.put("ACCOUNT_HOLDER_NAME", account.getAccountHolderName());
        contentValues.put("BALANCE", account.getBalance());

        database.insert("ACCOUNT_TABLE", null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql1 = "DELETE FROM TRANSACTION_TABLE WHERE ACCOUNT_NO = " + accountNo;
        String sql2 = "DELETE FROM ACCOUNT_TABLE WHERE ACCOUNT_NO = " + accountNo;
        SQLiteStatement statement1 = database.compileStatement(sql1);
        SQLiteStatement statement2 = database.compileStatement(sql2);

        statement1.executeUpdateDelete();
        statement2.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(expenseType == ExpenseType.EXPENSE){
            String queryString1 = "UPDATE ACCOUNT_TABLE SET BALANCE = BALANCE - " + amount + " WHERE ACCOUNT_NO = " + accountNo;
            SQLiteStatement statement = database.compileStatement(queryString1);
            statement.executeUpdateDelete();
        }else{
            String queryString2 = "UPDATE ACCOUNT_TABLE SET BALANCE = BALANCE + " + amount + " WHERE ACCOUNT_NO = " + accountNo;
            SQLiteStatement statement = database.compileStatement(queryString2);
            statement.executeUpdateDelete();
        }
    }
}
