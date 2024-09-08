package fsm.miaad.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "USERS";
    public static final String TABLE_IMAGE = "IMAGES";
    public static final String TABLE_HISTORIQUE = "HISTORIQUE";

    // Table columns
    public static final String FNAME = "firstname";
    public static final String LNAME = "lastname";
    public static final String EMAIL="email";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String EMAIL_IMAGE="email_image";
    public static final String PASSWORD = "password";
    public static final String _ID = "_id";
    public static final String _ID_IMAGE = "_id_image";
    public static final String IMAGE="image";
    public static final String _IDHIS = "id";
    public static final String ACTIVITY="activity";
    public static final String dateDe="dateDe";
    public static final String withPer="withPer";
    public static final String EMAIL_HIS="email_user";
    public static final String withoutPer="withoutPer";
    public static final  String tc1="tc1";
    public static final  String p="p";
    public static final  String hat="hat";
    public static final  String piggy="piggy";
    public static final  String merlin="merlin";
    public static final  String casta="casta";
    public static final  String mudr="mudr";



    // Database Information
    static final String DB_NAME = "PROJECT.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query : User
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FNAME + " TEXT NOT NULL, " + LNAME + " TEXT NOT NULL,"+ EMAIL + " TEXT NOT NULL,"+ PASSWORD + " TEXT NOT NULL,"+ PHONE + " TEXT ,"+ ADDRESS + " TEXT );";

    // Creating table query : Images
    private static final String CREATE_TABLE_Images = "create table " + TABLE_IMAGE + "(" + _ID_IMAGE
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL_IMAGE + " TEXT NOT NULL, " + IMAGE + " BLOB);";

    // Creating table query : historique
    private static final String CREATE_TABLE_historique  = "create table " + TABLE_HISTORIQUE + "(" + _IDHIS
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACTIVITY + " TEXT , " + EMAIL_HIS + " TEXT , " + dateDe + " TEXT NOT NULL, "
            + tc1 + " TEXT,"+ p + " TEXT,"+ casta + " TEXT,"+mudr + " TEXT,"+merlin + " TEXT,"+piggy + " TEXT,"+ hat + " TEXT,"+ withPer + " TEXT,"+ withoutPer + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_Images);
        db.execSQL(CREATE_TABLE_historique);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_Images);
        onCreate(db);
    }
}
