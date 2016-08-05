package com.example.administrator.hf;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
import com.example.administrator.hf.adapter.student.StudentAdapter;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wangjie.androidinject.annotation.annotations.AIView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.dao.query.Query;
import test.greenDAO.bean.Entity;
import test.greenDAO.dao.DaoMaster;
import test.greenDAO.dao.DaoSession;
import test.greenDAO.dao.EntityDao;


public class MainActivity extends Activity {

    @BindView(R.id.text)
    TextView text;
    @BindView((R.id.add))
    Button add;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.query)
    Button query;
    @BindView(R.id.student_id)
    TextView student_id;
    @BindView(R.id.student_age)
    EditText student_age;
    @BindView(R.id.student_name)
    EditText student_name;
    @BindView(R.id.student_score)
    EditText student_score;
    @BindView(R.id.listview)
    ListView listview;
    List studentList;

    private StudentAdapter studentAdapter;

    private Cursor cursor;

    private String name;
    private Long id;
    private Integer age;
    private Double score;

    public DaoSession daoSession;
    public SQLiteDatabase db;
    public MySQLiteOpenHelper helper;
    public DaoMaster daoMaster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        text=(TextView)findViewById(R.id.text);
//        volleyHttp();
        setupDatabase();
        okhttp();
        initData();

    }

    private void initData() {
        if (studentAdapter == null) {
            studentList = new ArrayList<Entity>();
            String orderBy = EntityDao.Properties.Id.columnName + " DESC";//根据Id降序排序


            cursor = getDb().query(getDaoSession().getEntityDao().getTablename(), getDaoSession().getEntityDao().getAllColumns(), null, null, null, null,orderBy);
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                entity.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                entity.setAge(cursor.getInt(cursor.getColumnIndex("AGE")));
                entity.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                entity.setScore(cursor.getDouble(cursor.getColumnIndex("SCORE")));
                Log.i("hf---", "xxxx"+cursor.getColumnIndex("TEST"));
                studentList.add(entity);
//                student_id.setText("id: " + cursor.getLong(cursor.getColumnIndex("id"))+"");
//                student_name.setText(cursor.getString(cursor.getColumnIndex("name")));
//                student_age.setText(cursor.getInt(cursor.getColumnIndex("age"))+"");
//                student_score.setText(cursor.getDouble(cursor.getColumnIndex("score"))+"");
            }
            cursor.close();
            studentAdapter = new StudentAdapter(this);
            studentAdapter.addAll(studentList);
            listview.setAdapter(studentAdapter);
        }
    }

    public void initStudent() {
        if (!TextUtils.isEmpty(student_id.getText()))
        id=Long.parseLong(student_id.getText().toString().substring(3, student_id.getText().length()));
       name=student_name.getText().toString();
        if (!TextUtils.isEmpty(student_score.getText())) {
            score = Double.parseDouble(student_score.getText().toString());
        }
        if (!TextUtils.isEmpty(student_age.getText()))
        age=Integer.parseInt(student_age.getText().toString());
    }

    @OnClick({R.id.text, R.id.add, R.id.delete, R.id.update, R.id.query})
    public void click(View view) {
        initStudent();
        switch (view.getId()) {
            case R.id.text:
                Toast toast = Toast.makeText(this, "hello world", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case R.id.add:
                addEntity();
                break;
            case R.id.delete:
                deleteEntity(Long.parseLong(student_id.getText().toString().substring(3, student_id.getText().length())));
                break;
            case R.id.update:
                updateList();
                break;
            case R.id.query:
                query(student_name.getText().toString());
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.;
    }

    public void okhttp() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        final Request request = new Request.Builder()
                .url("http://m.weather.com.cn/data/101010100.html")
                .build();
//new call
        Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                Log.d("TAG", response.toString());
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(response.toString());
                    }
                });

            }
        });
    }

    public void volleyHttp() {
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest("http://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gbk&word=%CD%BC%C6%AC&fr=ala&ala=1&alatpl=others&pos=0",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("TAG", response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", error.getMessage(), error);
//            }
//        });
//        mQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addEntity() {
        if (!TextUtils.isEmpty(name)) {
            Entity entity = new Entity(null, name, age, score,123.0);
            //面向对象添加表数据
            getDaoSession().getEntityDao().insert(entity);
            cursor.requery();//刷新
        } else {
            Toast.makeText(MainActivity.this, "name不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    private void deleteEntity(long id) {
        getDaoSession().getEntityDao().deleteByKey(id);
        cursor.requery();
    }

    /**
     * 更新
     */
    private void updateList() {
        Entity entity = new Entity(id, name, age, score,123.0);
        getDaoSession().getEntityDao().update(entity);
        cursor.requery();
    }

    /**
     * 根据name查询
     *
     * @param name
     */
    private void query(String name) {
        if (!TextUtils.isEmpty(this.name)) {
            // Query 类代表了一个可以被重复执行的查询
            Query query = getDaoSession().getEntityDao().queryBuilder()
                    .where(EntityDao.Properties.Name.eq(this.name))
                    .orderAsc(EntityDao.Properties.Id)
                    .build();
            // 查询结果以 List 返回
            List count = query.list();
            Toast.makeText(MainActivity.this, count.size() + "条数据被查到", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "name不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDatabase() {
        //创建数据库
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        helper = new MySQLiteOpenHelper(this, "test", null);
        //得到数据库连接对象
        db = helper.getWritableDatabase();
        //得到数据库管理者
        daoMaster = new DaoMaster(db);
        //得到daoSession，可以执行增删改查操作
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public MySQLiteOpenHelper getHelper() {
        return helper;
    }
}
