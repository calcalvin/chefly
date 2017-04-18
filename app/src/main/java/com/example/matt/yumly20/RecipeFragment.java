package com.example.matt.yumly20;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String component = "Ingredients";
    private IngredientAdapter iAdapter;

    private List ingredients = new ArrayList();

    private OnFragmentInteractionListener mListener;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_recipe, container, false);

        final ImageView image = (ImageView) view.findViewById(R.id.recipe_image);
        image.setImageBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.everydaybakedchicken));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final Button ingredientsButton = (Button) view.findViewById(R.id.ingredients_button);
        final Button directionsButton = (Button) view.findViewById(R.id.directions_button);
        final Button nutritionButton = (Button) view.findViewById(R.id.nutrition_button);

        ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.colorAccent));
        directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));
        nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));

        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorAccent));
                directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

            }
        });
        directionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorAccent));
                ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
            }
        });
        nutritionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorAccent));
                directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapters();
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

    private void setAdapters() {

        ListView iView = (ListView) getActivity().findViewById(R.id.ingredients_component);

        if (component.equals("Ingredients")) {

            if (ingredients == null || ingredients.size() == 0) {
                populateIngredients();
            }
            iAdapter = new IngredientAdapter(getActivity(), R.layout.ingredients_item, ingredients);
            iView.setAdapter(iAdapter);

        } else if (component.equals("Directions")) {

        }
    }

    private void populateIngredients() {
        ingredients = new ArrayList();
        ingredients.add(new Ingredient("4", "chicken breasts"));
        ingredients.add(new Ingredient("1 teaspoon", "kosher salt"));
    }
}