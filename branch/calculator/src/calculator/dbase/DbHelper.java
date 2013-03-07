/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved.    *
 *																			  *																			*
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved.           *
 * 																			  *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        * 
 * in your installation folder.                                               *
 ******************************************************************************
 */
package calculator.dbase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import calculator.ru.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	// Android's default system path for your application's database.
	// private static String DB_PATH = "/data/data/calculator.ru/databases/";
	private static String DB_PATH;
	private static String DB_NAME = "calc.db";

	private final Context myContext;

	public DbHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = context.getResources().getString(R.string.db_path);
	}

	/**
	 * This constructor copies the database file if the copyDatabase argument is
	 * <code>true</code>. It keeps a reference to the passed context in order to
	 * access the application's assets.
	 * 
	 * @param context
	 *            Context to be used
	 * @param copyDatabase
	 *            If <code>true</code>, the database file is copied (if it does
	 *            not already exist)
	 */
	public DbHelper(Context context, boolean copyDatabase) {
		// call overloaded constructor
		this(context);
		DB_PATH = context.getResources().getString(R.string.db_path);
		// copy database file in case desired
		if (copyDatabase) {
			copyDatabaseFile();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// fill in your code here
	
	}

	/**
	 * <p>
	 * Copy the database file from the assets directory to the proper location
	 * where the application can access it. The database location and name is
	 * given by the constants {@link #DB_PATH} and {@link #DB_NAME}
	 * respectively.
	 * </p>
	 * <p>
	 * If the database file already exists, it will not be overwritten.
	 * </p>
	 */
	public void copyDatabaseFile() {

		// variables
		InputStream myInput = null;
		OutputStream myOutput = null;
		SQLiteDatabase database = null;

		// only proceed in case the database does not exist
		if (!checkDataBaseExistence()) {
			// get the database
			database = this.getReadableDatabase();
			try {
				// Open your local db as the input stream
				myInput = myContext.getAssets().open(DB_NAME);

				// Path to the just created empty db
				String outFileName = DB_PATH + DB_NAME;

				// Open the empty db as the output stream
				myOutput = new FileOutputStream(outFileName);

				// transfer bytes from the input file to the output file
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
			} catch (FileNotFoundException e) {
				// handle your exception here
			} catch (IOException e) {
				// handle your exception here
			} finally {
				try {
					// Close the streams
					myOutput.flush();
					myOutput.close();
					myInput.close();
					// close the database in case it is opened
					if (database != null && database.isOpen()) {
						database.close();
					}

				} catch (Exception e) {
					// handle your exception here
				}
			}
		}
	}

	/**
	 * Returns whether the database already exists.
	 * 
	 * @return <code>true</code> if the database exists, <code>false</code>
	 *         otherwise.
	 */
	private boolean checkDataBaseExistence() {

		// database to be verified
		SQLiteDatabase dbToBeVerified = null;

		try {
			// get database path
			String dbPath = DB_PATH + DB_NAME;
			// try to open the database
			dbToBeVerified = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			// do nothing since the database does not exist
		}

		// in case it exists, close it
		if (dbToBeVerified != null) {
			// close DB
			dbToBeVerified.close();

		}

		// in case there is a DB entity, the DB exists
		return dbToBeVerified != null ? true : false;
	}
}
