package com.project.driverdrowsinessdetectionsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.project.driverdrowsinessdetectionsystem.ui.contact.ContactFragment;

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
import java.nio.CharBuffer;

public class BackGroundWorker extends AsyncTask<String, Void, String > {

    Context context;

    SharedPreferences sharedpreferences;

  public BackGroundWorker(Context ctx)
    {
        context=ctx;

    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        Toast.makeText(context, ""+s,Toast.LENGTH_LONG).show();
    }
    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String register_url="http://192.168.42.151:80/DDDS/register.php";
        String notify_url="http://192.168.42.151:80/DDDS/notify.php";

        if(type.equals("register"))
        {

            String  Uid = params[1];
            String  mobile= params[2];
            try
            {
                URL url = new URL(register_url);
                try
                {
                    //return Uid;
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data= URLEncoder.encode("mobile", "UTF-8")+"="+URLEncoder.encode(mobile, "UTF-8")+"&"+URLEncoder.encode("Uid", "UTF-8")+"="+URLEncoder.encode(Uid, "UTF-8");
                    bufferedWriter.write(post_data);

                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result="";
                    String line="";
                    while ((line = bufferedReader.readLine()) !=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    checkUserExist(result);
                    return result;
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
        else if(type.equals("notify"))
        {
            String Uid = params[1] ;

            // String pass_word = params[2];
            try
            {
                URL url = new URL(notify_url);
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data= URLEncoder.encode("Uid","UTF-8")+"="+URLEncoder.encode(Uid,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    String mobile="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        mobile = line.substring(line.length() -10);
                        result = line.substring(0, 10);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    if(result.contains("successfully"))
                    {
                        //mobile=result.substring(result.length() - 10);
                        //result="successfull";
                        return result;
                    }
                    else if(result.contains("Incorrect username or password"))
                    {
                        result="Incorrect username or password";
                    }
                    checkNumber(mobile);
                    return result;

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
        else {
            return "null";
        }
        return "nulll";
    }

    public void checkUserExist(String user)
    {
        if(user.equals("Number Already Exist"))
        {

        }
        else if(user.equals("User Successfully Submitted"))
        {
            Intent intent = new Intent(context, ContactFragment.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

    public void checkNumber(String mobile)
    {
            Intent intent = new Intent(context, Sms.class);
            intent.putExtra("umobile", mobile);
            context.startActivity(intent);
            //((Activity) context).finish();

    }
}
