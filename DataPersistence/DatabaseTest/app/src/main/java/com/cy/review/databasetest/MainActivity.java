package com.cy.review.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 创建数据库和表：SQLiteOpenHelper -> SQLiteOpenHelper中：getWritableDatabase()/getReadableDatabase() -> onCreate()
 * 升级数据库：SQLiteOpenHelper -> onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
 * 对数据进行的增删改查操作：
 * 1.使用Android提供的API：
 * getWritableDatabase()/getReadableDatabase()返回的SQLiteDatabase对象 -> 借助SQLiteDatabase对象进行增删改查操作
 * SQLiteDatabase对象 -> ContentValues组装数据 -> sqLiteDatabase.insert();
 * SQLiteDatabase对象 -> ContentValues组装数据 -> sqLiteDatabase.update();
 * SQLiteDatabase对象 -> sqLiteDatabase.delete();
 * SQLiteDatabase对象 -> sqLiteDatabase.query()的到游标Cursor对象 -> 遍历Cursor对象获取每一条数据；
 *
 * 2.使用SQL语句
 * db.execSQL()执行SQL语句，
 * 一点点不同是只需将SQL语句中的具体值的部分用占位符来代替，然后由相应参数的字符数组来提供具体的值
 */


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        Button createDatabase=findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                SQLiteOpenHelper中非常重要的两个实例方法getWritableDatabase()和getReadableDatabase(),
                都可以创建或者打开一个现有的数据库，并且返回一个对数据库进行读写操作的对象，区别在于，
                当数据库不可写入的时候，例如磁盘空间已满时，getReadableDatabase()返回的对象将以只读的方式去打开数据库；
                getWritableDatabase()将出现异常。
                 */
                dbHelper.getWritableDatabase();
                /*
                第一次触发这个事件，会检测到当前程序中并没有BookStore.db这个数据库，
                就会创建这个数据库，并且调用MyDatabaseHelper类中的oncreate()方法，那么book表也会得到创建。
                 */
            }
        });

        //增加数据
        Button addData=findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                //开始组装第一条数据
                values.put("author","Dan Brown");
                values.put("price",16.69);
                values.put("pages",454);
                values.put("name","The Da Vinci Code");
                sqLiteDatabase.insert("book",null,values);
                values.clear();
                //开始组装第二条数据
                values.put("author","Dan Brown");
                values.put("price",19.95);
                values.put("pages",510);
                values.put("name","The Lost Symbol");
                sqLiteDatabase.insert("book",null,values);
            }
        });

        //更新数据
        Button updateData=findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("price",10.99);
                sqLiteDatabase.update("book",values,"name=?",new String[]{"The Da Vinci Code"});
            }
        });

        //删除数据
        Button deleteData=findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
                sqLiteDatabase.delete("book","pages>?",new String[]{"500"});
            }
        });

        //查询数据
        Button queryData=findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
                Cursor cursor=sqLiteDatabase.query("book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "name: "+name);
                        Log.d(TAG, "author: "+author);
                        Log.d(TAG, "pages: "+pages);
                        Log.d(TAG, "price: "+price);
                    }while (cursor.moveToNext());
                }
            }
        });
    }
}
