# Stock Data Processing Application

This application processes and exposes stock information through a REST API. It leverages Spring Boot, Reactor for reactive programming, and various Spring components.

# Usage

Once the application is running, you can interact with it through the following endpoints:

## Endpoint `POST /stocks`
### Description: 
Update the list of stocks from three weeks ago to a specific date. If no stocks are specified, all stocks defined in the application properties will be fetched.
### Body:
- date (Date format: MM-dd-yyyy): Represents the target date. The range will be three weeks from this target date.
- stocks[] (List<String>): List of company stocks symbols

## Endpoint `GET /stocks`
### Description:
Obtain a list of based on a specific date and a defined range
### Query parameters:
- date (Date format: MM-dd-yyyy): Represents the target date. The range will be three weeks from this target date.
- size (Enum, accepted values: [`DAILY`, `WEEKLY`, `MONTHLY`, `QUARTERLY`, `ANNUALLY`]): This parameter defines the range used to calculate the date range for fetching information from the database. If no value is provided or an invalid value is specified, `WEEKLY` will be used as the default.

## Endpoint `GET /stocks/{SYMBOL}`
### Description:
Obtain a list of stock unities associated to the {SYMBOL} and within a range define by `date` (required) and `size` (optional)
### Path params:
- SYMBOL: The Stock company symbol
### Query parameters:
- date (Date format: MM-dd-yyyy): Represents the target date. The range will be three weeks from this target date.
- size (Enum, accepted values: [`DAILY`, `WEEKLY`, `MONTHLY`, `QUARTERLY`, `ANNUALLY`]): This parameter defines the range used to calculate the date range for fetching information from the database. If no value is provided or an invalid value is specified, `WEEKLY` will be used as the default.

## Endpoint `GET /stocks/information`
### Description:
Obtain a list of stock information, including symbol, name, and logo.

## Domain Dictionary

- **Stock Data**: The main domain entity representing stock information, including metadata and time series data
  - **Metadata**: Information about the stock, such as symbol, information, last refreshed date, output size, and time zone.
  - **Stock Unity**: Daily stock ata, including date, open price, high price, low price, close price, volume and symbol.
- **Period Calculation**: Enumeration representing different date ranges for Stock Unity (DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUALLY)