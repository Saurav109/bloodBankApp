package com.example.bloodbank.adapter;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: why i cant just say String instead of 'S'
public class CustomHashMap<S, V> {
    private String TAG = "CustomHashMap";
    private HashMap<S, V> valueHashMap;
    private List<S> indexList;

    public CustomHashMap() {
        valueHashMap = new HashMap<S, V>();
        indexList = new ArrayList<>();
    }

    public void add(S key, V v) {
        indexList.add(0, key);
        valueHashMap.put(key, v);
        Log.e(TAG, "adding key: " + key);
    }

    public void change(S key, V v) {
        valueHashMap.put(key, v);
        Log.e(TAG, "changing key: " + key);
    }

    public Boolean remove(S key) {
        V v = valueHashMap.get(key);
        if (v != null) {
            valueHashMap.remove(key);
            indexList.remove(key);
            Log.e(TAG, "removing key: " + key);
            return true;
        } else {
            Log.e(TAG, "removing but key not found, key: " + key);
            return false;
        }
    }

    public int size() {
        return indexList.size();
    }

    public void clear() {
        indexList.clear();
        valueHashMap.clear();
    }

    public V getValueByIndex(int i) {
        S key = indexList.get(i);
        return valueHashMap.get(key);
    }

    public V getByKey(S key) {
        return valueHashMap.get(key);
    }

    public int getIndexByKey(S key) {
        return indexList.indexOf(key);
    }

    public String getKeyByIndex(int i) {
        //TODO: recheck someday, cz i think its going to be a problem
        return (String) indexList.get(i);
    }
}
