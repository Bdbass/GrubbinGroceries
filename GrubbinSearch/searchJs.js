$(function(){
    $("#search").click(async function() {   
        var foodObjs = [];
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
            $("#response").append("name: " + foodObjs[i] + "<br />" + "calories: " + response.foods[0].nf_calories+ "<br />" +
                "fat :" + response.foods[0].nf_total_fat + "<br />" + "potassium: " + response.foods[0].nf_potassium +  "<br />" +
                "protein: " + response.foods[0].nf_protein + "<br />" + "sugar: " + response.foods[0].nf_sugars +  "<br />" + 
                "fiber: " + response.foods[0].nf_dietary_fiber + "<br /><br />");
            console.log(response.foods[0]);
            }); 
        }
    });
}); 




