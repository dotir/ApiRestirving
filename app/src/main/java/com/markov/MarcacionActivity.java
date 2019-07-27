package com.markov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.markov.Constantes.registro;

public class MarcacionActivity extends AppCompatActivity {
    ArrayList<User> informacion = new ArrayList<>();
    TextView txtNombre,txtApellidos,txtEdad,txtPassword;
    ImageView imageView;
    String user ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcacion);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            informacion = (ArrayList<User>) bundle.getSerializable("test");
            this.txtNombre = (TextView) findViewById(R.id.txtNombre);
            this.txtApellidos = (TextView) findViewById(R.id.txtApellidos);
            this.txtEdad = (TextView) findViewById(R.id.txtEdad);
            this.txtPassword = (TextView) findViewById(R.id.txtPassword);
            this.imageView = (ImageView) findViewById(R.id.imageView2);
            CheckBox satView = (CheckBox)findViewById(R.id.checkBox);
            for(User item:informacion){
                txtNombre.setText(item.getFirstName());
                txtApellidos.setText(item.getLastName());
                txtEdad.setText(item.getUserAge()+"");
                txtPassword.setText(item.getUserPassword());
                user = item.getUserName();
            }
            if(user.equals("admin")){
                imageView.setImageResource(R.mipmap.woman);
            }
            if(user.equals("root")){
                imageView.setImageResource(R.mipmap.man);
            }



        }
        findViewById(R.id.btnSalirTestamento).setOnClickListener(this::onSalir);
        findViewById(R.id.checkBox).setOnClickListener(this::onCheck);
        findViewById(R.id.btnTestamento).setOnClickListener(this::onTestamento);

    }
    public void onSalir(View view){
        finish();
    }
    public void onTestamento(View view){
        Intent i= new Intent(MarcacionActivity.this,Testamento.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("test",registro);
        i.putExtras(bundle);
        startActivity(i);

    }
    public void onCheck(View view){
        if(((CompoundButton) view).isChecked()){
            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }
}
