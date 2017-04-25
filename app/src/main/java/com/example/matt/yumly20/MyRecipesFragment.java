package com.example.matt.yumly20;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyRecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String APP_ID = "c99817f9";
    private static final String APP_KEY = "e7f3fbaa149d8beef86f2affedf35244";
    private static final String API_PREFIX = String.format("http://api.yummly.com/v1" +
            "/api/recipes?_app_id=%s&_app_key=%s", APP_ID, APP_KEY);

    protected ListView lv;
    protected SearchView sv;
    protected RecipesAdapter rAdapter;
    protected ArrayList<RecipePreview> recipes = new ArrayList<>();
    protected int current = 0;
    protected int possible = -1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRecipesFragment newInstance(String param1, String param2) {
        MyRecipesFragment fragment = new MyRecipesFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Favorite Recipes");

        searchForRecipes();

        if (recipes == null || recipes.size() == 0) {
            populateRecipes();
        }

        rAdapter = new RecipesAdapter(getActivity(), R.layout.my_recipes_item, recipes);
        lv = (ListView) getActivity().findViewById(R.id.recipes_list);
        lv.setAdapter(rAdapter);

        sv = (SearchView) getActivity().findViewById(R.id.mr_search_view);
        sv.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        rAdapter.getFilter().filter(newText);
                        return false;
                    }
                }
        );

        lv.setOnItemClickListener(new android.widget.ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((MainActivity) getActivity()).myRecipeClick(recipes.get(position));
            }
        });

        ImageView yummlyLogo = (ImageView) getActivity().findViewById(R.id.yummly_image);
        yummlyLogo.setImageBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.yummly_logo));
        yummlyLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout yummlyLinear = (LinearLayout) getActivity().findViewById(R.id.yummly_linear);
        yummlyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserYummly = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.yummly.com/"));
                startActivity(browserYummly);
            }
        });

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

    private void searchForRecipes() {

        try {
            if (possible == -1 || current < possible) {
                URL url = new URL(String.format("%s%s%s", API_PREFIX, "&maxResult=10&start=0",
                        "&requirePictures=true"));
                new QueryYummlyTask().execute(url);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        }

    }

    /**
     * Created by Isaac on 4/25/2017.
     */
    public class QueryYummlyTask extends AsyncTask<URL, Void, String> {

        ProgressBar pBar;
        ListView rList;

        @Override
        protected void onPreExecute() {
            pBar = (ProgressBar) getActivity().findViewById(R.id.progress_load);
            rList = (ListView) getActivity().findViewById(R.id.recipes_list);
            pBar.setVisibility(View.VISIBLE);
            rList.setVisibility(View.GONE);
            recipes = new ArrayList<>();
        }

        @Override
        protected String doInBackground(URL... params) {
            try {
                URL url = params[0];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new
                            InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    System.out.println(stringBuilder.toString());
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("")) {
                try {

                    JSONObject json = new JSONObject(result);
                    JSONArray recipesJson = json.getJSONArray("matches");

                    for (int a = 0; a < recipesJson.length(); a++) {

                        JSONObject rpJson = recipesJson.getJSONObject(a);

                        recipes.add(new RecipePreview(
                                rpJson.getString("recipeName"),
                                rpJson.getString("id"),
                                rpJson.getJSONArray("smallImageUrls").getString(0)
                            )
                        );
                    }


                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }

            pBar = (ProgressBar) getActivity().findViewById(R.id.progress_load);
            rList = (ListView) getActivity().findViewById(R.id.recipes_list);
            pBar.setVisibility(View.GONE);
            rList.setVisibility(View.VISIBLE);
        }

    }

    private void populateRecipes() {
        /*recipes = new ArrayList();
        recipes.add("Fried Rice");
        recipes.add("Everyday Baked Chicken");
        recipes.add("Burger");
        recipes.add("Crab Cake");
        recipes.add("Guacamole");
        recipes.add("Pasta");
        recipes.add("Ramen");
        recipes.add("Salmon");
        recipes.add("Tacos");
        recipes.add("Brownies");*/
    }

}
