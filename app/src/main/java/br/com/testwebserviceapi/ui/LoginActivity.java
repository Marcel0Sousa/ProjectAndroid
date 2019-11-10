package br.com.testwebserviceapi.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.testwebserviceapi.Domain.Assets;
import br.com.testwebserviceapi.Domain.User;
import br.com.testwebserviceapi.R;
import br.com.testwebserviceapi.network.CustomJsonRequestVerify;

import static com.android.volley.Request.Method;

public class LoginActivity extends Activity {

    EditText edtEmail, edtSenha;
    ProgressBar progressBar;
    private int MY_SOCKET_TIMEOUT_MS = 5000;
    private RequestQueue mQueue;
    private Button btnLogin, btnCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmailUser);
        edtSenha = findViewById(R.id.edtSenhaUser);
        btnLogin = findViewById(R.id.btnLogin);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        mQueue = Volley.newRequestQueue(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setVisibility(View.INVISIBLE);
                progressBar = (ProgressBar) findViewById(R.id.progress);
                progressBar.setVisibility(View.VISIBLE);

                if (validaCampos() != false) {

                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                } else {

                    sendJson();
                }
            }
        });

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                onStop();
            }
        });

    }

    public void dados() {
        edtEmail = findViewById(R.id.edtEmailUser);
        edtSenha = findViewById(R.id.edtSenhaUser);
    }

    private boolean validaCampos() {

        boolean respostaValidaCampos = false;
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (respostaValidaCampos = !isEmailValido(email)) {

            edtEmail.setError(Assets.INVALID_EMAIL);
            edtEmail.requestFocus();

        }

        if (respostaValidaCampos = isCampoVazio(senha)) {

            edtSenha.setError(Assets.INVALID_PASSWD);
            edtSenha.requestFocus();

        }

        return respostaValidaCampos;
    }

    private boolean isCampoVazio(String valor) {

        boolean resultadoValor = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultadoValor;
    }

    private boolean isEmailValido(String email) {

        boolean resultadoEmail = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultadoEmail;
    }

    private void noConnection() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("AVISO");
        alert.setMessage(Assets.NO_CONNECTION);
        alert.setNeutralButton("OK", null);
        alert.show();
    }

    public void sendJson() {

        User usr = new User();
        usr.setEmail(edtEmail.getText().toString());
        usr.setSenha(edtSenha.getText().toString());
        String jsonObject = new Gson().toJson(usr);

        CustomJsonRequestVerify jsonReq = new CustomJsonRequestVerify(Method.POST, Assets.API_LOGIN, jsonObject, new Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(Assets.LOG_RESPONSE, response.toString());


                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject data = response.getJSONObject(i);
                        User user = new Gson().fromJson(data.toString(), User.class);
                        int id = data.getInt("id");

                        if (id > 0) {

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("dtusr", user);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(), Assets.INVALID_DATA, Toast.LENGTH_LONG).show();
                            btnLogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);

                        }


                    }
                } catch (JSONException e) {

                    Log.e(Assets.LOG_ERRO, e.getMessage(), e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Log", Assets.LOG_ERRO + error.toString(), error);
                btnLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                noConnection();

            }
        });

        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsonReq.setTag(LoginActivity.class);
        mQueue.add(jsonReq);

    }


    @Override
    public void onStop() {
        super.onStop();
        mQueue.cancelAll("tag");
    }
}
