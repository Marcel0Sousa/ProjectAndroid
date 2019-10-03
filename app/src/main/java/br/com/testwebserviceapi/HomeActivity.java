package br.com.testwebserviceapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import br.com.testwebserviceapi.Domain.User;

public class HomeActivity extends AppCompatActivity {
    private int MY_SOCKET_TIMEOUT_MS = 20000;
    JSONObject data;
    private RequestQueue mQueue;
    String urlDelete;
    Button btnAtualizar, btnDeletar;
    TextView tvNomeUsur, tvEmailUsur;

    private User usr;
    String email_antigo;
    String senha_antiga;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle args = getIntent().getExtras();
        this.usr = getIntent().getParcelableExtra("dtusr");
        this.email_antigo = usr.getEmail();
        this.senha_antiga = usr.getSenha();
        TextView textView = (TextView) findViewById(R.id.textTV);
        //TextView textView2 = (TextView) findViewById(R.id.textTV2);
        //textView.setText(usr.getNome() + " " + usr.getEmail());

        Toast toast = Toast.makeText(this, "Seja Bem Vindo!", Toast.LENGTH_SHORT);
        toast.show();


        mQueue = Volley.newRequestQueue(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headerView();

        //btnAtualizar = findViewById(R.id.btnAtualizar);
        //btnDeletar = findViewById(R.id.btnDeletar);

        /**btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UpdateActivity.class);

                Bundle params = new Bundle();
                intent.putExtra("dtupdt", usr);
                startActivity(intent);

            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonDelete();
            }
        });**/

    }

    public void headerView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvNomeUsur = headerView.findViewById(R.id.tvNomeUsur);
        tvEmailUsur = headerView.findViewById(R.id.tvEmailUsur);

        tvNomeUsur.setText(usr.getNome());
        tvEmailUsur.setText(usr.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void jsonDelete() {
        Bundle args = getIntent().getExtras();

        this.usr = getIntent().getParcelableExtra("dtusr");
        usr.getId();
        usr.getEmail();
        usr.getSenha();
        String jsonObject = new Gson().toJson(usr);
        urlDelete = "https://serene-sea-70010.herokuapp.com/login/delete/";

        JsonRequest jsonRequest = new JsonRequest(Method.POST, urlDelete, jsonObject, new Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {

                String resposta;

                if (response == true) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    resposta = "Conta excluida com sucesso!";
                    finish();
                } else {
                    resposta = "Ocorreu um erro ao excluir sua conta :(";
                }

                Toast.makeText(getApplicationContext(), resposta, Toast.LENGTH_LONG).show();
                Log.i("Resposta", "teste ok= " +  response);


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

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

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
