package br.com.testwebserviceapi;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.AbstractCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import br.com.testwebserviceapi.Domain.User;

public class HomeActivity extends Activity {
    JSONObject data;
    private RequestQueue mQueue;
    String urlDelete;
    Button btnAtualizar, btnDeletar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle args = getIntent().getExtras();
        String nome = args.getString("nome");
        TextView textView = (TextView) findViewById(R.id.textTV);
        textView.setText(nome);

        Toast.makeText(HomeActivity.this, "Seja Bem Vindo!", Toast.LENGTH_SHORT).show();

        urlDelete = "https://serene-sea-70010.herokuapp.com/login/delete/";
        mQueue = Volley.newRequestQueue(this);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UpdateActivity.class);
                startActivity(intent);

            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonDelete();
            }
        });

    }

    public void jsonDelete() {
        User user = new User();
        String jsonObject = new Gson().toJson(user);

        JsonRequest jsonRequest = new JsonRequest(Method.POST, urlDelete, jsonObject, new Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                Log.i("Resposta", "teste ok");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Log", "ERROR sendJSON >> " + error.toString(), error);

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
            protected Response<Boolean> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new Boolean(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

        };

        jsonRequest.setTag(LoginActivity.class);
        mQueue.add(jsonRequest);
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Deseja sair?");
        builder.setCancelable(true);
        builder.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeContextMenu();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        mQueue.cancelAll("tag");
    }
}
