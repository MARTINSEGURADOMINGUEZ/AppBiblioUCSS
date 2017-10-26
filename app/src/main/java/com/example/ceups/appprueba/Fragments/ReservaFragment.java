package com.example.ceups.appprueba.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ReservaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList id = new ArrayList();
    ArrayList codigo = new ArrayList();
    ArrayList fecha = new ArrayList();
    ArrayList codigolibro = new ArrayList();
    ArrayList nombre = new ArrayList();
    ArrayList descripcion = new ArrayList();
    ArrayList portada = new ArrayList();
    ArrayList linea = new ArrayList();
    private ListView listRsv;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private String[] sistemas = {"Lista de Reservas Vacia..."};

    public ReservaFragment() {
        // Required empty public constructor
    }


    public static ReservaFragment newInstance(String param1, String param2) {
        ReservaFragment fragment = new ReservaFragment();
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

        View view = inflater.inflate(R.layout.fragment_reserva, container, false);

        listRsv = (ListView) view.findViewById(R.id.LSTVWRSV);

        CargarDatos(ObtenerCODIGOAlumno());

        registerForContextMenu(listRsv);

        return view;
    }

    public String ObtenerCODIGOAlumno() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String codigo = preferences.getString("codigoAlumno", "null");
        return codigo;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        int id = v.getId();
        MenuInflater inflater = this.getActivity().getMenuInflater();

        switch (id) {
            case R.id.LSTVWRSV:
                if(!linea.isEmpty())
                {
                    inflater.inflate(R.menu.menu_contextual, menu);
                }
                break;
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.action_delete_item:

                //Toast.makeText(getContext(), "ELEMENTO: " + info.position + " opcion de Menu: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                //ELiminarReserva(id.get(info.position).toString());
                //Toast.makeText(getContext(), "EL ID POR POSICION ES: "+id.get(info.position).toString(), Toast.LENGTH_SHORT).show();
                AlertDialog(nombre.get(info.position).toString(),id.get(info.position).toString());

                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }

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

    public void limpiarCampos() {
        id.clear();
        codigo.clear();
        fecha.clear();
        codigolibro.clear();
        nombre.clear();
        portada.clear();
        descripcion.clear();
        linea.clear();
    }

    public void AlertDialog(String Nombre , final String codigo)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reserva WebInfov");
        builder.setMessage("Desea Eliminar la  reserva "+Nombre+"?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ELiminarReserva(codigo);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void ELiminarReserva(String codigoReserva) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("idreserva", codigoReserva);

        String url = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/ReservaControlador.php?op=4";

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (statusCode == 200) {
                    try {

                        JSONArray array = new JSONArray(new String(responseBody));

                        String estado = array.getJSONObject(0).getString("estado");

                        if (estado.contains("success")) {
                            Toast.makeText(getContext(), "SU RESERVA ACABA DE SER CANCELADA.", Toast.LENGTH_LONG).show();
                            CargarDatos(ObtenerCODIGOAlumno());
                        } else if (estado.contains("failed")) {
                            Toast.makeText(getContext(), "ERROR AL CANCELAR , COMUNIQUESE CON EL ADMINISTRADOR.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "TENEMOS UN PROBLEMA HARRY :V", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "ERROR: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "ERROR CODIGO: " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void CargarDatos(String codigoAlumno) {
        limpiarCampos();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/ReservaControlador.php?op=2";

        RequestParams params = new RequestParams();
        params.add("codialum", codigoAlumno);

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Obteniendo Reservas...");
        progress.show();
        RequestHandle post = client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        progress.dismiss();
                        JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            id.add(jsonArray.getJSONObject(i).getString("ID"));
                            codigo.add(jsonArray.getJSONObject(i).getString("CODIGO"));
                            fecha.add(jsonArray.getJSONObject(i).getString("FECHA"));
                            codigolibro.add(jsonArray.getJSONObject(i).getString("CODILIBRO"));
                            nombre.add(jsonArray.getJSONObject(i).getString("NOMBRE"));
                            descripcion.add(jsonArray.getJSONObject(i).getString("DECRIPCION"));
                            portada.add(jsonArray.getJSONObject(i).getString("PORTADA"));
                            linea.add(codigo + " " + nombre + " " + descripcion + " " + portada);
                        }
                        if (linea.size() > 0) {
                            listRsv.setAdapter(new MiAdaptador(getContext(), linea.size()));
                        } else {

                            Toast.makeText(getContext(), "No hay Reservas", Toast.LENGTH_SHORT).show();
                            linea.clear();
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sistemas);
                            listRsv.setAdapter(adaptador);
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
