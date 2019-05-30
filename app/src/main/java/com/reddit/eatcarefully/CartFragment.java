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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends ListFragment implements View.OnClickListener{

    /*public static CartFragment newInstance() {
        return new CartFragment();
    }*/
    ArrayList list1;
    String Key= "CartList";
    SharedPreferences prefs;
    ArrayAdapter adapter;
    EditText Add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.allergies_fragment, container, false);

        View view = inflater.inflate(R.layout.cart_fragment,container, false);
        Button btnAdd = view.findViewById(R.id.btnAddCart);
        btnAdd.setOnClickListener(this);
        Button btnDel =  view.findViewById(R.id.btnDelCart);
        btnDel.setOnClickListener(this);
        Button btnDelAll =  view.findViewById(R.id.btnRestore);
        btnDelAll.setOnClickListener(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(prefs.contains(Key)){
            list1= getArray(Key);
        }else{
            list1= new ArrayList();
        }
        return view;
    }

    /*public void ListUpdate(){
        if(prefs.contains(Key)){
            list1.clear();
            list1= getArray(Key);
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);
        }else{
            list1= new ArrayList();
        }
    }*/

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
        Objects.requireNonNull(getActivity()).setTitle("Cart");
        Add= Objects.requireNonNull(getView()).findViewById(R.id.txtItemCart);
        adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, list1);
        setListAdapter(adapter);

    }
    public void AddCart(){
        list1.add(Add.getText().toString());
        Add.setText("");
        adapter.notifyDataSetChanged();
        setListAdapter(adapter);
        saveArray(list1, Key);

    }
    public void DeleteCart(){
        SparseBooleanArray checkedItemPos= getListView().getCheckedItemPositions();
        int itemCount=getListView().getCount();

        for(int i=0; i<itemCount; i++){
            if(checkedItemPos.get(i)){
                adapter.remove(list1.get(i));
            }
        }
        checkedItemPos.clear();
        adapter.notifyDataSetChanged();
        saveArray(list1, Key);

    }
    public void DeleteCartAll(){

        list1.clear();
        adapter.notifyDataSetChanged();
        saveArray(list1, Key);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddCart:
                AddCart();
                break;
            case R.id.btnDelCart:
                DeleteCart();
                break;
            case R.id.btnRestore:
                DeleteCartAll();
                break;
        }
    }
}
