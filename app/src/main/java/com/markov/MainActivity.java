package com.markov;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.markov.Constantes.registro;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText txtPasword, txtUsuario;
    private CheckBox estado;
    private static final String FREFS_NAME = "PreferenciasUsuario";
    private static final String TAG_ESTADO = "recordar";
    private static final String TAG_USUARIO = "usuario";
    private static final String TAG_CLAVE = "clave";
    private SharedPreferences sharedPreferences;
    private Boolean creado = false;
    private  User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnLogin = (Button) findViewById(R.id.btnIngresar);
        this.txtPasword = (EditText) findViewById(R.id.txtClave);
        this.txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        this.estado = (CheckBox) findViewById(R.id.check_recordad);
        this.btnLogin.setOnClickListener(v -> login());
        this.sharedPreferences = getSharedPreferences(FREFS_NAME, Context.MODE_PRIVATE);
        this.accesosDato();

    }
    private void accesosDato() {
        if(sharedPreferences !=null){
            if (contains(TAG_ESTADO)) {
                this.creado = sharedPreferences.getBoolean(TAG_ESTADO, false);
            }
            this.estado.setChecked(creado);
            this.txtUsuario.setText(sharedPreferences.getString(TAG_USUARIO,""));
            this.txtPasword.setText(sharedPreferences.getString(TAG_CLAVE,""));
            if(contains(TAG_USUARIO) & contains(TAG_CLAVE)){
                if(creado){
                    login();
                }
            }
        }
    }
    private void login(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.43.246/login.php";
        //String url = "http://192.168.1.130/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            System.out.println("Info: "+response);
            if(!response.trim().isEmpty() & isJSONValid(response)){
                try {
                    JSONObject json = new JSONObject(response);
                    user = new User();
                    user.setId( json.getInt("id") );
                    user.setUserName( json.getString("userName") );
                    user.setUserPassword( json.getString("userPassword") );
                    user.setFirstName( json.getString("firstName") );
                    user.setLastName( json.getString("lastName") );
                    user.setUserAge( json.getInt("userAge") );
                    registro.add(user);
                } catch (JSONException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                boolean valor = estado.isChecked();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(TAG_ESTADO, valor);
                editor.putString(TAG_USUARIO, !valor ? "" : user.getUserName() );
                editor.putString(TAG_CLAVE, !valor ? "" : user.getUserPassword());
                editor.apply();
                Intent i= new Intent(MainActivity.this,MarcacionActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("test",registro);
                i.putExtras(bundle);
                startActivity(i);
                Toast toast=Toast.makeText(getApplicationContext(),"Acceso Permitido.",Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();
            }else{
                Toast toast=Toast.makeText(getApplicationContext(),"Acceso Denegado.",Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();
            }
        }, error -> {
            Toast toast=Toast.makeText(getApplicationContext(),"Error: "+error.getMessage(),Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
            toast.show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("userName",txtUsuario.getText().toString().trim());
                params.put("userPassword",txtPasword.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    public boolean contains (String key) {
        return sharedPreferences.contains(key);
    }
}
