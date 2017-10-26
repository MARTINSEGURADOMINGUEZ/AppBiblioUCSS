package com.example.ceups.appprueba;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceups.appprueba.Fragments.BuscarFragment;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DetalleLibro extends AppCompatActivity {

    SmartImageView smartImageView;
    Toolbar toolbar ;
    TextView lblCodigo,lblNombre,lblDescripcion,lblAutor,lblCategoria,lblContenido,lblPaginas,lblFechaPublicacion,lblUbigeo;
    FloatingActionButton btnReservar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                {
                    onBackPressed();

                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        smartImageView = (SmartImageView) findViewById(R.id.smrtVwCollapse);
        btnReservar = (FloatingActionButton) findViewById(R.id.btnReservar);
        lblCodigo = (TextView) findViewById(R.id.lblCodigo2);
        lblNombre = (TextView) findViewById(R.id.lblNombre2);
        lblAutor = (TextView) findViewById(R.id.lblAutor2);
        lblCategoria = (TextView) findViewById(R.id.lblCategoria2);
        lblDescripcion = (TextView) findViewById(R.id.lblDescripcion2);
        lblFechaPublicacion = (TextView) findViewById(R.id.lblFechaPublicacion2);
        lblContenido = (TextView) findViewById(R.id.lblContenido2);
        lblUbigeo = (TextView) findViewById(R.id.lblUbigeo2);

        String urlFinal="http://webinfovic.com/WSBiblioUCSS/imagenes/libroPortada/"+getIntent().getStringExtra("portada");
        Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());
        smartImageView.setImageUrl(urlFinal,rect);
        toolbar.setTitle("DETALLE DEL LIBRO");

        lblCodigo.setText("CODIGO: "+getIntent().getStringExtra("codigo"));
        lblNombre.setText("NOMBRE: "+getIntent().getStringExtra("nombre"));
        lblAutor.setText("AUTOR: "+getIntent().getStringExtra("autor"));
        lblCategoria.setText("CATEGORIA: "+getIntent().getStringExtra("categoria"));
        lblDescripcion.setText("DESCRIPCIÓN: "+getIntent().getStringExtra("descripcion"));
        if(getIntent().getStringExtra("fechapublic").contains("0000"))
        {
            lblFechaPublicacion.setText("Fecha Publicacion: No registrada");
        }else {
            lblFechaPublicacion.setText("Fecha Publicacion:" + getIntent().getStringExtra("fechapublic"));
        }

        if(getIntent().getStringExtra("contenido").equals(""))
        {
            lblContenido.setText("Contenido: No Disponible");
        }else {
            lblContenido.setText("Contenido: "+getIntent().getStringExtra("contenido"));
        }
        lblUbigeo.setText("UBICACION: "+getIntent().getStringExtra("ubigeo"));
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void MensajeResponseOK(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
    public void MensajeResponseFAILED(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
    public void MensajeResponseINFO(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    public void AlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reserva WebInfov");
        builder.setMessage("Desea reservar el libro "+getIntent().getStringExtra("nombre")+"?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 RealizarReserva(ObtenerFechayHora(),getIntent().getStringExtra("codigo"),ObtenerCODIGOAlumno(),ObtenerIDAlumno(),getIntent().getStringExtra("id"));
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String ObtenerIDAlumno()
    {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String id = preferences.getString("idAlumno","null");
        return id;
    }
    public String ObtenerCODIGOAlumno()
    {
        SharedPreferences preferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        String codigo = preferences.getString("codigoAlumno","null");
        return codigo;
    }

    public String ObtenerFechayHora()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        return fecha;
    }

    public void RealizarReserva(String fechaHoraReserva,String codigoLibro,String codigoAlumno,String alumnoID, String libroID)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/ReservaControlador.php?op=1";

        RequestParams params = new RequestParams();
        params.add("codigo",codigoLibro+codigoAlumno+fechaHoraReserva);
        params.add("fechaemi",fechaHoraReserva);
        params.add("codalum",alumnoID);
        params.add("codlib",libroID);

        RequestHandle post = client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200)
                {
                    try {
                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        String estado = jsonArray.getJSONObject(0).getString("estado");

                        if(estado.equals("success"))
                        {
                            MensajeResponseOK("SE REALIZO LA RESERVA");
                        }else if(estado.contains("failed"))
                        {
                            MensajeResponseFAILED("RESERVA NO REALIZADA");
                        }else
                        {
                            MensajeResponseINFO("ALGO LOCO SUCEDE HARRY :V");
                        }

                    }catch (Exception ex){
                        MensajeResponseINFO(ex.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MensajeResponseFAILED("CODIGO: "+statusCode);            }
        });
    }

}
