package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

/**
 * Created by Chanaka on 11/20/2016.
 */
public class PersistantAccountDAO implements AccountDAO {

    DBConnector dbCon = MainActivity.dbCon;
    SQLiteDatabase dbase = dbCon.getWritableDatabase();

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accNoList = new ArrayList<String>();
        Cursor res = dbase.rawQuery("SELECT acc_no FROM accounts", null);
        if(res.getCount()>0){
            while(res.moveToNext()){
                String acc_no = res.getString(0);
                accNoList.add(acc_no);
            }
            res.close();
            return accNoList;
        }else{
            System.out.println("CURSOR ERROR!!!");
            return null;
        }

    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accList = new ArrayList<Account>();
        Cursor res = dbase.rawQuery("SELECT acc_no,bank_name,acc_holder_name,balance FROM accounts", null);
        if(res.getCount()>0){
            while(res.moveToNext()){
                String acc_no = res.getString(0);
                String bank_name = res.getString(1);
                String acc_holder_name = res.getString(2);
                String balance = res.getString(3);
                Account acc = new Account(acc_no,bank_name,acc_holder_name,Double.parseDouble(balance));
                accList.add(acc);
            }
            res.close();
        }
        return accList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor res = dbase.rawQuery("SELECT * FROM accounts WHERE acc_no LIKE '" + accountNo + "'", null);
        if (res != null) {
            String acc_no = res.getString(0);
            String bank_name = res.getString(1);
            String holder_name = res.getString(2);
            String balance = res.getString(3);
            Account c = new Account(acc_no, bank_name, holder_name, Double.parseDouble(balance));
            return c;
        }else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void addAccount(Account account) {
        Cursor res = dbase.rawQuery("SELECT * FROM accounts WHERE acc_no LIKE '"+account.getAccountNo()+"'",null);
        if(res.getCount()>0){

        }else{
            ContentValues cv = new ContentValues();
            cv.put("acc_no",account.getAccountNo());
            cv.put("bank_name",account.getBankName());
            cv.put("holder_name",account.getAccountHolderName());
            cv.put("balance",account.getBalance());
            if((dbase.insert("accounts",null,cv))==-1){
                System.out.println("ERROR");
            }
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbase.delete("accounts","acc_no = ?",new String[] {accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Cursor res;
        String val="";
        ContentValues values;
        res = dbase.rawQuery("SELECT acc_no FROM accounts WHERE acc_no LIKE '"+accountNo+"'",null);
        if(res.getCount()>0){
            Cursor getbal = dbase.rawQuery("SELECT balance FROM accounts WHERE acc_no LIKE '"+accountNo+"'",null);
            if(getbal.moveToFirst()){
                while(!getbal.isAfterLast()){
                    val = getbal.getString(getbal.getColumnIndex("balance"));
                    getbal.moveToNext();
                }
            }
            String newBal;
            switch (expenseType) {
                case EXPENSE:
                    newBal = Double.toString(Double.parseDouble(val)-amount);
                    values= new ContentValues();
                    values.put("balance",newBal);
                    dbase.update("accounts",values,"acc_no = ?",new String[] {accountNo});
                    break;
                case INCOME:
                    newBal = Double.toString(Double.parseDouble(val)+amount);
                    values = new ContentValues();
                    values.put("balance",newBal);
                    dbase.update("accounts",values,"acc_no = ?",new String[] {accountNo});
                    break;
            }
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }
}
