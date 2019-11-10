package br.com.testwebserviceapi.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import static com.android.volley.Request.*;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import br.com.testwebserviceapi.Domain.Assets;
import br.com.testwebserviceapi.Domain.User;
import br.com.testwebserviceapi.R;
import br.com.testwebserviceapi.network.CustomJsonRequestCreate;

public class RegisterActivity extends AppCompatActivity {

    private Volley volley;
    private RequestQueue mQueue;
    private Button btnRegister;
    private EditText edtNome, edtSobrenome, edtEmail, edtSenha,
            edtDescricao, edtDate, edtSexo;
    ProgressBar progressReg;

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNome = findViewById(R.id.txtNameRegister);
        edtSobrenome = findViewById(R.id.txtLastNameRegister);
        edtEmail = findViewById(R.id.txtEmailRegister);
        edtSenha = findViewById(R.id.txtPasswdRegister);
        edtDescricao = findViewById(R.id.txtDescRegister);
        edtDate = findViewById(R.id.txtDateRegister);
        edtSexo = findViewById(R.id.txtSexoRegister);
        btnRegister = findViewById(R.id.btnRegister);

        mQueue = Volley.newRequestQueue(this);

        // Calendar EditText > edtDate
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                edtDate.setText(date);
            }
        };

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        edtDate.setText(date);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegister.setVisibility(View.INVISIBLE);
                progressReg = (ProgressBar) findViewById(R.id.progressReg);
                progressReg.setVisibility(View.VISIBLE);
                JsonRegister();
            }
        });
    }

    private void JsonRegister() {

        User user = new User();
        user.setNome(edtNome.getText().toString());
        user.setSobrenome(edtSobrenome.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setSenha(edtSenha.getText().toString());
        user.setDescricao(edtDescricao.getText().toString());
        user.setImg("img");
        user.setData(edtDate.getText().toString());
        user.setSexo(edtSexo.getText().toString());
        user.setTipo(1);

        String jsonObject = new Gson().toJson(user);

        CustomJsonRequestCreate jsonReq = new CustomJsonRequestCreate(Method.POST, Assets.API_REGISTER, jsonObject, new Listener<Integer>() {

            @Override
            public void onResponse(Integer response) {
                Log.i(Assets.LOG_RESPONSE, response.toString());

                String resposta;

                if (response == -2) {

                    resposta = Assets.INVALID_EMAIL;
                    btnRegister.setVisibility(View.VISIBLE);
                    progressReg.setVisibility(View.INVISIBLE);
                } else if (response == -1) {
                    resposta = Assets.USER_EXISTS;
                    btnRegister.setVisibility(View.VISIBLE);
                    progressReg.setVisibility(View.INVISIBLE);
                } else if (response > 0) {
                    resposta = Assets.ACCOUNT_CREATED;
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    resposta = Assets.FATAL_ERROR;
                    btnRegister.setVisibility(View.VISIBLE);
                    progressReg.setVisibility(View.INVISIBLE);
                }

                Toast.makeText(getApplicationContext(), resposta, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(Assets.LOG_ERRO, Assets.LOG_ERRO + error.toString(), error);
                Toast.makeText(getApplicationContext(), Assets.NO_CONNECTION, Toast.LENGTH_SHORT).show();

            }
        });

        jsonReq.setTag(LoginActivity.class);
        mQueue.add(jsonReq);

    }

    @Override
    public void onStop() {
        super.onStop();
        mQueue.cancelAll("tag");
    }
}
