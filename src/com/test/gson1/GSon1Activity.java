package com.test.gson1;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class GSon1Activity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson1);
        
        Gson gson = new Gson();
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 10; i++) {
             Person p = new Person();
             p.setName("name" + i);
             p.setAge(i * 5);
             persons.add(p);
        }
        String str = gson.toJson(persons);
  
        
//        Person person = gson.fromJson(str, Person.class);
        List<Person> ps = gson.fromJson(str, new TypeToken<List<Person>>(){}.getType());
        for(int i = 0; i < ps.size() ; i++)
        {
             Person p = ps.get(i);
             System.out.println(p.toString());
        }

        String city = "西安";  
        getWeather(city);  

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gson1, menu);
        return true;
    }


private static final String NAMESPACE = "http://WebXml.com.cn/";

// WebService地址
private static String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";

//private static final String METHOD_NAME = "qqCheckOnline";
private static final String METHOD_NAME = "getWeatherbyCityName";

private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";

private String weatherToday;

private SoapObject detail;

public void getWeather(String cityName) {
	try {
		System.out.println("rpc------");
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
	//	System.out.println("rpc" + rpc);
	//	System.out.println("cityName is " + cityName);
		rpc.addProperty("theCityName", cityName);

		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		
		HttpTransportSE ht = new HttpTransportSE(URL);

		//AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;

		ht.call(SOAP_ACTION, envelope);
		//ht.call(null, envelope);

		//SoapObject result = (SoapObject)envelope.bodyIn;
		//detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");

		detail =(SoapObject) envelope.getResponse();
		
		//System.out.println("result" + result);
		System.out.println("detail" + detail);
		Toast.makeText(this, detail.toString(), Toast.LENGTH_LONG).show();
		parseWeather(detail);

		return;
	} catch (Exception e) {
		e.printStackTrace();
	}
}

private void parseWeather(SoapObject detail)
		throws UnsupportedEncodingException {
	String date = detail.getProperty(6).toString();
	weatherToday = "今天：" + date.split(" ")[0];
	weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
	weatherToday = weatherToday + "\n气温："
			+ detail.getProperty(5).toString();
	weatherToday = weatherToday + "\n风力："
			+ detail.getProperty(7).toString() + "\n";
	System.out.println("weatherToday is " + weatherToday);
	Toast.makeText(this, weatherToday, Toast.LENGTH_LONG).show();

}


}

    

//==========================================================================
