package com.example.ceups.appprueba.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceups.appprueba.DetalleLibro;
import com.example.ceups.appprueba.Detalle_Bienvenida;
import com.example.ceups.appprueba.R;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class BienvenidaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    ArrayList id = new ArrayList();
    ArrayList codigo = new ArrayList();
    ArrayList titulo = new ArrayList();
    ArrayList fecha = new ArrayList();
    ArrayList descripcion = new ArrayList();
    ArrayList texto = new ArrayList();
    ArrayList portada = new ArrayList();
    ArrayList ubigeo = new ArrayList();
    ArrayList linea = new ArrayList();
    TextView fechaTextview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public BienvenidaFragment() {
        // Required empty public constructor
    }

    public static BienvenidaFragment newInstance(String param1, String param2) {
        BienvenidaFragment fragment = new BienvenidaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_bienvenida, container, false);

        fechaTextview = (TextView) vista.findViewById(R.id.lblFecha);
        listView = (ListView) vista.findViewById(R.id.LSTVWNEWS);
        fechaTextview.setText("Fecha: " + ObtenerFecha());

        CargarNoticias();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                verDetalleNoticia(position);
            }
        });


        return vista;
    }

    public void verDetalleNoticia(int position) {
        Intent intent = new Intent(getContext(), Detalle_Bienvenida.class);
        intent.putExtra("id", id.get(position).toString());
        intent.putExtra("portada", portada.get(position).toString());
        intent.putExtra("codigo", codigo.get(position).toString());
        intent.putExtra("titulo", titulo.get(position).toString());
        intent.putExtra("descripcion", descripcion.get(position).toString());
        intent.putExtra("fecha", fecha.get(position).toString());
        intent.putExtra("texto", texto.get(position).toString());
        intent.putExtra("ubigeo", ubigeo.get(position).toString());
        startActivity(intent);
    }

    public String ObtenerFecha() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        return fecha;
    }

    public void CargarNoticias() {
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/NoticiaControlador.php?op=1";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {

                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            id.add(jsonArray.getJSONObject(i).getString("ID"));
                            codigo.add(jsonArray.getJSONObject(i).getString("CODIGO"));
                            titulo.add(jsonArray.getJSONObject(i).getString("TITULO"));
                            descripcion.add(jsonArray.getJSONObject(i).getString("DESCRIPCION"));
                            texto.add(jsonArray.getJSONObject(i).getString("TEXTO"));
                            fecha.add(jsonArray.getJSONObject(i).getString("FECHA"));
                            portada.add(jsonArray.getJSONObject(i).getString("PORTADA"));
                            ubigeo.add(jsonArray.getJSONObject(i).getString("UBIGEO"));
                            linea.add(id + "" + codigo + "" + titulo + "" + descripcion + "" + texto + "" + fecha + "" + portada + "" + ubigeo);

                        }

                        if (linea.size() > 0) {
                            listView.setAdapter(new MiAdaptador(getContext(), linea.size()));
                        } else {
                            Toast.makeText(getContext(), "No hay noticias", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error :" + statusCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MiAdaptador extends BaseAdapter {

        Context context;
        int tamano;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView txtNombre, txtDescripcion, txtUbigeo;

        public MiAdaptador(Context appContext, int linea) {
            this.context = appContext;
            this.tamano = linea;
            layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return tamano;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.grilla_bienvenida, null);

            smartImageView = (SmartImageView) viewGroup.findViewById(R.id.imaNoticia);
            txtNombre = (TextView) viewGroup.findViewById(R.id.lblTituloNoticia);
            txtDescripcion = (TextView) viewGroup.findViewById(R.id.lblDescripcionNoticia);
            txtUbigeo = (TextView) viewGroup.findViewById(R.id.lblUbigeoNoticia);

            String urlFinal = "http://webinfovic.com/WSBiblioUCSS/imagenes/noticiaPortada/" + portada.get(position).toString();
            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
            smartImageView.setImageUrl(urlFinal, rect);

            txtNombre.setText((titulo.get(position).toString()).toUpperCase());
            txtDescripcion.setText((descripcion.get(position).toString()));
            txtUbigeo.setText("Ubicacion: " + ubigeo.get(position).toString());

            return viewGroup;
        }
    }

}
