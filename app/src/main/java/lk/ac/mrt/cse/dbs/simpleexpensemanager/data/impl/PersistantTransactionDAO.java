package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

/**
 * Created by Chanaka on 11/20/2016.
 */
public class PersistantTransactionDAO implements TransactionDAO {

    DBConnector dbCon = MainActivity.connector;
    SQLiteDatabase dbase = dbCon.getWritableDatabase();

    List<Transaction> transList;

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount){
        Cursor res = dbase.rawQuery("SELECT account_no FROM accounts WHERE account_no LIKE '"+accountNo+"'",null);
        if(res.getCount()>0){
            ContentValues content = new ContentValues();
            content.put("date", date.toString());
            content.put("acc_no",accountNo);
            content.put("type",expenseType.toString());
            content.put("amount",amount);
            if((dbase.insert("transactions",null, content))==-1){
                System.out.println("ERROR");
            }
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        transList = new ArrayList<Transaction>();

        ExpenseType e = null;
        Cursor res = dbase.rawQuery("SELECT * FROM transactions",null);
        if(res.getCount()>0){
            while(res.moveToNext()){
                String date = res.getString(res.getColumnIndex("date"));
                String account_no = res.getString(res.getColumnIndex("account_no"));
                String type = res.getString(res.getColumnIndex("type"));
                String amount = res.getString(res.getColumnIndex("amount"));
                if(type.equalsIgnoreCase("EXPENSE")){
                    e = ExpenseType.EXPENSE;
                }else{
                    e = ExpenseType.INCOME;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyy");
                Date date2 = null;
                try {
                    date2 = sdf.parse(date);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                Transaction t = new Transaction(date2,account_no,e,Double.parseDouble(amount));
                transList.add(t);
            }

        }

        return transList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        getAllTransactionLogs();
        int size = transList.size();
        if (size <= limit) {
            return transList;
        }
        return transList.subList(size - limit, size);
    }
}
