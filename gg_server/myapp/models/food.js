var db = require("../db");

var FoodSchema = new db.Schema({
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
   vitK: 		{type: Number, default: 0.00 }
});

var Food = db.model("Food", FoodSchema);

module.exports = Food;

