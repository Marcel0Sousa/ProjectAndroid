package br.com.testwebserviceapi;

import androidx.appcompat.app.AlertDialog;

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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import br.com.testwebserviceapi.Domain.User;


import static com.android.volley.Request.*;

public class LoginActivity extends Activity {

    private int MY_SOCKET_TIMEOUT_MS = 20000;
    private RequestQueue mQueue;
    Button btnLogin;
    EditText edtEmail, edtSenha;
    private String url;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmailUser);
        edtSenha = findViewById(R.id.edtSenhaUser);
        btnLogin = findViewById(R.id.btnLogin);
        url = "https://serene-sea-70010.herokuapp.com/login/verify/";
        mQueue = Volley.newRequestQueue(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setVisibility(View.INVISIBLE);
                progressBar = (ProgressBar) findViewById(R.id.progress);
                progressBar.setVisibility(View.VISIBLE);

                if (validaCampos() != false ){

                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                } else {

                    sendJson();

                }

            }
        });


    }

    private boolean validaCampos() {

        boolean respostaValidaCampos = false;
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (respostaValidaCampos =! isEmailValido(email)) {
            edtEmail.setError("Email inválido");
            edtEmail.requestFocus();

        }
        if (respostaValidaCampos = isCampoVazio(senha)) {
            edtSenha.setError("Senha inválida");
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
        alert.setMessage("Verifique sua conexão com a internet!");
        alert.setNeutralButton("OK", null);
        alert.show();
    }


    public void sendJson() {

        User usr = new User();
        usr.setEmail(edtEmail.getText().toString());
        usr.setSenha(edtSenha.getText().toString());

        String jsonObject = new Gson().toJson(usr);

        JsonRequest jsonReq = new JsonRequest(Method.POST, url, jsonObject, new Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("RESPOSTA >> ", response.toString());


                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject data = response.getJSONObject(i);
                        User user = new Gson().fromJson(data.toString(), User.class);
                        int id = data.getInt("id");

                        if(id > 0) {

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("dtusr", user);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(), "email ou senha incorretos", Toast.LENGTH_LONG).show();
                            btnLogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);

                        }


                    }
                } catch (JSONException e) {

                    Log.e("LOG ERRO >>>", e.getMessage(), e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Log", "ERROR sendJSON >> " + error.toString(), error);
                btnLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                noConnection();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
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
