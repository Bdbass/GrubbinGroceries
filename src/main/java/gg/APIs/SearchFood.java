package gg.APIs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

public class SearchFood {
	//TO DO 
	
	public static String processFood(String food) throws IOException {
		System.setProperty("https.protocols", "TLSv1.1");
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            HttpPost request = new HttpPost("http://ec2-18-188-67-103.us-east-2.compute.amazonaws.com:3000/food/process");
            request.setHeader("User-Agent", "Java client");
            //request.setEntity(new StringEntity(food));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("n", food));
            request.setEntity(new UrlEncodedFormEntity(params));
            
            HttpResponse response = client.execute(request);

            HttpEntity entity = response.getEntity();
            
            return "false"; 
//
//            if (entity != null) {
//
//                // A Simple JSON Response Read
//                InputStream instream = entity.getContent();
//                String result = convertStreamToString(instream);
//                
//            //JSONObject obj = new JSONObject(result); 
//            }
//
//            
//            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
//                    response.getEntity().getContent()));
//
//            StringBuilder builder = new StringBuilder();
//
//            String line;
//
//            while ((line = bufReader.readLine()) != null) {
//                builder.append(line);
//                builder.append(System.lineSeparator());
//            }

//            return builder.toString();
        } catch (IOException e) {
        	throw e; 
        }
//            
	}
        
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
	public static void main(String args[]) {
		try {
			System.out.println(processFood("apple"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
