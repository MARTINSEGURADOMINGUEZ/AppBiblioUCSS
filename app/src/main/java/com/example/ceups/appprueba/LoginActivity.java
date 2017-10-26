package com.example.ceups.appprueba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsuario,edtPassword;
    Button btnLogin;

    ArrayList estado = new ArrayList();
    ArrayList codigoid = new ArrayList();
    ArrayList codigo = new ArrayList();
    ArrayList nombre = new ArrayList();
    ArrayList edad = new ArrayList();
    ArrayList email = new ArrayList();
    ArrayList dni = new ArrayList();
    ArrayList carrera = new ArrayList();
    ArrayList facultad = new ArrayList();
    ArrayList foto = new ArrayList();
    ArrayList ciclo = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnIngresar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getUsuario().equals("")|| getPassword().equals(""))
                {
                    MensajeAlerta();
                }else
                    {
                        AccesoPrincipal(getUsuario(),getPassword());
                    }

            }
        });

    }
    public void MensajeAlerta()
    {
        Crouton.makeText(this,"CREDENCIALES VACIAS",Style.ALERT).show();
    }
    public String getUsuario()
    {
        return edtUsuario.getText().toString();
    }
    public String getPassword()
    {
        return edtPassword.getText().toString();
    }

    public void CredencialesIncorrectas()
    {
        Crouton.makeText(this,"CREDENCIALES INCORRECTAS",Style.ALERT).show();
    }
    public void CredencialesCorrectas()
    {
        Crouton.makeText(this,"CREDENCIALES CORRECTAS",Style.CONFIRM).show();
    }
    public void ProblemasTecnicos()
    {
        Crouton.makeText(this,"PROBLEMAS DE RED",Style.INFO).show();
    }

    public void AccesoPrincipal(String usuario, String password)
    {
        estado.clear();

        AsyncHttpClient client = new AsyncHttpClient();

        String url = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/AlumnoControlador.php?op=1";

        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("Cargando Datos...");
        progress.show();

        RequestParams params = new RequestParams();
        params.add("Usuario",usuario);
        params.add("Password",password);

        RequestHandle post = client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode == 200)
                    {
                        progress.cancel();
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));

                            for(int i=0; i<jsonArray.length();i++){

                                estado.add(jsonArray.getJSONObject(i).getString("estado"));
                                codigoid.add(jsonArray.getJSONObject(i).getString("id"));
                                codigo.add(jsonArray.getJSONObject(i).getString("codigo"));
                                nombre.add(jsonArray.getJSONObject(i).getString("nombre"));
                                edad.add(jsonArray.getJSONObject(i).getString("edad"));
                                dni.add(jsonArray.getJSONObject(i).getString("dni"));
                                email.add(jsonArray.getJSONObject(i).getString("email"));
                                carrera.add(jsonArray.getJSONObject(i).getString("carrera"));
                                facultad.add(jsonArray.getJSONObject(i).getString("facultad"));
                                foto.add(jsonArray.getJSONObject(i).getString("foto"));
                                ciclo.add(jsonArray.getJSONObject(i).getString("ciclo"));

                            }

                            String response = estado.get(0).toString();

                            if(response.contains("success"))
                            {
                                    //CredencialesCorrectas();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("idAlumno",codigoid.get(0).toString());
                                editor.putString("codigoAlumno",codigo.get(0).toString());
                                editor.putString("nombreAlumno",nombre.get(0).toString());
                                editor.putString("edadAlumno",edad.get(0).toString());
                                editor.putString("dniAlumno",dni.get(0).toString());
                                editor.putString("emailAlumno",email.get(0).toString());
                                editor.putString("carreraAlumno",carrera.get(0).toString());
                                editor.putString("facultadlumno",facultad.get(0).toString());
                                editor.putString("fotoAlumno",foto.get(0).toString());
                                editor.putString("cicloAlumno",ciclo.get(0).toString());

                                editor.commit();

                                        startActivity(intent);
                                        progress.dismiss();

                            }else if(response.contains("failed"))
                            {
                                CredencialesIncorrectas();
                            }else
                                {
                                    ProblemasTecnicos();
                                }

                        }catch (Exception ex)
                        {
                            //Toast.makeText(getApplicationContext(), ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                            CredencialesIncorrectas();
                        }
                    }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

}
