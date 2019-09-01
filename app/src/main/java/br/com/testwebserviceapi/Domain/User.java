package br.com.testwebserviceapi.Domain;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    private Integer id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String descricao;
    private String image;
    private String data_nascimento;
    private String sexo;
    private String tipo;

public User(){
    super();
}

    public User(Integer id, String nome, String sobrenome, String email, String senha, String descricao, String image, String data_nascimento, String sexo, String tipo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.descricao = descricao;
        this.image = image;
        this.data_nascimento = data_nascimento;
        this.sexo = sexo;
        this.tipo = tipo;
    }

    /*public User(JSONObject data) {

        try {
            id = getId(data.getInt("id"));
            email = getEmail(data.getString("email"));
            senha = getSenha(data.getString("senha"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/


    public Integer getId(int id) {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail(String email) {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha(String senha) {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
