package com.example.ceups.appprueba.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ceups.appprueba.R;
import com.github.snowdream.android.widget.SmartImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EstudianteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EstudianteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstudianteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView lblNombre, lblCarrera,lblCorreo,lblCiclo, lblEdad;
    SmartImageView smartImageView;

    public EstudianteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstudianteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstudianteFragment newInstance(String param1, String param2) {
        EstudianteFragment fragment = new EstudianteFragment();
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

        View vista = inflater.inflate(R.layout.fragment_estudiante, container, false);

        lblNombre = (TextView)vista.findViewById(R.id.lblNombre);
        lblCarrera = (TextView) vista.findViewById(R.id.lblCarrera);
        lblCorreo = (TextView) vista.findViewById(R.id.lblCorreo);
        lblEdad = (TextView) vista.findViewById(R.id.lblEdad);
        lblCiclo = (TextView) vista.findViewById(R.id.lblCiclo);
        smartImageView = (SmartImageView)vista.findViewById(R.id.smrtVw);

        obtenerDatosALumno();

        return vista;
    }

    public void obtenerDatosALumno()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);

        String codigo = preferences.getString("codigoAlumno","null");
        String nombre = preferences.getString("nombreAlumno","null");
        String edad = preferences.getString("edadAlumno","null");
        String dni = preferences.getString("dniAlumno","null");
        String foto = preferences.getString("fotoAlumno","null");
        String email = preferences.getString("emailAlumno","null");
        String carrera = preferences.getString("carreraAlumno","null");
        String facultad = preferences.getString("facultadAlumno","null");
        String ciclo = preferences.getString("cicloAlumno","null");

        String urlFinal="http://webinfovic.com/WSBiblioUCSS/imagenes/alumnoFoto/"+foto;
        Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());

        smartImageView.setImageUrl(urlFinal,rect);

        lblNombre.setText("Nombre: \n"+nombre);
        lblCarrera.setText("Carrera: \n"+carrera);
        lblCorreo.setText("Correo Electronico: \n"+email);
        lblEdad.setText("Edad: \n"+edad);
        lblCiclo.setText("Ciclo Academico: \n"+ciclo);

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
}
