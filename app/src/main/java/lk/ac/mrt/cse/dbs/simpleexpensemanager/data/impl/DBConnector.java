package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chanaka on 11/20/2016.
 */
public class DBConnector extends SQLiteOpenHelper{
    public static final String database = "140562K";

    public DBConnector(Context context) {
        super(context, database, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE accounts(account_no varchar(60) primary key not null,bank_name varchar(50) not null," +
                "holder_name varchar(50) not null,balance double precision not null)");
        db.execSQL("CREATE TABLE transactions(date varchar(50) not null,acc0unt_no varchar(60) not null,type varchar(50) not null," +
                "amount double precision not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
