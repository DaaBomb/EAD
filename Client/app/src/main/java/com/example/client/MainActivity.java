package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.client.Retrofit.IMyService;
import com.example.client.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText edt_Login_email,edt_Login_password;
    Button btn_login;

    CompositeDisposable compositeDisposable= new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        edt_Login_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_Login_password= (MaterialEditText) findViewById(R.id.edt_password);
        btn_login=(Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edt_Login_email.getText().toString(),
                        edt_Login_password.getText().toString());
            }
        });

        txt_create_account= (TextView) findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    private void registerUser(String email, String name, String password) {
        compositeDisposable.add(iMyService.registerUser(email,name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>(){
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this,""+response,Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    private void loginUser(String email, String password) {

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email cannot be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password cannot be empty",Toast.LENGTH_LONG).show();
            return;
        }
        compositeDisposable.add(iMyService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>(){
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject res = new JSONObject(response);
                        String msg = res.getString("msg");
                        if(msg.equals("login successful"))
                        {
                            String token = res.getString("token");
                            JSONObject user = res.getJSONObject("user");
                            Intent i=new Intent(MainActivity.this, HomepageActivity.class);
                            i.putExtra("user",  user.toString());
                            Toast.makeText(MainActivity.this,""+token ,Toast.LENGTH_LONG).show();
                            startActivity(i);
                        }
//                        Bundle extras = getIntent().getExtras();
//                        String value=null;
//                        if (extras != null) {
//                            value = extras.getString("token");
//                        }
//                        Toast.makeText(MainActivity.this,""+ value +response ,Toast.LENGTH_LONG).show();
                    }
                })
        );

    }
}
