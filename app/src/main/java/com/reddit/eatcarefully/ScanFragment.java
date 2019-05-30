package com.reddit.eatcarefully;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class ScanFragment extends Fragment implements View.OnClickListener {

    /*public static ScanFragment newInstance() {
        return new ScanFragment();
    }*/

    ArrayList list1;
    String Key= "CartList";
    SharedPreferences prefs;
    EditText Add;
    TextView barcodeResult;
    TextView barcodeResult2;
    ArrayList list;
    String Key2= "AllergyList";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_fragment, container, false);
        Button btnScan =  view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        Button btnAdd = view.findViewById(R.id.btnAddToCart);
        btnAdd.setOnClickListener(this);
        barcodeResult =  view.findViewById(R.id.Name);
        barcodeResult2 =  view.findViewById(R.id.Result);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(prefs.contains(Key)){
            list1= getArray(Key);
        }else{
            list1= new ArrayList();
        }

        if(prefs.contains(Key2)){
            list= getArray(Key2);
        }
        return view;
    }

    public void saveArray(ArrayList<String> list, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getArray(String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(json, type);
    }
    public void AddCart(){
        list1.add(Add.getText().toString());
        Add.setText("");
        saveArray(list1, Key);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        Objects.requireNonNull(getActivity()).setTitle("Scan");
        Add= Objects.requireNonNull(getView()).findViewById(R.id.Name);

    }

    public void ScanBar(){
        Intent intent = new Intent(getActivity(), CameraScan.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String barcodeResString = "";
        if(requestCode == 0) {
            if(data != null)
            {
                Barcode barcode = data.getParcelableExtra("barcode");
                barcodeResString = barcode.displayValue;
                barcodeResult.setText(barcodeResString);
            }
            else
            {
                barcodeResult.setText(getString(R.string.BarcodenotFound));
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
        //barcodeResString = "49000036756";
        String url = "https://api.nutritionix.com/v1_1/item?upc=" + barcodeResString+ "&appId=24543a50&appKey=48624ed2185efa520662920b5927873b";
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest= new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        String Ingredients="";
                        int flag=0;
                        try {
                            barcodeResult.setText((String)response.get("item_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            Ingredients= (String) response.get("nf_ingredient_statement");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int i;
                        for (i= 0; i<list.size();i++){
                            if(Ingredients.toLowerCase().contains(list.get(i).toString().toLowerCase())){
                                flag=1;
                                break;
                            }
                        }
                        if(flag==1){
                            barcodeResult2.setText("Allergic due to "+ list.get(i).toString());
                        }
                        else
                            barcodeResult2.setText("Not Allergic");

                    }
                },

                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barcodeResult.setText(getString(R.string.BarcodenotFound));
            }
        });
        requestQueue.add(objectRequest);

    }

        @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnScan:
                ScanBar();
                break;
            case R.id.btnAddToCart:
                AddCart();
                break;
        }

    }
}
