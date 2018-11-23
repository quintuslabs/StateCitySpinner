package com.quintus.labs.statecityspinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Spinner citySpinner, stateSpinner;
    ArrayList<String> stringArrayState;
    ArrayList<String> stringArrayCity;
    String spinnerStateValue, _city, _state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citySpinner = findViewById(R.id.citySpinner);
        stateSpinner = findViewById(R.id.stateSpinner);

        init();
    }


    private void init() {
        stringArrayState = new ArrayList<String>();
        stringArrayCity = new ArrayList<String>();

        //set city adapter
        final ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinnertextview, stringArrayCity);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);


        //Get state json value from assets folder
        try {
            JSONObject obj = new JSONObject(loadJSONFromAssetState());
            JSONArray m_jArry = obj.getJSONArray("statelist");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String state = jo_inside.getString("State");
                String id = jo_inside.getString("id");

                stringArrayState.add(state);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinnertextview, stringArrayState);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);


        //state spinner item selected listner with the help of this we get selected value

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String Text = stateSpinner.getSelectedItem().toString();


                spinnerStateValue = String.valueOf(stateSpinner.getSelectedItem());
                _state = spinnerStateValue;
                Log.e("SpinnerStateValue", spinnerStateValue);
                stringArrayCity.clear();

                try {
                    JSONObject obj = new JSONObject(loadJSONFromAssetCity());
                    JSONArray m_jArry = obj.getJSONArray("citylist");

                    for (int i = 0; i < m_jArry.length(); i++) {
                        JSONObject jo_inside = m_jArry.getJSONObject(i);
                        String state = jo_inside.getString("State");
                        String cityid = jo_inside.getString("id");

                        if (spinnerStateValue.equalsIgnoreCase(state)) {
                            _city = jo_inside.getString("city");
                            stringArrayCity.add(_city);
                        }

                    }

                    //notify adapter city for getting selected value according to state
                    adapterCity.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerCityValue = String.valueOf(citySpinner.getSelectedItem());
                Log.e("SpinnerCityValue", spinnerCityValue);

                _city = spinnerCityValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public String loadJSONFromAssetState() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("state.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadJSONFromAssetCity() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("cityState.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
