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
    private String img;
    private String data;
    private String sexo;
    private Integer tipo;


    public User(){
    super();
}

    /**public User(Integer id, String nome, String sobrenome, String email, String senha, String descricao, String image, String data_nascimento, String sexo, String tipo) {
        super();
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
    }**/

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }

        nome = in.readString();
        sobrenome = in.readString();
        email = in.readString();
        senha = in.readString();
        descricao = in.readString();
        img = in.readString();
        data = in.readString();
        sexo = in.readString();
        tipo = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public Integer getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte)0);
        } else {
            parcel.writeByte((byte)1);
            parcel.writeInt(id);
        }

        parcel.writeString(nome);
        parcel.writeString(sobrenome);
        parcel.writeString(email);
        parcel.writeString(senha);
        parcel.writeString(descricao);
        parcel.writeString(img);
        parcel.writeString(data);
        parcel.writeString(sexo);
        parcel.writeInt(tipo);

    }
}
