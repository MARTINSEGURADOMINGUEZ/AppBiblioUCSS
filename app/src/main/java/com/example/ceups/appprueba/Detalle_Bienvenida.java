package com.example.ceups.appprueba;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;

public class Detalle_Bienvenida extends AppCompatActivity {

    TextView lblFecha, lblTitulo, lblDescripcion, lblTexto, lblUbigeo;
    SmartImageView smartImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__bienvenida);

        smartImageView = (SmartImageView) findViewById(R.id.smrtVwCollapseNoticia);
        lblFecha = (TextView) findViewById(R.id.lblFechaNoticia);
        lblTitulo = (TextView) findViewById(R.id.lblTituloNoticia);
        lblDescripcion = (TextView) findViewById(R.id.lblDescripcionNoticia);
        lblTexto = (TextView) findViewById(R.id.lblTextoNoticia);
        lblUbigeo = (TextView) findViewById(R.id.lblUbigeoNoticia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNoticia);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String urlFinal = "http://webinfovic.com/WSBiblioUCSS/imagenes/noticiaPortada/" + getIntent().getStringExtra("portada").toString();
        Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
        smartImageView.setImageUrl(urlFinal, rect);

        toolbar.setTitle("DETALLE NOTICIA");

        lblFecha.setText("FECHA: " + getIntent().getStringExtra("fecha").toString());
        lblTitulo.setText("TITULO: " + getIntent().getStringExtra("titulo").toString());
        lblDescripcion.setText("RESUMEN: " + getIntent().getStringExtra("descripcion").toString());
        lblTexto.setText("CONTENIDO: " + getIntent().getStringExtra("texto").toString());
        lblUbigeo.setText("UBICACIÓN: " + getIntent().getStringExtra("ubigeo").toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
