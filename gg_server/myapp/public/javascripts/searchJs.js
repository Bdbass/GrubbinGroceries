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

function addVitamins(m){
    var res = {"vitA": 0, "vitD": 0, "vitK": 0}; 
	for (i in map){
        if (i in m){
        	switch (i){
        		case "324": 
        			res["vitD"] = m[i]; 
                    break;
        		case "318": 
        			res["vitA"] = m[i]; 
                    break;
        		case "430": 
        			res["vitK"] = m[i]; 
                    break;
        	}
        }
    }
    return res; 
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
            var holder = foodObjs[i];
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
                var temp = createMap(response.foods[0].full_nutrients); 
                var m = addVitamins(temp); 

                var xhr = new XMLHttpRequest();
                xhr.addEventListener("load", loaded);
                xhr.responseType = "json";
                xhr.open("POST", '/food/add');
                xhr.setRequestHeader("Content-type", "application/json"); 
                xhr.send(JSON.stringify({
                    n:holder, 
                    grams:response.foods[0].serving_weight_grams, 
                    cals: response.foods[0].nf_calories, 
                    fat: response.foods[0].nf_total_fat, 
                    pot: response.foods[0].nf_potassium,
                    protein: response.foods[0].nf_protein, 
                    sugars: response.foods[0].nf_sugars, 
                    fiber: response.foods[0].nf_dietary_fiber, 
                    chol: response.foods[0].nf_cholesterol,
                    carbs: response.foods[0].nf_total_carbohydrate,
                    vitA: m["vitA"], 
                    vitD: m["vitD"], 
                    vitK: m["vitK"]
                }));

	      	        $("#response").append(holder +" was saved.");  
	                console.log(response.foods[0]); 
        		});
        }
    });
}); 


function loaded() {
  // 200 is the response code for a successful GET request
  var response = document.getElementById("response"); 
  if (this.status === 201) {
    if (this.response.success) {
      response.innerHTML += "<div> success!! </div>"; 
    } 
    else {
      response.innerHTML += "<ol class='ServerResponse'>";
      for (key in this.response) {
        response.innerHTML += "<li> " + key + ": " + this.response[key] + "</li>";
      }
      response.innerHTML += "</ol>";
    }
  }
  else {
    // Use a span with dark red text for errors
    response.innerHTML += "<span class='red-text text-darken-2'>";
    response.innerHTML += "Error: " + this.response.error;
    response.innerHTML += "</span>";
  }
  // Update the response div in the webpage and make it visible
  response.style.display = "block";
}