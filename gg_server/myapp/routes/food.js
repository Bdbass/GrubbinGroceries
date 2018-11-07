var express = require('express');
var router = express.Router();
var fs = require('fs');
var Food = require("../models/food");

router.post('/add', function(req, res, next) {
   Food.findOne({name: req.body.n}, function(err, f) {
      if (err) {
         res.status(401).json({success : false, error : "Error communicating with database."});
      }
      else if(f) {
         res.status(200).json({success : false, error : "The food has already been added!"});         
      }
      else {        
            // Create an entry for the user
            var food = new Food({
             name:        req.body.n,        
             grams:       req.body.grams,
             calories:    req.body.cals,
             fat:         req.body.fat,
             potassium:   req.body.pot,
             protein:     req.body.protein,
             sugar:       req.body.sugars,
             fiber:       req.body.fiber,
             cholesterol: req.body.chol,
             carbs:       req.body.carbs,
             vitA:        req.body.vitA,
             vitD:        req.body.vitD,
             vitK:        req.body.vitK
             });
        
           food.save( function(err, f) {
             if (err) {
                // Error can occur if a duplicate email is sent
                res.status(400).json( {success: false, message: err.errmsg});
             }
             else {
                 res.status(201).json( {success: true, message: f.name + " has been created."})
             }
            }); 
        }
    });
});

module.exports = router;