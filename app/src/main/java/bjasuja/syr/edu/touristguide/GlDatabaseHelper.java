package bjasuja.syr.edu.touristguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GlDatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_Name="touristguideglsignin.db";
    public static final String Table_Name="touristguideglsignin_table";
    public static final String COL_1="ID";
    public static final String COL_2="logout";
    public static final String COL_3="googleid";
    public static final String COL_4="googlename";
    public static final String COL_5="googlepic";
    public static final String COL_6="googleemail";

    SQLiteDatabase db;
    public GlDatabaseHelper(Context context) {
        super(context, Database_Name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table touristguideglsignin_table(ID INTEGER PRIMARY KEY AUTOINCREMENT,logout Text,googleid TEXT,googlename TEXT, googlepic TEXT, googleemail TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }

    public boolean insertdata(String logout,String googleid,String googlename,String googlepic, String googleemail)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues b1=new ContentValues();
        b1.put(COL_2,logout);
        b1.put(COL_3,googleid);
        b1.put(COL_4,googlename);
        b1.put(COL_5,googlepic);
        b1.put(COL_6,googleemail);
        long b2=db.insert(Table_Name, null, b1);
        if(b2==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getalldata()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor b1=db.rawQuery("Select * from "+Table_Name,null);
        return b1;
    }


    public boolean deletedata(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        long b2=db.delete(Table_Name, null,null);
        if(b2>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

