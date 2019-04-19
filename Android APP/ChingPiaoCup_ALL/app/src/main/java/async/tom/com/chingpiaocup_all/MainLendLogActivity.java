package async.tom.com.chingpiaocup_all;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import java.lang.String;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class MainLendLogActivity extends AppCompatActivity {
    int i=0;
    String Url = ""; // Use your own Google sheet URL
    String result;
    String input;
    boolean isStayScreen = true;
    //String jsonInput;
    //String jsonString = "[{time:'Fri Sep 14 2018 11:25:42 GMT+0800 (HKT)',LR:'Lend',phone:'975032589',id:'87929234023',cup:0,box:3,spoon:4,fork:4,chopstick:9},{time:'Fri Sep 14 2018 11:25:48 GMT+0800 (HKT)',LR:'Lend',phone:'975032589',id:'87929234023',cup:0,box:3,spoon:4,fork:4,chopstick:9},{time:'Fri Sep 14 2018 11:25:52 GMT+0800 (HKT)',LR:'Lend',phone:'936248338',id:'32641088438',cup:5,box:2,spoon:7,fork:8,chopstick:2},{time:'Fri Sep 14 2018 11:25:58 GMT+0800 (HKT)',LR:'Lend',phone:'908293039',id:'75018849023',cup:1,box:3,spoon:1,fork:1,chopstick:0},{time:'Fri Sep 14 2018 11:26:03 GMT+0800 (HKT)',LR:'Lend',phone:'936248338',id:'32641088438',cup:5,box:2,spoon:7,fork:8,chopstick:2}]";
    //Type listType;
    //Gson gson = (List<Order>) new Gson();
    //List<Order> orders = new ArrayList<Order>();

    View mView;
    private TextView mTextMessage, mTxtLendLog, mTxtLogCat, mTxtSpace, mTxtStatus;
    private Button mBtnTest;
    private BottomNavigationView navigation;

    private static class StaticHandler extends Handler {
        private final WeakReference<MainLendLogActivity> mActivity;
        private StaticHandler(MainLendLogActivity activity) {
            mActivity = new WeakReference<MainLendLogActivity>(activity);
        }
    }
    private final StaticHandler mHandler = new StaticHandler(this);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(), MainLendActivity.class));
                    onPause();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(getApplicationContext(), MainReturnActivity.class));
                    onPause();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };
    private View.OnClickListener btnTestClick = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            result = "";
            input = "";
            //jsonInput = "";
            mTxtStatus.setText("開始更新");
            new LendLog().execute();
            //Collection 作法 不成功
            //Type collecttionType = new TypeToken<Collection<Order>>(){}.getType();
            //Gson gson = new Gson();
            //Collection<Order> ordersC = gson.fromJson(jsonInput, collecttionType);

            //ArrayList 作法 不成功
            //Type listType = new TypeToken<ArrayList<Order>>(){}.getType();
            //orders = (ArrayList<Order>) new Gson().fromJson(jsonInput, listType);
            //mTxtLendLog.setText((CharSequence) orders.get(1));
            //if(orders.isEmpty()) mTxtLendLog.setText("No Data in List");
            //else mTxtLendLog.setText("Data in List");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lend_log);
        UpdateLendLog updateLendLog = new UpdateLendLog();

        mView = (View) findViewById(android.R.id.content);
        mTextMessage = (TextView) findViewById(R.id.message);
        mTxtLendLog = (TextView) findViewById(R.id.txtLendLog);
        mTxtLogCat = (TextView) findViewById(R.id.txtLogCat);
        mTxtSpace = (TextView) findViewById(R.id.txtSpace);
        mTxtStatus = (TextView) findViewById(R.id.txtStatus);
        mBtnTest = (Button) findViewById(R.id.btnTest);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        mTxtLogCat.setVisibility(View.GONE);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBtnTest.setOnClickListener(btnTestClick);
        mTxtSpace.setOnLongClickListener(txtSpaceLongClk);

        updateLendLog.setHandler(mHandler);
        updateLendLog.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStayScreen = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStayScreen = true;
        navigation.setSelectedItemId(R.id.navigation_notifications);
    }

    @Override
    protected void onPause(){
        super.onPause();
        isStayScreen = false;
    }
    @Override
    protected void onStop(){
        super.onStop();
        isStayScreen = false;
        finish();
    }
    private View.OnLongClickListener txtSpaceLongClk = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v){
            if(mTxtLogCat.getVisibility() == View.VISIBLE){
                mTxtLogCat.setVisibility(View.GONE);;
                Snackbar.make(mView, "已關閉偵錯模式，再次長按以開啟", Snackbar.LENGTH_LONG).show();
            }
            else{
                mTxtLogCat.setVisibility(View.VISIBLE);
                Snackbar.make(mView, "已啟動偵錯模式，再次長按以取消", Snackbar.LENGTH_LONG).show();
            }
            return false;
        }

    };

    class LendLog extends AsyncTask<Void, Integer, Void>{
        String result = "Enter Async Task_Done\n";
        //String postParam = "action=LendLog";
        String postParam = "action=LendLogTxt";

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mTxtStatus.setText("更新完成");
            mTxtLogCat.setText(result + "PostExecute_Done");
            mTxtLendLog.setText(input);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mTxtStatus.setText("更新中...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            HttpURLConnection urlConn = null;
            try {
                url = new URL(Url);
                result = result + "NewURL_Done\n";
                urlConn = (HttpURLConnection) url.openConnection();
                result = result + "OpenURL_Done\n";
                urlConn.setRequestMethod("POST");
                result = result + "SetPostMethod_Done\n";
                urlConn.setRequestProperty("Content-Length", "" + Integer.toString(postParam.getBytes().length));
                result = result + "SetRequestProperty_Done\n";
                urlConn.setDoInput(true);
                result = result + "SetInput_Done\n";
                urlConn.setDoOutput(true);
                result = result + "SetOutput_Done\n";
                DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
                result = result + "NewDataOutputStream_Done\n";
                wr.writeBytes(postParam);
                wr.flush();
                wr.close();
                InputStream is = urlConn.getInputStream();
                int responseCode = urlConn.getResponseCode();
                if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                    result = result + "HTTP_OK_Done\n";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
                    result = result + "NewBufferedReader_Done\n";
                    String str;
                    input = "";
                    result = result + "StartReading_Done\n";
                    while((str=reader.readLine()) != null){
                        //JSON 作法 不成功
                        //jsonInput = str;
                        input = input + str + "\n";
                        result = result + str + "\n";
                        result = result + "Line_Done" ;
                        publishProgress();
                    }
                    reader.close();
                    result = result + "\nHTTP_OK: " + responseCode + "\ndoInBackGround_Done\n";
                }else { result = result + "ERROR: " + responseCode + "\n"; }
                result = result + "DisConnect_Done\n";
            } catch (IOException e) {
                e.printStackTrace();
                result = result + "CatchException\n";
            } finally {
                if(urlConn != null){
                    urlConn.disconnect();
                }
            }
            return null;
        }
    }

    public class UpdateLendLog extends Thread{
        private Handler mHandler;
        public void run(){
            while(isStayScreen){
                /*
                Context context = new Context();
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;*/
                try {
                    Thread.sleep(2000);
                    new LendLog().execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void setHandler(Handler h){
            mHandler = h;
        }
    }

}