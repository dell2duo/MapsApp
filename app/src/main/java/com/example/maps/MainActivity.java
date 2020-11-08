package com.example.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        String menu [] = new String[] {"Casa dos meu pais", "Meu apt em Viçosa", "Minha faculdade", "Fechar APP"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String aux = l.getItemAtPosition(position).toString();

        if(position != 3){
            Intent it = new Intent(this, MapsActivity.class);
            it.putExtra("foco", position);
            Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
            startActivity(it);
        }
        else {
            finish();
        }
    }

    public void OpenMap(View v) {



    }
}