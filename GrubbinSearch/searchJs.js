//divide vitamin A by 507.45 to get percentage
//divide vitamin K by 134.00 to get percentage 
//divide vitamin D by 600.00 to get percentage
//fat is in g
//potassum is in mg
//protein is in g 
//sugar is in g
//fiber is in g
//cholesterol is in mg
//carbs are in g
//vitamin a is in percent of dv
//vitamin d is in percent of dv
//vitamin k is in dv 

var mongoose = require("mongoose");
mongoose.connect("mongodb://localhost/mydb");

var FoodSchema = new mongoose.Schema({
   name:      	{type: String, default: "Unknown"},
   grams:     	{type: Number, default: 0.00 },
   calories:  	{type: Number, default: 0.00 },
   fat:  	  	{type: Number, default: 0.00 },
   potassium: 	{type: Number, default: 0.00 },
   protein:   	{type: Number, default: 0.00 },
   sugar:     	{type: Number, default: 0.00 },
   fiber:     	{type: Number, default: 0.00 },
   cholesterol: {type: Number, default: 0.00 },
   carbs: 		{type: Number, default: 0.00 },
   vitA: 		{type: Number, default: 0.00 },
   vitD: 		{type: Number, default: 0.00 },
   vitK: 		{type: Number, default: 0.00 },
});


var Food = mongoose.model("Food", FoodSchema);

map = {324: "vitamin D", 318: "vitamin A", 430: "vitamin K"}; 

function createMap(array){
    var tempMap = {}; 
    for (i in array){
        tempMap[array[i].attr_id] = array[i].value; 
    }
    return tempMap; 
}

// function appendVitamins(m){
//     var s = ""; 
//     for (i in map){
//         if (i in m){
//             s += map[i] + ": " + m[i] + " <br />"; 
//         }else{
//             s += map[i] + ": 0" + " <br />"; 
//         }
//     }
//     return s; 
// }

function addVitamins(m , food){
	for (i in map){
        if (i in m){
        	switch (i){
        		case 324: 
        			food.vitD = m[i]; 
        		case 318: 
        			food.vitA = m[i]; 
        		case 430: 
        			food.vitK = m[i]; 
        	}
        }
    }
}

$(function(){
    $("#search").click(async function() {   
        var foodObjs = [];
        var temp = {}; 
        if ($("#foods").val()) {
            // Create an array from the comma-separated values
            foodObjs = $("#foods").val().split(",");
        }

        for (i in foodObjs){
            var settings = {
            "async": true,
            "crossDomain": true,
            "url": "https://trackapi.nutritionix.com/v2/natural/nutrients",
            "method": "POST",
            "headers": {
            "content-type": "application/json",
            "accept": "application/json",
            "x-app-id": "4be29d1c",
            "x-app-key": "e74ba1e5c37797b818c4e44d87c3e75b",
            "x-remote-user-id": "0",
            "cache-control": "no-cache",
            },
            "processData": false,
            "data": "{\n \"query\": \"" + foodObjs[i] + "\",\n \"num_servings\": 1,\n \"aggregate\": \"string\",\n \"line_delimited\": false,\n \"use_raw_foods\": false,\n \"include_subrecipe\": false,\n \"timezone\": \"US/Eastern\",\n \"consumed_at\": null,\n \"lat\": null,\n \"lng\": null,\n \"meal_type\": 0,\n \"use_branded_foods\": false,\n \"locale\": \"en_US\"\n }"
            }

            await $.ajax(settings).done(function (response) {
            	var food = new Food({
   				   name: 		foodObjs[i],       	
				   grams: 		response.foods[0].serving_weight_grams,
				   calories: 	response.foods[0].nf_calories,
				   fat: 		response.foods[0].nf_total_fat,
				   potassium: 	response.foods[0].nf_potassium,
				   protein: 	response.foods[0].nf_protein,
				   sugar:     	response.foods[0].nf_sugars,
				   fiber:     	response.foods[0].nf_dietary_fiber,
				   cholesterol: response.foods[0].nf_cholesterol,
				   carbs: 		response.foods[0].nf_total_carbohydrate,
				   });

                // $("#response").append("name: " + foodObjs[i] + "<br />" + "grams in serving: " + response.foods[0].serving_weight_grams + "<br />" +
                // 	"calories: " + response.foods[0].nf_calories+ "<br />" +
                //     "fat :" + response.foods[0].nf_total_fat + "<br />" + "potassium: " + response.foods[0].nf_potassium +  "<br />" +
                //     "protein: " + response.foods[0].nf_protein + "<br />" + "sugar: " + response.foods[0].nf_sugars +  "<br />" + 
                //     "fiber: " + response.foods[0].nf_dietary_fiber + "<br /> cholesterol: " + response.foods[0].nf_cholesterol +  
                //     "<br /> carbs: " + response.foods[0].nf_total_carbohydrate +  "<br />");
                
                temp = createMap(response.foods[0].full_nutrients); 
                addVitamins(temp, food); 
                
                // $("#response").append(appendVitamins(temp) + "<br /><br />");

                console.log(response.foods[0]); 
            }); 
        }
    });
}); 




