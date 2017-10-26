package com.example.ceups.appprueba.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceups.appprueba.R;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class HistorialFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList codigolibro = new ArrayList();
    ArrayList nombre = new ArrayList();
    ArrayList descripcion = new ArrayList();
    ArrayList portada = new ArrayList();
    ArrayList linea = new ArrayList();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ListView listRsv;

    public HistorialFragment() {

    }

    public static HistorialFragment newInstance(String param1, String param2) {
        HistorialFragment fragment = new HistorialFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        listRsv = (ListView) view.findViewById(R.id.LSTVWHSTRL);

        CargarDatos(ObtenerCODIGOAlumno());

        return view;
    }

    public String ObtenerCODIGOAlumno() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String codigo = preferences.getString("codigoAlumno", "null");
        return codigo;
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

    public void CargarDatos(String codigoAlumno) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("codialum", codigoAlumno);

        String url = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/ReservaControlador.php?op=3";

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("MiS LIBROS...");
        progress.show();

        RequestHandle post = client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (statusCode == 200) {
                    try {

                        progress.dismiss();

                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            codigolibro.add(jsonArray.getJSONObject(i).getString("CODILIBRO"));
                            nombre.add(jsonArray.getJSONObject(i).getString("NOMBRE"));
                            descripcion.add(jsonArray.getJSONObject(i).getString("DECRIPCION"));
                            portada.add(jsonArray.getJSONObject(i).getString("PORTADA"));

                            linea.add(nombre + " " + descripcion + " " + portada);
                        }

                        if (linea.size() > 0) {

                            listRsv.setAdapter(new MiAdaptador(getContext(), linea.size()));

                        } else {
                            Toast.makeText(getContext(), "No hay Libros Registrados", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "ERROR: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "CODIGO: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MiAdaptador extends BaseAdapter {
        int tamano;
        Context context;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView txtCodigo, txtNombre, txtDescripcion;

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

            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.grilla_reserva, null);

            smartImageView = (SmartImageView) viewGroup.findViewById(R.id.imaReserva);
            txtCodigo = (TextView) viewGroup.findViewById(R.id.lblCodigoReserva);
            txtDescripcion = (TextView) viewGroup.findViewById(R.id.lblDescripcionReserva);
            txtNombre = (TextView) viewGroup.findViewById(R.id.lblDescripcionReserva);

            String urlFinal = "http://webinfovic.com/WSBiblioUCSS/imagenes/libroPortada/" + portada.get(position).toString();
            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
            smartImageView.setImageUrl(urlFinal, rect);

            txtCodigo.setText(codigolibro.get(position).toString());
            txtNombre.setText(nombre.get(position).toString());
            txtDescripcion.setText(descripcion.get(position).toString());

            return viewGroup;
        }
    }

}
