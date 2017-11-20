package com.fayne.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mUsername;
    private EditText mPassword;
    private View mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsername = findViewById(R.id.et_username);
        mPassword = findViewById(R.id.et_password);
        mLogin = findViewById(R.id.btn_login);
        mLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginHandle().run();
            }
        });

    }

    class LoginHandle implements Runnable {

        @Override
        public void run() {
            final String username = mUsername.getText().toString();
            final String password = mPassword.getText().toString();
            String connectUrl = "http://10.0.2.2/PhpLoginDemo/login.php";
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String TAG = "LOGIN";
                    Log.e(TAG, s);
                    System.out.println(s);
                    int retCode = 0;
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        retCode = jsonObject.getInt("success");
                        Log.d("retCode", retCode+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (retCode == 1) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this,"用户名或密码错误!",Toast.LENGTH_SHORT).show();
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                public String TAG = "LOG";
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.getMessage(), error);
                }
            };
            StringRequest stringRequest = new StringRequest(Request.Method.POST, connectUrl, listener, errorListener) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", username);
                    map.put("password", password);
                    return map;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}

