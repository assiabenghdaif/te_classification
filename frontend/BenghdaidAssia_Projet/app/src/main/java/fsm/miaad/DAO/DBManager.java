package fsm.miaad.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import fsm.miaad.models.History;
import fsm.miaad.models.User;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;
    private SQLiteDatabase databaseReader;

    public DBManager(Context c) {
        context = c;
        dbHelper = new DatabaseHelper(context);
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        databaseReader = dbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertUser (User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FNAME, user.getFirstname());
        contentValues.put(DatabaseHelper.LNAME, user.getLastname());
        contentValues.put(DatabaseHelper.EMAIL, user.getEmail());
        contentValues.put(DatabaseHelper.PASSWORD, user.getPassword());
        database.insert("USERS", null, contentValues);
//        db.close();
        return true;
    }

    // get all users
    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.FNAME, DatabaseHelper.LNAME,DatabaseHelper.EMAIL,DatabaseHelper.PASSWORD };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getAllImage() {
        String[] columns = new String[] { DatabaseHelper._ID_IMAGE, DatabaseHelper.EMAIL_IMAGE,DatabaseHelper.IMAGE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_IMAGE, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean insertImage (String email,byte[] byteArray) {
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EMAIL_IMAGE, email);
        contentValues.put(DatabaseHelper.IMAGE, byteArray);
        database.insert("IMAGES", null, contentValues);
//        db.close();
        return true;
    }

    public User getUserById(String email) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "firstname,lastname"
        };

        // Define a selection and arguments for the WHERE clause
        String selection = "email = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the query
        Cursor cursor = databaseReader.query(
                "USERS",  // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // Don't group the rows
                null,               // Don't filter by row groups
                null                // The sort order
        );


        User userconnect=new User();
        if (cursor.moveToFirst()) {
            // Extract the image data from the cursor
            userconnect =new User(cursor.getString(0),cursor.getString(1));
        }

        cursor.close();
//        databaseReader.close();

        return userconnect;
    }

    public byte[] getImageByEmail(String email) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "image"
        };

        // Define a selection and arguments for the WHERE clause
        String selection = "email_image = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the query
        Cursor cursor = databaseReader.query(
                "IMAGES",  // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // Don't group the rows
                null,               // Don't filter by row groups
                null                // The sort order
        );


//        User userconnect=new User();
        byte[] image=null;
        if (cursor.moveToFirst()) {
            // Extract the image data from the cursor
            image =cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
        }

        cursor.close();
//        databaseReader.close();

        return image;
    }

    public boolean updateImage(String email, byte[] imageData) {
//        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("image", imageData);

        // Define a selection and arguments for the WHERE clause
        String selection = "email_image = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the update
        int count = database.update(
                "IMAGES",
                values,
                selection,
                selectionArgs
        );

//        db.close();

        return count > 0;
    }

    public boolean updatePersonnalInfo(String email, String f,String l,String loca) {
//        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("firstname", f);
        values.put("lastname", l);
        values.put("address", loca);

        // Define a selection and arguments for the WHERE clause
        String selection = "email = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the update
        int count = database.update(
                "USERS",
                values,
                selection,
                selectionArgs
        );

//        db.close();

        return count > 0;
    }

    public boolean updateContactInfo(String email, String p) {
//        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("phone", p);

        // Define a selection and arguments for the WHERE clause
        String selection = "email = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the update
        int count = database.update(
                "USERS",
                values,
                selection,
                selectionArgs
        );

//        db.close();

        return count > 0;
    }

    public boolean updatePassword(String email, String p) {
//        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", p);

        // Define a selection and arguments for the WHERE clause
        String selection = "email = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the update
        int count = database.update(
                "USERS",
                values,
                selection,
                selectionArgs
        );

//        db.close();

        return count > 0;
    }

    public User getUserByEmail(String email) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "firstname,lastname,address,phone"
        };

        // Define a selection and arguments for the WHERE clause
        String selection = "email = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the query
        Cursor cursor = databaseReader.query(
                "USERS",  // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // Don't group the rows
                null,               // Don't filter by row groups
                null                // The sort order
        );


        User userconnect=new User();
        if (cursor.moveToFirst()) {
            // Extract the image data from the cursor
            userconnect =new User(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),"");
        }

        cursor.close();
//        databaseReader.close();

        return userconnect;
    }


    public Cursor HistoryByEmail(String email){
        List<History> historys=new ArrayList<>();
        String[] projection = {
                "activity,dateDe,withPer,withoutPer,tc1,p,hat,piggy"
        };

        // Define a selection and arguments for the WHERE clause
        String selection = "email_user = ?";
        String[] selectionArgs = { String.valueOf(email) };

        // Execute the query
        Cursor cursor = databaseReader.query(
                "HISTORIQUE",  // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // Don't group the rows
                null,               // Don't filter by row groups
                null                // The sort order
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
//        cursor.close();
//        if (cursor.moveToFirst()) {
//            do{
//                History history=new History(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3),cursor.getDouble(4),cursor.getDouble(5),cursor.getDouble(6));
//                historys.add(history);
//            }while (cursor.moveToNext());
//        }

        return cursor;
    }

    public void deleteHistory(String email){
        String sql = "DELETE FROM HISTORIQUE WHERE email_user = ?";
        databaseReader.delete("HISTORIQUE",  "email_user=?", new String[]{email});




    }


    public History HistoryByAll(String email,String stat,String date){
//        tc1,hat,piggy,p,mer,mudr,casta
        String[] projection = {
                "withPer,withoutPer,tc1,hat,piggy,p,merlin,mudr,casta"
        };

        // Define a selection and arguments for the WHERE clause
        String selection = "email_user = ? and activity=? and dateDe=? ";
        String[] selectionArgs = { String.valueOf(email), String.valueOf(stat),String.valueOf(date)};

        // Execute the query
        Cursor cursor = databaseReader.query(
                "HISTORIQUE",  // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // Don't group the rows
                null,               // Don't filter by row groups
                null                // The sort order
        );
        History history=new History();
        if (cursor.moveToFirst()) {
            history =new History(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),"");
        }

        cursor.close();

        return history;
    }



    public boolean insertHistoryFamily (String email, History history) {
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EMAIL_HIS, email);
        contentValues.put(DatabaseHelper.dateDe, history.getDateDe());
        contentValues.put(DatabaseHelper.ACTIVITY, history.getActivity());
        contentValues.put(DatabaseHelper.tc1, history.getTc1());
        contentValues.put(DatabaseHelper.p, history.getP());
        contentValues.put(DatabaseHelper.piggy, history.getPiggy());
        contentValues.put(DatabaseHelper.hat, history.getHat());
        contentValues.put(DatabaseHelper.merlin, history.getMerlin());
        contentValues.put(DatabaseHelper.casta, history.getCasta());
        contentValues.put(DatabaseHelper.mudr, history.getMudr());

        database.insert("HISTORIQUE", null, contentValues);
//        db.close();
        return true;
    }

    public boolean insertHistoryET (String email, History history) {
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EMAIL_HIS, email);
        contentValues.put(DatabaseHelper.dateDe, history.getDateDe());
        contentValues.put(DatabaseHelper.ACTIVITY, history.getActivity());
        contentValues.put(DatabaseHelper.withPer, history.getWithPer());
        contentValues.put(DatabaseHelper.withoutPer, history.getWithoutPer());
        database.insert("HISTORIQUE", null, contentValues);
//        db.close();
        return true;
    }
}
