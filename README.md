# beer-yo-ass-backend

Server is live: https://beer-yo-ass-backend.herokuapp.com/

## How do I get this ?
Your IDE ([IntelliJ](https://www.jetbrains.com/idea/), [Eclipse](https://eclipse.org/), [Spring Tool Suit](https://spring.io/tools)) should be able to clone a project from Github.
It should be easy to find information regarding how to do that for your chosen IDE.
It's also possible to download the project as a .zip [here](https://github.com/mbook/SpringBootMVC/archive/master.zip).

## How do I run this ?
You have to have postgres downloaded on your machine and a db called HBV with username "postgres" and password "postgres" <br />

the tables are made automatically <br /> 
This project is setup using [Maven](https://maven.apache.org/what-is-maven.html) as a dependency manager, so if your IDE does not manage that, or you don't have it installed you can look [here](https://maven.apache.org/install.html) for further information.
When all the dependencies are downloaded, you can run the project by running the ``main()`` method in the class ``Application`` and then enter [localhost:8080](http://localhost:8080) into the address bar of your favorite web browser.


### Populating the beer database
You have to have installed a couple of python packages
```$xslt
pip install psycopg2
pip install selenium
pip install bs4
```
You have to have firefox.

Then you need to go into the scraper folder and run
```$xslt
python vinbud_scraper.py
```

Then you have to turn on your postgres database server, with user:postgres, pw:postgres, and create a database named beer-yo-ass

Then go into database folder and run
```$xslt
python createTables.py
python insertData.py
```

It is possible to drop all the tables with the command in the same folder
```$xslt
python deleteTables.py
```


## Apinn
note, this application is not secure, maybe we will implement a secure app later.
<br />

### get beers
send a get request to 
<br />
/beers
<br />
to get a response as a list of all the beers.

### get a beer
send a get request to 
<br />
/beers/beerId
<br />
beerId (String) is the id of the beer,
<br />
get a response following information about one beer
<br />
beerId, name, 
linkToVinbudin, 
(float) alcohol, 
taste (e.g. lager, IPA),
(int) volume (in ml),
(int) price (kr.),
votes (int) how many times the beer has been voted on,
(float) stars (out of 5) if never voted on the value will be -1.

### User

#### login
send a post request to 
<br />
/login/{username}/{password}
<br />
if the username and password match the response will be (boolean) true otherwise false.
<br />
#### signup
send a post request to 
<br />
/signup/{username}/{password}
<br />
if the username is not taken, the response is (boolean) true otherwise false.

#### profile picture
send a post request to 
<br />
/changeProfilePicture/{username}/{beerId}
<br />
To set user's profile picture to the beerId

### comment
send a post request to 
<br />
/comment/{username}/{beerId}/{title}/{comment}/{stars}
<br />
username is the user logged in, 
beerId (String) is the beer being commented on,
title is the title of the comment,
comment is what the user writes about the beer,
stars (float) how many stars out of 5 the user rates the beer. If stars is lower than zero then the stars don't go into the database.
<br />
response will be (boolean) true if comment was saved otherwise false.
<br />
<br />

#### delete comment
send a request to 
<br />
/deleteComment/{commentId}
<br />
To delete comment with id commentId (Long)
<br />
<br />
response will be true (need to fix that)
#### update comment
send a request to 
<br />
/updateComment/{commentId}/{title}/{comment}/{stars}
<br />
To update comment with id commentId (Long)
<br />
response will be true (need to fix that)

#### rate
send a request to 
<br />
/rate/{username}/{beerId}/{stars}


### My Beers
send a post request to 
<br />
/addToMyBeers/{username}/{beerId}
<br />
To a beer add to my beers
<br />
response will be (boolean) true if comment was saved otherwise false.
<br />
<br />
Send a get request to 
<br />
/myBeers/{username}
<br />
To get all the users beers.
<br />
Response will be JSON Array.
 
### Drinklists
get request 
<br />
/getMyDrinklists/{username}
<br />
<br />
get request to
<br />
/getAllPublicDrinklists
<br />
<br />
post request 
<br />
/createDrinklist/{username}/{name}/{public}
<br />
<br />
post request to 
<br />
/addToDrinklist/{username}/{drinklistId}/{beerId}
<br />
<br />
post request
<br />
/markBeerOnDrinklist/{username}/{drinklistId}/{beerId}/{marked}
<br />
where {marked} is a boolean weather the beer should be marked or not, could be a problem in the future if many of the same beer are in the same drinklist
<br />
<br />
post request to
<br />
/deleteDrinklist/{username}/{drinklistId}
<br />
<br/>



#TODO
possible to update the beers tables with scraper so comments stay with their beers.