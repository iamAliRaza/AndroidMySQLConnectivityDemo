package com.mysqldemo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private MyThread thread;
    private String []getDonorData;
    private Thread handler;
    private ArrayAdapter<String> arrayAdapter;
    private String [] Cities = new String [5];
    private String [] bloodGroups = new String [5];
    private Spinner [] spinner = new Spinner[2];
    final String ADDRESS = "http://192.168.10.7:82/FetchDonorData.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0;i<5;i++){

            Cities[i] = bloodGroups[i] = "No Connection";
        }

        try {
            handle();
            handler.join();
        } catch (InterruptedException e) {

        }
        initGUI();
    }

    void handle () {

        handler = new Thread(this::establishHTTPClient);
        handler.start();
    }

    private void establishHTTPClient (){
        String line;

    try {
        URL url = new URL(ADDRESS);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoInput(true);
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
        int counter = 0;
        int temp=0;
        while((line = bufferedReader.readLine())!=null){

                if (counter > 4){

                    this.bloodGroups[temp] = line;
                    temp++;
                }
                else {
                    this.Cities[counter] = line;
                }
                    counter++;

        }

        bufferedReader.close();
        inputStream.close();
        httpURLConnection.disconnect();

    }catch(MalformedURLException exception){
        exception.printStackTrace();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    }

    private void initGUI(){

        spinner[0] = findViewById(R.id.city);
        spinner[1] = findViewById(R.id.bloodGroup);

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,Cities);
        spinner[0].setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,bloodGroups);
        spinner[1].setAdapter(arrayAdapter);


        Button button = findViewById(R.id.searchButton);

        button.setOnClickListener(v->{

            thread = new MyThread(spinner[1].getSelectedItem().toString(),spinner[0].getSelectedItem().toString());
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getDonorData = thread.getData();
            setData();

        });
    }

    private void setData(){

        TextView textView = findViewById(R.id.datatv);
        String paste = "Donor Name            :  "+getDonorData[0]+"\n"+
                       "Donor Blood Group :  "+getDonorData[1]+"\n"+
                       "Donor MobileNo      :  "+getDonorData[2]+"\n"+
                       "Donor Age                :  "+getDonorData[3]+"\n"+
                       "Donor City                :  "+getDonorData[4]+"\n"+
                       "Donor Address        :  "+getDonorData[5];

        textView.setText(paste);
    }
}
