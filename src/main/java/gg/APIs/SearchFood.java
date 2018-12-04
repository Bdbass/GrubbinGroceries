package gg.APIs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class SearchFood {
	
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
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            String r = builder.toString(); 
            String array[] = r.split(":"); 
            
            if (array.length < 3 || array[2].equals("true")) {
            	return "Error"; 
            }else {
            	return array[1].split(",")[0]; 
            }
        } catch (IOException e) {
        	throw e; 
        }          
	}
	
	public static String parseRecipe(String input) throws IOException{

		String title = ""; 
		String description = ""; 
		HashMap<String, Double> items = new HashMap<String, Double>(); 
		String instructions = ""; 
		HashMap<String, Double> amounts = new HashMap<>(); 

		amounts.put("1/2", 0.50); 
		amounts.put("1/4", 0.25); 
		amounts.put("3/4", 0.75); 
		amounts.put("1/3", 0.33); 
		amounts.put("2/3", 0.66); 
		amounts.put("½", 0.50); 
		amounts.put("¼", 0.25); 
		amounts.put("⅔", 0.66);
		amounts.put("¾", 0.75);
		amounts.put("⅓", 0.33);

        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            
            //grab the name of the recipe 
            //skip over all faulty lines 
            String temp = br.readLine(); 
            while ((temp.trim().length() > 0)){
            	temp = br.readLine(); 
            }
            title = temp; 

            //grab the description
            temp = br.readLine(); 
            while ((temp.trim().length() > 0)){
            	temp = br.readLine(); 
            }
            description = temp; 

            //grab the ingredients 
            temp = br.readLine(); 
			do{
				//skip over blank lines
				while ((temp.trim().length() > 0)){
	            	temp = br.readLine(); 
	            }
	            //grab current line 
				temp = br.readLine();

				//loop through the line and find a number
				String num = ""; 
				boolean found = false; 
				for (int i =0; i < temp.length(); i++){
					if (Character.isDigit(temp.charAt(i)) ||
						temp.charAt(i) == '½' || 
						temp.charAt(i) == '¼' ||
						temp.charAt(i) == '⅔' ||
						temp.charAt(i) == '¾' || 
						temp.charAt(i) == '⅓' ||
						temp.charAt(i) == '/'){
					    //always grab the most recent number
					    if (found){
					    	num = ""; 
					    }
						num += temp.charAt(i); 
					}else if(Character.isLetter(temp.charAt(i))){
						found = true; 
					}
				}

				//check for '('
				if (temp.contains("(")){
					temp = temp.substring(0, temp.indexOf("(")) + temp.substring(temp.indexOf("("), temp.length()); 
				}
				//check for 'for'
				if (temp.contains("for")){
					temp = temp.substring(0, temp.indexOf("for")); 
				}
				//check for 'per'
				if (temp.contains("per")){
					temp = temp.substring(0, temp.indexOf("per")); 
				}
				//check for last 'or'
				if (temp.contains("or")){
					temp = temp.substring(temp.lastIndexOf("or"), temp.length()); 
				}

				//map this value in items
				if (amounts.containsKey(num)){
					items.put(temp, amounts.get(num));
				}else {
					items.put(temp, 1.0); 
				}
				
				temp = br.readLine();
	        } while (!temp.contains("Instructions") && !temp.contains("Directions"));

	        //grab the Instructions 
	        while ((temp = br.readLine()) != null){
	        	if (temp.trim().length() > 0){
	        		instructions += temp;
        		} 
	        }  
	        
	        //grab the restrictions
	        
	        //create recipe obj and save it 

	        //print everything out 
	        System.out.println(title); 
	        System.out.println(description); 
	        System.out.println("ingredients");
	        for (String s: items.keySet()){
	        	System.out.println(s + items.get(s)); 
	        }
	        System.out.println("Directions"); 
	        System.out.println(instructions); 
	        System.out.println("restrictions");

            return "sucess"; 
        } catch (IOException e) {
        	return e.toString(); 
        }
	}
            
	public static void main(String args[]) {
		try {
			System.out.println(processFood("orange"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
