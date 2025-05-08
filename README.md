# Nutriplus

This is the backend of Nutriplus, a meal logger to follow daily nutriments intakes.

## Quickstart

You will need a PostgresSQL database and a Redis instance.

## Endpoints

- `/api/v1/public/scan` Scan a barcode to get the product's nutritional values. 
It calls OpenFoodFacts to retrieve the values and store them in a Redis cache.