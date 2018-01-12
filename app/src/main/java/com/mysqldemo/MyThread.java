package com.mysqldemo;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

class MyThread extends Thread {

    private final String ADDRESS = "http://192.168.10.7:82/RetrieveDonorDetail.php";
    private String bloodGroup;
    private String city;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    private String [] data = new String [6];

    MyThread(String bloodGroup, String city){

        this.bloodGroup = bloodGroup;
        this.city = city;
    }

    @Override
    public void run() {

        try {
            URL url = new URL(ADDRESS);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            writeData(outputStream);
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            readData(inputStream);
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

    private void writeData(OutputStream out) throws IOException {

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(out,"iso-8859-1"));
        String post = URLEncoder.encode("DonorBlood","UTF-8")+"="+URLEncoder.encode(this.bloodGroup,"UTF-8")+"&"+
                URLEncoder.encode("DonorCity","UTF-8")+"="+URLEncoder.encode(this.city,"UTF-8");

        bufferedWriter.write(post);
        bufferedWriter.flush();

    }
    private void readData(InputStream in) throws IOException {
        String line;
        int counter=0;

        bufferedReader = new BufferedReader(new InputStreamReader(in,"iso-8859-1"));

        while((line = bufferedReader.readLine())!=null){

            data[counter++] = line;
        }

    }

    String [] getData(){

        return data;
    }

}
