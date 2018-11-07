#GrubbinGroceries

How to add food to db: 

1. First pull the updates. 
2. Run mongod 
3. Open up a terminal, cd to GrubbinGroceries/gg_server/myapp
4. copy and paster : DEBUG=myapp:* npm start  , into your terminal and run it 
5. You should see "myapp:server Listening on port 3000 +0ms" if it is working 
6. Then type in 'localhost:3000' in chrome or whatever browser you're using
7. Then type in whatever food you want in a list format i.e: tuna, orange, apple, ....  and you should see something like 
tuna was saved.
success!!
if it worked 
8. then open up another terminal and run a mongo shell
9. type 'show dbs' 
You should see something like 
GrubbinGroceries
admin
config
local 
10. type 'use GrubbinGroceries'
11. type 'show collections'
You should see 'foods' 
12. type 'db.foods.find()' 
this should display all the food in your database currently

