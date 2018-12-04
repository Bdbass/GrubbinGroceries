package gg.APIs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import gg.mealInfo.MealType;
import gg.mealInfo.Recipe;

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
            String error; 
            if (array.length < 3) { 
            	return "Error"; 
            }else {
            	error = array[2].substring(1, array[2].length()-3);
            	if (error.equals("true")) {
            		return "Error";
            	}
            	return array[1].split(",")[0]; 
            }
        } catch (IOException e) {
        	throw e; 
        }          
	}
	
	public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static String parseRecipe(String input, ArrayList<String> restrictions, String mealType) throws IOException {

        String title = "";
        String description = "";
        HashMap<String, Double> items = new HashMap<String, Double>();
        String instructions = "";
        HashMap<String, Double> amounts = new HashMap<>();
        String processedFood; 
        //ArrayList<String> restrictions1 = new ArrayList<>();
        
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
            if (temp == null) {
            	return "bad file"; 
            }
            while (!(temp.trim().length() > 0)) {
                temp = br.readLine();
            }
            title = temp;

            //grab the description
            temp = br.readLine();
            if (temp == null) {
            	return "bad file"; 
            }
            while (!(temp.trim().length() > 0)) {
                temp = br.readLine();
            }
            description = temp;

            //grab the ingredients
            temp = br.readLine();
            if (temp == null) {
            	return "bad file"; 
            }
            do {
                //skip over blank lines
                while (!(temp.trim().length() > 0)) {
                    temp = br.readLine();
                }
                
                
                //throw out ingredients
                if (temp.contains("Ingredients") || temp.contains("ingredients")) {
                    temp = br.readLine();
                    continue;
                }
                         
                
                //break on instructions or directions
                if (temp.contains("Instructions") || temp.contains("Directions")){
                    break;
                }

                //edit the line to be readable
                int j = 0;
                //check for '('
                if (temp.contains("(")) {
                    temp = temp.substring(0, temp.indexOf("(")) + temp.substring(temp.indexOf("("), temp.length());
                }
                //check for 'for'
                if (temp.contains("for")) {
                    temp = temp.substring(0, temp.indexOf("for"));
                }
                //check for 'per'
                if (temp.contains("per") && temp.charAt(temp.indexOf("per")-1) == ' '){
                    temp = temp.substring(0, temp.indexOf("per"));
                }
                //check for last 'or'
                temp = temp.trim();
                while (Character.isDigit(temp.charAt(temp.length()-1))) {
                    temp = temp.substring(0, temp.length()-2);
                }

                //loop through the line and find a number
                String num = "";
                boolean found = false;
                for (int i = 0; i < temp.length(); i++) {
                    if (Character.isDigit(temp.charAt(i)) ||
                            temp.charAt(i) == '½' ||
                            temp.charAt(i) == '¼' ||
                            temp.charAt(i) == '⅔' ||
                            temp.charAt(i) == '¾' ||
                            temp.charAt(i) == '⅓' ||
                            temp.charAt(i) == '/') {
                        //always grab the most recent number
                        if (found) {
                            num = "";
                            found = false;
                        }
                        num += temp.charAt(i);
                    } else if (Character.isLetter(temp.charAt(i))) {
                        found = true;
                    }
                }

                //map this value in items
				processedFood = processFood(temp); 
                if (!processedFood.equals("Error") && !processedFood.equals("Error")) {
	                if (amounts.containsKey(num)) {
	                    items.put(processedFood.substring(1, processedFood.length()-1), amounts.get(num));
	                } else if(isInteger(num, 10)) {
	                    items.put(processedFood.substring(1, processedFood.length()-1), Double.parseDouble(num));
	                }else {
	                    items.put(processedFood.substring(1, processedFood.length()-1), 1.0);
	                }
                }
                

                temp = br.readLine();
            } while (!temp.contains("Instructions") && !temp.contains("Directions"));

            //grab the Instructions
            while ((temp = br.readLine()) != null && !temp.trim().equalsIgnoreCase("Restrictions")
            		&& !temp.trim().equals("Restrictions:")) {
                if (temp.trim().length() > 0) {
                    instructions += temp + "\n";
                }
            }
            
            //print everything out
            System.out.println(title + "\n");
            System.out.println(description);
            System.out.println("\nIngredients:\n");
            for (String s : items.keySet()) {
                System.out.println(s + ": " +items.get(s));
            }
            System.out.println("\nDirections\n");
            System.out.println(instructions);
            System.out.println("restrictions");
//            for (String s: restrictions1) {
//            	System.out.println(s);
//            }
            
            //create recipe obj
            Recipe r = new Recipe(items, instructions, title, true, restrictions, MealType.valueOf(mealType)); 
            
            return "success";
        } catch (IOException e) {
            return e.toString();
        }
    }
            
	public static void main(String args[]) {
		try {
			ArrayList<String> r = new ArrayList<String>(); 
			r.add("GF"); 
			parseRecipe("recipe3.txt", r, MealType.BREAKFAST.toString()); 
		}catch(IOException e) {
			System.out.println(e);
		}
//		try {
//			System.out.println(processFood("orange"));
//		} catch (IOException e) {
//			System.out.println(e);
//		}
	}
}
