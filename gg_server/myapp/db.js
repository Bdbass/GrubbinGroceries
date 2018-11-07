var mongoose = require("mongoose");
mongoose.connect("mongodb://localhost:27017/GrubbinGroceries", { useNewUrlParser: true });
//mongoose.connect("mongodb://localhost/GrubbinGroceries", { useNewUrlParser: true });
module.exports = mongoose;