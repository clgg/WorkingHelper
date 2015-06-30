package cn.niuco.library.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cn.niuco.library.db.bean.UserInfo;

/**
 * Created by Administrator on 2015/5/7.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String TABLE_NAME = "sqlite-test.db";
    /**
     * userDao ，每张表对于一个
     */
    private Dao<UserInfo, Integer> userDao;

    private DataBaseHelper(Context context)
    {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, UserInfo.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTable(connectionSource, UserInfo.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static DataBaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DataBaseHelper getHelper(Context context)
    {
        if (instance == null)
        {
            synchronized (DataBaseHelper.class)
            {
                if (instance == null)
                    instance = new DataBaseHelper(context);
            }
        }

        return instance;
    }

    /**
     * 获得userDao
     *
     * @return
     * @throws java.sql.SQLException
     */
    public Dao<UserInfo, Integer> getUserDao() throws SQLException
    {
        if (userDao == null)
        {
            userDao = getDao(UserInfo.class);
        }
        return userDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
        userDao = null;
    }

}
