package com.reddit.eatcarefully;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class AllergiesFragment extends ListFragment implements View.OnClickListener{

    /*public static AllergiesFragment newInstance() {
        return new AllergiesFragment();
    }*/
    ArrayList list;
    String Key= "AllergyList";

    SharedPreferences prefs;
    ArrayAdapter adapter;
    EditText Add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.allergies_fragment, container, false);

        View view = inflater.inflate(R.layout.allergies_fragment,container, false);
        Button btnAdd =  view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        Button btnDel = view.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(prefs.contains(Key)){
            list= getArray(Key);
        }else{
            list= new ArrayList();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        Objects.requireNonNull(getActivity()).setTitle("Allergies");
        Add= Objects.requireNonNull(getView()).findViewById(R.id.txtItem);
        adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, list);
        setListAdapter(adapter);
    }

    public void AddAllergy(){
    list.add(Add.getText().toString());
    Add.setText("");
    adapter.notifyDataSetChanged();
    setListAdapter(adapter);
    saveArray(list, Key);
    }

    public void DeleteAllergy(){
        SparseBooleanArray checkedItemPos= getListView().getCheckedItemPositions();
        int itemCount=getListView().getCount();

        for(int i=0; i<itemCount; i++){
            if(checkedItemPos.get(i)){
                adapter.remove(list.get(i));
            }
        }
        checkedItemPos.clear();
        adapter.notifyDataSetChanged();
        saveArray(list, Key);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                AddAllergy();
                break;
            case R.id.btnDel:
                DeleteAllergy();
                break;
        }
    }
}
