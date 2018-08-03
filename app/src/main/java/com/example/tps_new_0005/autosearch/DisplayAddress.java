package com.example.tps_new_0005.autosearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class DisplayAddress extends AppCompatActivity {

    EditText et,et2,et3,et4,et5,et6,et7,et8,et9;
    String streetno,street,sublocality1,sublocality2,sublocality3,city,state,pincode,country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_address);
        et=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        et4=(EditText)findViewById(R.id.editText4);
        et5=(EditText)findViewById(R.id.editText5);
        et6=(EditText)findViewById(R.id.editText6);
        et7=(EditText)findViewById(R.id.editText7);
        et8=(EditText)findViewById(R.id.editText8);
        et9=(EditText)findViewById(R.id.editText9);
        Bundle b=getIntent().getExtras();
        if(b!=null)
        {
            streetno=b.getString("streetno");
            if(streetno==null)
                streetno=" ";
            street=b.getString("street");
            if(street==null)
                street=" ";
            sublocality1=b.getString("sublocality1");
            if(sublocality1==null)
                sublocality1=" ";
            sublocality2=b.getString("sublocality2");
            if(sublocality2==null)
                sublocality2=" ";
            sublocality3=b.getString("sublocality3");
            if(sublocality3==null)
                sublocality3=" ";
            city=b.getString("city");
            if(city==null)
                city=" ";
            state=b.getString("state");
            if(state==null)
                state=" ";
            pincode=b.getString("pincode");
            if(pincode==null)
                pincode=" ";
            country=b.getString("country");
            if(country==null)
            country=" ";
        }
        et9.setText(String.valueOf(streetno));
        et.setText(String.valueOf(street));
        et2.setText((String.valueOf(sublocality3)));
        et3.setText(String.valueOf(sublocality2));
        et4.setText(String.valueOf(sublocality1));
        et5.setText(String.valueOf(city));
        et6.setText(String.valueOf(state));
        et7.setText(String.valueOf(pincode));
        et8.setText(String.valueOf(country));
        Toast.makeText(getApplicationContext(),"New Activity Started: "+name,Toast.LENGTH_SHORT).show();
    }
}
