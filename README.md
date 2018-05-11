# Word Stats

Simple web service for parsing text files and get words stats.

Run the application should be easy as:

## Clone locally
```
git clone git@github.com:ascariandrea/words-stats.git
```

## Run the server
```
cd api
sbt run
```

## Run the client
```
cd client
yarn
yarn build
```

Browser should automatically open `http://localhost:3000/`.
Once the page is loaded you should be able to upload a file clicking the input, and get the stats by clicking "Send" button.

