package com.example.ceups.appprueba.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceups.appprueba.DetalleLibro;
import com.example.ceups.appprueba.LoginActivity;
import com.example.ceups.appprueba.MainActivity;
import com.example.ceups.appprueba.R;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuscarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText edtCriterio;
    Spinner spnCriterio;
    Button btnConsultarLibro;
    ExpandableListView expandableListView;
    String URL = "";
    ArrayList codigoid = new ArrayList();
    ArrayList codigo = new ArrayList();
    ArrayList nombre = new ArrayList();
    ArrayList descripcion = new ArrayList();
    ArrayList imagen = new ArrayList();
    ArrayList stock = new ArrayList();
    ArrayList autor = new ArrayList();
    ArrayList categoria = new ArrayList();
    ArrayList ubigeo = new ArrayList();
    ArrayList contenido = new ArrayList();
    ArrayList fechapublic = new ArrayList();
    ArrayList linea = new ArrayList();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public BuscarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BuscarFragment newInstance(String param1, String param2) {
        BuscarFragment fragment = new BuscarFragment();
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
        View vista = inflater.inflate(R.layout.fragment_buscar, container, false);

        edtCriterio = (EditText) vista.findViewById(R.id.edtCriterio);
        spnCriterio = (Spinner) vista.findViewById(R.id.spnCriterio);
        btnConsultarLibro = (Button) vista.findViewById(R.id.btnConsultarLibro);
        expandableListView = (ExpandableListView) vista.findViewById(R.id.LstVwExpan);

        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getContext(), R.array.criterio , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCriterio.setAdapter(spinner_adapter);

        spnCriterio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {
                    edtCriterio.setHint("Busqueda por Nombre");
                    URL = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/LibroControlador.php?op=2";

                }else if(position == 1)
                {
                    edtCriterio.setHint("Busqueda por Autor");
                    URL="http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/LibroControlador.php?op=3";


                }else if(position == 2)
                {
                    edtCriterio.setHint("Busqueda por Categoria");
                    URL="http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/LibroControlador.php?op=4";
                }else
                    {
                        edtCriterio.setHint("Busqueda por Cualquier Cosa");
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                edtCriterio.setHint("Busqueda por Nombre");
                URL="http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/LibroControlador.php?op=2";
            }
        });

        CargarListaLibros();

        btnConsultarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarXCriterio(ObtenerCriterio(),URL);
            }
        });

        return vista;
    }

    /////public void MensajePrueba(String mensaje) {Toast.makeText(getContext(), "BUSCAR X "+mensaje, Toast.LENGTH_SHORT).show();}

    public void BuscarXCriterio(String criterio,String URL)
    {
        codigoid.clear();
        codigo.clear();
        nombre.clear();
        descripcion.clear();
        imagen.clear();
        stock.clear();
        fechapublic.clear();
        autor.clear();
        categoria.clear();
        ubigeo.clear();
        linea.clear();

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("criterio",criterio);


        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Buscando Libros...");
        progress.show();

        RequestHandle post = client.post(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200)
                {
                    //Toast.makeText(getContext(), new String(responseBody) , Toast.LENGTH_LONG).show();
                    progress.cancel();
                    try {

                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            codigoid.add(jsonArray.getJSONObject(i).getString("ID"));
                            codigo.add(jsonArray.getJSONObject(i).getString("CODIGO"));
                            nombre.add(jsonArray.getJSONObject(i).getString("NOMBRE"));
                            descripcion.add(jsonArray.getJSONObject(i).getString("DESCRIPCION"));
                            imagen.add(jsonArray.getJSONObject(i).getString("PORTADA"));
                            stock.add(jsonArray.getJSONObject(i).getString("STOCK"));
                            contenido.add(jsonArray.getJSONObject(i).getString("CONTENIDO"));
                            fechapublic.add(jsonArray.getJSONObject(i).getString("FECHA"));
                            autor.add(jsonArray.getJSONObject(i).getString("autor"));
                            categoria.add(jsonArray.getJSONObject(i).getString("categoria"));
                            ubigeo.add(jsonArray.getJSONObject(i).getString("ubigeo"));
                            linea.add(codigo+" "+nombre+" "+descripcion+" "+stock);
                        }

                        if(linea.size()>0){

                            expandableListView.setAdapter(new MiAdaptador(getContext(), linea.size()));

                        }else{

                            Toast.makeText(getContext(),"No hay resultados",Toast.LENGTH_SHORT).show();
                            CargarListaLibros();
                        }

                    }catch (Exception ex)
                    {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "OCURRIO UN PROBLEMA", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public String ObtenerCriterio() {return edtCriterio.getText().toString();}

    public void CargarListaLibros()
    {
        /*codigoid.clear();
        codigo.clear();
        nombre.clear();
        descripcion.clear();
        imagen.clear();
        stock.clear();
        fechapublic.clear();
        contenido.clear();
        autor.clear();
        categoria.clear();
        ubigeo.clear();*/

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://webinfovic.com/WSBiblioUCSS/CONTROLADOR/LibroControlador.php?op=1";
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Obteniendo Registros...");
        progress.show();

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if(statusCode == 200)
                {
                    progress.dismiss();
                    try {

                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            codigoid.add(jsonArray.getJSONObject(i).getString("ID"));
                            codigo.add(jsonArray.getJSONObject(i).getString("CODIGO"));
                            nombre.add(jsonArray.getJSONObject(i).getString("NOMBRE"));
                            descripcion.add(jsonArray.getJSONObject(i).getString("DESCRIPCION"));
                            imagen.add(jsonArray.getJSONObject(i).getString("PORTADA"));
                            stock.add(jsonArray.getJSONObject(i).getString("STOCK"));
                            contenido.add(jsonArray.getJSONObject(i).getString("CONTENIDO"));
                            fechapublic.add(jsonArray.getJSONObject(i).getString("FECHA"));
                            autor.add(jsonArray.getJSONObject(i).getString("autor"));
                            categoria.add(jsonArray.getJSONObject(i).getString("categoria"));
                            ubigeo.add(jsonArray.getJSONObject(i).getString("ubigeo"));
                            linea.add(codigo+" "+nombre+" "+descripcion+" "+stock);
                        }

                        if(linea.size()>0){

                            expandableListView.setAdapter(new MiAdaptador(getContext(), linea.size()));

                        }else{

                            Toast.makeText(getContext(),"No hay datos",Toast.LENGTH_SHORT).show();

                        }

                    }catch (Exception ex)
                    {
                        Toast.makeText(getContext(), "->"+ex.getMessage(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "=>"+new String(responseBody), Toast.LENGTH_LONG).show();
                    }

                    //Toast.makeText(getContext(), "=>"+new String(responseBody), Toast.LENGTH_SHORT).show();

                }else
                    {
                        Toast.makeText(getContext(), "PROBLEMA DE INTERNET", Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(),"NO OCURRE NADA"+String.valueOf(statusCode),Toast.LENGTH_SHORT).show();

            }
        });
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

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Realizando Reserva...");
        progress.show();

        RequestHandle post = client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode == 200)
                    {
                        progress.dismiss();
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

    public String ObtenerFechayHora()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        return fecha;
    }

    public void MensajeResponseOK(String mensaje)
    {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }
    public void MensajeResponseFAILED(String mensaje)
    {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }
    public void MensajeResponseINFO(String mensaje)
    {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    public void verDetalleLibro(int position)
    {
        Intent intent = new Intent(getContext(),DetalleLibro.class);
        intent.putExtra("id",codigoid.get(position).toString());
        intent.putExtra("portada",imagen.get(position).toString());
        intent.putExtra("codigo",codigo.get(position).toString());
        intent.putExtra("nombre",nombre.get(position).toString());
        intent.putExtra("descripcion",descripcion.get(position).toString());
        intent.putExtra("contenido",contenido.get(position).toString());
        intent.putExtra("fechapublic",fechapublic.get(position).toString());
        intent.putExtra("autor",autor.get(position).toString());
        intent.putExtra("categoria",categoria.get(position).toString());
        intent.putExtra("ubigeo",ubigeo.get(position).toString());
        //intent.putExtra("stock",stock.get(position).toString());
        startActivity(intent);
    }

    public String ObtenerIDAlumno()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        String id = preferences.getString("idAlumno","null");
        return id;
    }
    public String ObtenerCODIGOAlumno()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        String codigo = preferences.getString("codigoAlumno","null");
        return codigo;
    }

    public void MensajeReservaDialog(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reserva WebInfov");
        builder.setMessage("Desea reservar el libro "+nombre.get(position).toString()+"?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getContext(),ObtenerFechayHora(), Toast.LENGTH_SHORT).show();
                RealizarReserva(ObtenerFechayHora(),codigo.get(position).toString(),ObtenerCODIGOAlumno(),ObtenerIDAlumno(),codigoid.get(position).toString());
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MiAdaptador extends BaseExpandableListAdapter
    {
        Context context;
        LayoutInflater layoutInflater;
        int tamaño;
        SmartImageView smartImageView;
        TextView lblNombre,lblAutor,lblUbigeo;
        Button btnInformacion , btnReservar;

        public MiAdaptador(Context ctx, int linea) {
            this.tamaño = linea;
            this.context = ctx;
            layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount() {
            return tamaño;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return tamaño;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.grilla_busqueda,null);

            lblNombre = (TextView) viewGroup.findViewById(R.id.lblNombre);
            lblAutor = (TextView) viewGroup.findViewById(R.id.lblAutor);
            lblUbigeo = (TextView) viewGroup.findViewById(R.id.lblUbigeo);
            smartImageView = (SmartImageView) viewGroup.findViewById(R.id.imaLibro);

            if((imagen.get(groupPosition).toString()).contains("nodisponible"))
            {
                String urlFinal="http://webinfovic.com/WSBiblioUCSS/imagenes/libroPortada/nodisponible.png";
                Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());
                smartImageView.setImageUrl(urlFinal,rect);
            }else
                {
                    String urlFinal="http://webinfovic.com/WSBiblioUCSS/imagenes/libroPortada/"+imagen.get(groupPosition).toString();
                    Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());
                    smartImageView.setImageUrl(urlFinal,rect);
                }


            //lblCodigo.setText(codigo.get(groupPosition).toString());
            lblNombre.setText("Nombre: "+nombre.get(groupPosition).toString());
            lblAutor.setText("Autor: "+autor.get(groupPosition).toString());
            lblUbigeo.setText("Ubicación: "+ubigeo.get(groupPosition).toString());
            return viewGroup;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.grilla_busqueda_botones,null);

                btnInformacion = (Button) viewGroup.findViewById(R.id.btnInfo);
                btnReservar = (Button) viewGroup.findViewById(R.id.btnReserva);

                btnInformacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, "posicion grupo:"+groupPosition+" / "+"posicion hijo:"+childPosition+" / "+"codigo grupo:"+imagen.get(groupPosition).toString(), Toast.LENGTH_SHORT).show();
                        verDetalleLibro(groupPosition);
                    }
                });

                btnReservar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MensajeReservaDialog(groupPosition);
                    }
                });

            return viewGroup;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

}
