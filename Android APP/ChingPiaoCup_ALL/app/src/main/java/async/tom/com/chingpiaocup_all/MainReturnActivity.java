package async.tom.com.chingpiaocup_all;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class MainReturnActivity extends AppCompatActivity{
    int min=0;
    int [] reItemN = new int[5];
    String [] rePhoneId = new String[2];
    String Url = "";// Use your own Google sheet URL
    boolean found = false;
    boolean isStayScreen = true;
    boolean devMode = false;

    private View mView;
    private TextView mTxtInstruct, mTxtResult, mTxtInstruct2, mTxtLogCat, mTxtSpace, mTxtCup, mTxtBox, mTxtSpoon, mTxtFork, mTxtChopstick;
    private EditText mEdtPhone, mEdtID;
    private NumberPicker mNumPicCup, mNumPicBox, mNumPicSpoon, mNumPicFork, mNumPicChopstick;
    private Button mBtnOK, mBtnCancel, mBtnSend;
    private Switch mSwtReturnAll, mSwtUsePhone;
    BottomNavigationView navigation;

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

                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(getApplicationContext(), MainLendLogActivity.class));
                    onPause();
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_return);

        mView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTxtInstruct = (TextView) findViewById(R.id.txtInstruction);
        mTxtResult = (TextView) findViewById(R.id.txtResult);
        mTxtInstruct2 = (TextView) findViewById(R.id.txtInstruct2);
        mTxtLogCat = (TextView) findViewById(R.id.logCat);
        mTxtSpace = (TextView) findViewById(R.id.txtSpace);
        mTxtCup = (TextView) findViewById(R.id.txtCup);
        mTxtBox = (TextView) findViewById(R.id.txtBox);
        mTxtSpoon = (TextView) findViewById(R.id.txtSpoon);
        mTxtFork = (TextView) findViewById(R.id.txtFork);
        mTxtChopstick = (TextView) findViewById(R.id.txtChopstick);
        mEdtPhone = (EditText) findViewById(R.id.edtPhoneNum);
        mEdtID = (EditText) findViewById(R.id.edtID);
        mNumPicCup = (NumberPicker) findViewById(R.id.numPicCup);
        mNumPicBox = (NumberPicker) findViewById(R.id.numPicBox);
        mNumPicSpoon = (NumberPicker) findViewById(R.id.numPicSpoon);
        mNumPicFork = (NumberPicker) findViewById(R.id.numPicFork);
        mNumPicChopstick = (NumberPicker) findViewById(R.id.numPicChopstick);
        mBtnOK = (Button) findViewById(R.id.btnOK);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSend = (Button) findViewById(R.id.btnSend);
        mSwtReturnAll = (Switch) findViewById(R.id.swtReturnAll);
        mSwtUsePhone = (Switch) findViewById(R.id.swtUsePhone);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        mEdtPhone.setEnabled(false);

        mTxtCup.setEnabled(false);
        mTxtBox.setEnabled(false);
        mTxtSpoon.setEnabled(false);
        mTxtFork.setEnabled(false);
        mTxtChopstick.setEnabled(false);
        mTxtInstruct2.setEnabled(false);
        mTxtLogCat.setVisibility(8);

        mNumPicCup.setMinValue(min);
        mNumPicCup.setMaxValue(min);
        mNumPicCup.setEnabled(false);
        mNumPicBox.setMinValue(min);
        mNumPicBox.setMaxValue(min);
        mNumPicBox.setEnabled(false);
        mNumPicSpoon.setMinValue(min);
        mNumPicSpoon.setMaxValue(min);
        mNumPicSpoon.setEnabled(false);
        mNumPicFork.setMinValue(min);
        mNumPicFork.setMaxValue(min);
        mNumPicFork.setEnabled(false);
        mNumPicChopstick.setMinValue(min);
        mNumPicChopstick.setMaxValue(min);
        mNumPicChopstick.setEnabled(false);

        mBtnOK.setEnabled(true);
        mBtnSend.setEnabled(false);

        mSwtReturnAll.setEnabled(false);
        mSwtReturnAll.setChecked(false);
        mSwtUsePhone.setChecked(false);

        mEdtID.requestFocus();
        InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        //mEdtPhone.setOnFocusChangeListener(edtPhoneFocusChange);
        //mEdtID.setOnFocusChangeListener(edtIDFocusChange);
        mBtnOK.setOnClickListener(btnOKClick);
        mBtnCancel.setOnClickListener(btnCancelClick);
        mBtnSend.setOnClickListener(btnSendClick);
        mSwtUsePhone.setOnCheckedChangeListener(mSwtUsePhoneChange);
        mSwtReturnAll.setOnCheckedChangeListener(mSwtReturnAllChange);
        mTxtSpace.setOnLongClickListener(mTxtSpaceLongClk);
        mEdtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(mSwtUsePhone.isChecked()){
                    int l = s.toString().length();
                    if(l==9) {mTxtInstruct.setText("輸入 \'市話\'"); mBtnOK.setEnabled(true);}
                    else if(l==10) {mTxtInstruct.setText("輸入 \'手機\'"); mBtnOK.setEnabled(true);}
                    else {mTxtInstruct.setText("請輸入電話號碼"); mBtnOK.setEnabled(false);}
                }
            }
        });
        mEdtID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!mSwtUsePhone.isChecked()){
                    int l = s.toString().length();
                    mTxtInstruct.setText("請掃描QR Code");
                    if(l!=0) { mBtnOK.setText("查詢借用狀況");}
                    else { mBtnOK.setText("掃描QR Code");}
                }
            }
        });
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        scanBarcode(mView);
    }
    @Override
    protected void onStart(){
        super.onStart();
        isStayScreen = true;
    }
    @Override
    protected void onResume(){
        super.onResume();
        isStayScreen = true;
        navigation.setSelectedItemId(R.id.navigation_dashboard);
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
        //finish();
    }

    private CompoundButton.OnCheckedChangeListener mSwtUsePhoneChange = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                mTxtInstruct.setText("請輸入電話號碼");
                mEdtID.setText("");
                mEdtID.setEnabled(false);
                mEdtPhone.setEnabled(true);
                mEdtPhone.requestFocus();
                mBtnOK.setText("查詢借用狀況");
                mBtnOK.setEnabled(false);
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mEdtPhone, 0);
            }else{
                mTxtInstruct.setText("請掃描QR Code");
                mEdtPhone.setText("");
                mEdtPhone.setEnabled(false);
                mEdtID.setEnabled(true);
                mEdtID.requestFocus();
                mBtnOK.setText("掃描QR Code");
                mBtnOK.setEnabled(true);
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                scanBarcode(mView);
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener mSwtReturnAllChange = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                mNumPicCup.setValue(mNumPicCup.getMaxValue());
                mNumPicBox.setValue(mNumPicBox.getMaxValue());
                mNumPicSpoon.setValue(mNumPicSpoon.getMaxValue());
                mNumPicFork.setValue(mNumPicFork.getMaxValue());
                mNumPicChopstick.setValue(mNumPicChopstick.getMaxValue());
                Toast.makeText(MainReturnActivity.this, "選擇全部歸還", Toast.LENGTH_LONG).show();
            }else{
                mNumPicCup.setValue(0);
                mNumPicBox.setValue(0);
                mNumPicSpoon.setValue(0);
                mNumPicFork.setValue(0);
                mNumPicChopstick.setValue(0);
            }
        }
    };

    private View.OnClickListener btnCancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if( mSwtUsePhone.isChecked() ){
                mEdtPhone.setText("");
                mEdtPhone.setEnabled(true);
                mEdtPhone.requestFocus();
                mTxtInstruct2.setText("搜尋結果");
                mBtnOK.setEnabled(false);
                mSwtReturnAll.setEnabled(true);
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mEdtPhone, 0);
            }else{
                mEdtID.setText("");
                mEdtID.setEnabled(true);
                mEdtID.requestFocus();
                mTxtInstruct2.setText("搜尋結果");
                mBtnOK.setEnabled(true);
                mSwtReturnAll.setEnabled(true);
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                scanBarcode(v);
            }
            if(mSwtReturnAll.isEnabled()){
                mTxtResult.setText("搜尋結果");
                mTxtCup.setEnabled(false);
                mTxtBox.setEnabled(false);
                mTxtSpoon.setEnabled(false);
                mTxtFork.setEnabled(false);
                mTxtChopstick.setEnabled(false);
                mTxtInstruct2.setEnabled(false);

                mNumPicCup.setMaxValue(0);
                mNumPicCup.setValue(0);
                mNumPicCup.setEnabled(false);
                mNumPicBox.setMaxValue(0);
                mNumPicBox.setValue(0);
                mNumPicBox.setEnabled(false);
                mNumPicSpoon.setMaxValue(0);
                mNumPicSpoon.setValue(0);
                mNumPicSpoon.setEnabled(false);
                mNumPicFork.setMaxValue(0);
                mNumPicFork.setValue(0);
                mNumPicFork.setEnabled(false);
                mNumPicChopstick.setMaxValue(0);
                mNumPicChopstick.setValue(0);
                mNumPicChopstick.setEnabled(false);

                mBtnSend.setEnabled(false);

                mSwtReturnAll.setEnabled(false);
                mSwtReturnAll.setChecked(false);
                mSwtUsePhone.setEnabled(true);
            }
        }
    };

    private View.OnClickListener btnOKClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSwtUsePhone.isChecked()){
                mEdtPhone.setEnabled(false);
                mEdtID.setEnabled(false);
                mTxtResult.setText("搜尋結果");
                mSwtUsePhone.setEnabled(false);
                Snackbar.make(v, "送出查詢資料...", Snackbar.LENGTH_LONG).show();
                new FindArray().execute();
            }else if(!mSwtUsePhone.isChecked()){
                if(mEdtID.getText().toString().equals("")){
                    mEdtPhone.setEnabled(false);
                    mEdtID.setEnabled(true);
                    mTxtResult.setText("搜尋結果");
                    mSwtUsePhone.setEnabled(false);
                    scanBarcode(v);
                }else{
                    mEdtPhone.setEnabled(false);
                    mEdtID.setEnabled(false);
                    mTxtResult.setText("搜尋結果");
                    mSwtUsePhone.setEnabled(false);
                    Snackbar.make(v, "送出查詢資料...", Snackbar.LENGTH_LONG).show();
                    new FindArray().execute();
                }
            }

        }
    };
    private View.OnClickListener btnSendClick = new View.OnClickListener(){
        @SuppressLint("WrongConstant")
        @Override
        public void onClick(final View v) {
            final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(MainReturnActivity.this);
            altDlgBuilder.setTitle("歸還確認");
            altDlgBuilder.setMessage("電話: " + mEdtPhone.getText() + "  ID: " + mEdtID.getText() + "\n杯子: " + mNumPicCup.getValue() + "  餐盒: " + mNumPicBox.getValue() + "  湯匙: " + mNumPicSpoon.getValue() + "  叉子: " + mNumPicFork.getValue() + "  筷子: " + mNumPicChopstick.getValue() +"\n確認歸還?");
            altDlgBuilder.setCancelable(false);
            altDlgBuilder.setPositiveButton("是", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    mEdtPhone.setEnabled(false);
                    mEdtID.setEnabled(false);
                    mNumPicCup.setEnabled(false);
                    mNumPicBox.setEnabled(false);
                    mNumPicSpoon.setEnabled(false);
                    mNumPicFork.setEnabled(false);
                    mNumPicChopstick.setEnabled(false);

                    new Return().execute();
                    Snackbar.make(v, "送出歸還資料...", Snackbar.LENGTH_LONG).show();

                    mEdtPhone.setEnabled(false);
                    mEdtPhone.setText("");
                    mEdtID.setText("");
                    mEdtID.setEnabled(true);
                    mEdtID.requestFocus();

                    mTxtResult.setText("搜尋結果");
                    mTxtCup.setEnabled(false);
                    mTxtBox.setEnabled(false);
                    mTxtSpoon.setEnabled(false);
                    mTxtFork.setEnabled(false);
                    mTxtChopstick.setEnabled(false);
                    mTxtInstruct2.setEnabled(false);

                    mSwtReturnAll.setEnabled(false);

                    mNumPicCup.setMaxValue(min);
                    mNumPicCup.setValue(min);
                    mNumPicBox.setMaxValue(min);
                    mNumPicBox.setValue(min);
                    mNumPicSpoon.setMaxValue(min);
                    mNumPicSpoon.setValue(min);
                    mNumPicFork.setMaxValue(min);
                    mNumPicFork.setValue(min);
                    mNumPicChopstick.setMaxValue(min);
                    mNumPicChopstick.setValue(min);

                    mBtnSend.setEnabled(false);
                    if(mSwtUsePhone.isChecked()){
                        mBtnOK.setEnabled(false);
                    }else{
                        mBtnOK.setEnabled(true);
                    }

                    mSwtReturnAll.setEnabled(false);
                    mSwtReturnAll.setChecked(false);
                    mSwtUsePhone.setEnabled(true);
                    mSwtUsePhone.setChecked(false);

                    for(int i=0; i<5; i++){
                        reItemN[i] = 0;
                    }
                    for(int i=0; i<2; i++){
                        rePhoneId[i] = "";
                    }

                }
            });
            altDlgBuilder.setNeutralButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            altDlgBuilder.show();
        }
    };

    private View.OnLongClickListener mTxtSpaceLongClk = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
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

    public void scanBarcode(View v){
        Intent intent = new Intent(this, MainScanActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==0) {
            if(requestCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    mEdtID.setText(barcode.displayValue);
                    mEdtID.setEnabled(false);
                    Toast.makeText(MainReturnActivity.this,"QR Code 結果 : " + barcode.displayValue, Toast.LENGTH_LONG).show();
                    Snackbar.make(mView, "送出查詢資料...", Snackbar.LENGTH_LONG).show();
                    new FindArray().execute();
                } else {
                    mBtnOK.setEnabled(true);
                    Toast.makeText(MainReturnActivity.this, "掃不到QR Code", Toast.LENGTH_LONG).show();
                }
            }
        } else {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class FindArray extends AsyncTask<Void, Void, Void>{
        String result = "";
        String reStringF = "";
        String phoneNum = mEdtPhone.getText().toString(), id = mEdtID.getText().toString();
        String postParam = "action=FindArray&phone=" + phoneNum + "&id=" + id;
        String [] reArray = new String[7];

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mTxtLogCat.setText(result + "PostExecute_Done\n");
            if(reStringF.equals("Not Found")  || reStringF.equals("No Data Input") || reStringF.equals("Already Reurned All")){
                found = false;
                mTxtResult.setText("搜尋結果: " + reStringF);
                mTxtInstruct2.setEnabled(false);
                mSwtReturnAll.setEnabled(false);
                mBtnSend.setEnabled(false);
                if(mSwtUsePhone.isChecked()){
                    mBtnOK.setEnabled(false);
                }else{
                    mBtnOK.setEnabled(true);
                }
                Toast.makeText(MainReturnActivity.this, reStringF, Toast.LENGTH_LONG).show();
            }else{
                reArray = reStringF.split(",");
                for(int i=0; i<5; i++){
                    reItemN[i] = 0;
                }
                if(reArray.length != 0) {
                    rePhoneId[0] = reArray[0];
                    rePhoneId[1] = reArray[1];
                    reItemN[0] = Integer.parseInt(reArray[2]);
                    reItemN[1]  = Integer.parseInt(reArray[3]);
                    reItemN[2] = Integer.parseInt(reArray[4]);
                    reItemN[3] = Integer.parseInt(reArray[5]);
                    reItemN[4] = Integer.parseInt(reArray[6]);
                    found = true;

                    mTxtResult.setText("搜尋結果: 電話=" + rePhoneId[0] + " ID=" + rePhoneId[1] + "\n杯子=" + reItemN[0] + " 餐盒=" + reItemN[1] + " 湯匙=" + reItemN[2] + " 叉子=" + reItemN[3] + " 筷子=" + reItemN[4]);

                    mTxtLogCat.setText(result + "Enter Found\n");
                    mTxtInstruct2.setEnabled(true);
                    mTxtInstruct2.setVisibility(View.VISIBLE);

                    mNumPicCup.setMaxValue(reItemN[0]);
                    mNumPicBox.setMaxValue(reItemN[1]);
                    mNumPicSpoon.setMaxValue(reItemN[2]);
                    mNumPicFork.setMaxValue(reItemN[3]);
                    mNumPicChopstick.setMaxValue(reItemN[4]);
                    mNumPicCup.setValue(min);
                    mNumPicBox.setValue(min);
                    mNumPicSpoon.setValue(min);
                    mNumPicFork.setValue(min);
                    mNumPicChopstick.setValue(min);

                    if (reItemN[0] > 0) {
                        mTxtCup.setEnabled(true);
                        mTxtCup.setVisibility(View.VISIBLE);
                        mNumPicCup.setEnabled(true);
                        mNumPicCup.setVisibility(View.VISIBLE);
                    }
                    if (reItemN[1] > 0) {
                        mTxtBox.setEnabled(true);
                        mTxtBox.setVisibility(View.VISIBLE);
                        mNumPicBox.setEnabled(true);
                        mNumPicBox.setVisibility(View.VISIBLE);

                    }
                    if (reItemN[2] > 0) {
                        mTxtSpoon.setEnabled(true);
                        mTxtSpoon.setVisibility(View.VISIBLE);
                        mNumPicSpoon.setEnabled(true);
                        mNumPicSpoon.setVisibility(View.VISIBLE);
                    }
                    if (reItemN[3] > 0) {
                        mTxtFork.setEnabled(true);
                        mTxtFork.setVisibility(View.VISIBLE);
                        mNumPicFork.setEnabled(true);
                        mNumPicFork.setVisibility(View.VISIBLE);
                    }
                    if (reItemN[4] > 0) {
                        mTxtChopstick.setEnabled(true);
                        mTxtChopstick.setVisibility(View.VISIBLE);
                        mNumPicChopstick.setEnabled(true);
                        mNumPicChopstick.setVisibility(View.VISIBLE);
                    }

                    mBtnOK.setEnabled(false);
                    mBtnSend.setEnabled(true);
                    mBtnSend.setVisibility(View.VISIBLE);

                    mSwtReturnAll.setEnabled(true);
                    mSwtReturnAll.setVisibility(View.VISIBLE);
                    Toast.makeText(MainReturnActivity.this, "Found", Toast.LENGTH_LONG).show();
                }
            }
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
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = result + "HTTP_OK_Done\n";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
                    result = result + "NewBufferedReader_Done\n";
                    String str;
                    result = result + "StartReading_Done\n";
                    while ((str = reader.readLine()) != null) {
                        reStringF = reStringF + str;
                        result = result + str + "\n";
                        result = result + "Line_Done";
                    }
                    reader.close();
                    result = result + "\nHTTP_OK: " + responseCode + "\ndoInBackGround_Done\n";
                } else {
                    result = result + "ERROR: " + responseCode + "\n";
                }
                result = result + "DisConnect_Done\n";
            } catch (IOException e) {
                e.printStackTrace();
                result = result + "CatchException\n";
            } finally {
                if (urlConn != null) {
                    urlConn.disconnect();
                }
            }
            return null;
        }
    }

    class Return extends AsyncTask<Void, Void, Void>{
        String phoneNum = mEdtPhone.getText().toString(), id = mEdtID.getText().toString();
        int nCup = mNumPicCup.getValue(), nBox = mNumPicBox.getValue(), nSpoon = mNumPicSpoon.getValue(), nFork = mNumPicFork.getValue(), nChopstick = mNumPicChopstick.getValue();
        String result = "";
        String reStringR = "";
        String postParam = "action=Return&phone=" + phoneNum + "&id=" + id + "&nCup=" + nCup + "&nBox=" + nBox + "&nSpoon=" + nSpoon + "&nFork=" + nFork + "&nChopstick=" + nChopstick ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mTxtLogCat.setText(result + "PostExecute_Done\n");
            Toast.makeText(MainReturnActivity.this, reStringR,Toast.LENGTH_LONG).show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            found = false;
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
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = result + "HTTP_OK_Done\n";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
                    result = result + "NewBufferedReader_Done\n";
                    String str;
                    result = result + "StartReading_Done\n";
                    while ((str = reader.readLine()) != null) {
                        reStringR = reStringR + str;
                        result = result + str + "\n";
                        result = result + "Line_Done";
                    }
                    reader.close();
                    result = result + "\nHTTP_OK: " + responseCode + "\ndoInBackGround_Done\n";
                } else {
                    result = result + "ERROR: " + responseCode + "\n";
                }
                result = result + "DisConnect_Done\n";
            } catch (IOException e) {
                e.printStackTrace();
                result = result + "CatchException\n";
            } finally {
                if (urlConn != null) {
                    urlConn.disconnect();
                }
            }
            return null;
        }
    }
    public class Reset{
        public void resetVariable(){
            for(int i=0; i<5; i++){
                reItemN[i] = 0;
            }
            for(int i=0; i<2; i++){
                rePhoneId[i] = "";
            }
        }
    }
}
