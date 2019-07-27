package com.markov;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Testamento extends AppCompatActivity {
    private ArrayList<User> informacion = new ArrayList<>();
    private TextView textarea, lblInfo;
    private String Resultado = "";
    private String FILE_NAME = "testamento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testamento);
        this.textarea = (TextView)findViewById(R.id.txtTestamente);
        this.lblInfo = findViewById(R.id.lblInfo);
        this.textarea.setTextSize(20);
        this.textarea.setTextColor(Color.rgb(0xff, 0, 0));
        this.textarea.setTypeface(Typeface.SERIF, Typeface.ITALIC);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            informacion = (ArrayList<User>) bundle.getSerializable("test");
            for(User item:informacion){
                lblInfo.setText("Testamento de "+item.getFirstName() + " "+item.getLastName());
            }

        }
        findViewById(R.id.btnSalirTestamento).setOnClickListener(this::onSalir);
        findViewById(R.id.btnGuardarTestamento).setOnClickListener(this::onGuardar);
        findViewById(R.id.btnLeerTestamento).setOnClickListener(this::onLeer);

        File fichero = new File(FILE_NAME);
        if (fichero.exists())
            System.out.println("El fichero " + FILE_NAME + " existe");
        else
            System.out.println("Pues va a ser que no");

    }
    public void onSalir(View view){
        finish();
    }
    public void onGuardar(View view){
        String cadena = textarea.getText().toString();
        FileOutputStream fos = null;
        try{
            fos =openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(cadena.getBytes());
            fos.close();
            Toast toast=Toast.makeText(getApplicationContext(),"Guardando..."+Resultado,Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
            toast.show();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void onLeer(View view){

        FileInputStream fis = null;
        try{
            fis = openFileInput(FILE_NAME);
            Resultado = getfileContent(fis,"UTF-8");
            this.textarea.setText(Resultado);
            Toast toast=Toast.makeText(getApplicationContext(),"Leendo..."+Resultado,Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
            toast.show();
            fis.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getfileContent(FileInputStream fis,String encoding) throws IOException{
        try(BufferedReader br = new BufferedReader(new InputStreamReader(fis,encoding))){
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
                sb.append('\n');
            }
            return  sb.toString();
        }

    }
}
