# Stock Data Processing Application

This application processes and exposes stock information through a REST API. It leverages Spring Boot, Reactor for reactive programming, and various Spring components.

## Table of Contents

1. [Installation - Docker](#installation)
2. [Usage](#usage)
3. [Contributing](#contributing)
4. [License](#license)

## Installation

### Docker

To run the application using Docker, follow these steps:

1. Ensure you have Docker installed on your machine. You can download it from [Docker's official website](https://www.docker.com/products/docker-desktop).

2. Build the Docker image:

    ```sh
    docker build -t stock-data-processing-app .
    ```

3. Run the Docker container:

    ```sh
    docker run -p 8080:8080 stock-data-processing-app
    ```

## Usage

Once the application is running, you can interact with it through the following endpoints:

- **POST /stocks**: Update the list of stocks.
- **GET /stocks**: Obtain the list of stocks.
- **GET /stocks/information**: Obtain stock metadata information.
- **GET /stocks/{symbol}**: Obtain stock unity for a specific symbol.

Example request to update the list of stocks:

```sh
curl -X POST http://localhost:8080/stocks -H "Content-Type: application/json" -d '{"endDate":"07-16-2024", "stocks":["AAPL", "MSFT"]}'
```

## Domain Dictionary

- **Stock Data**: The main domain entity representing stock information, including metadata and time series data
  - **Metadata**: Information about the stock, such as symbol, information, last refreshed date, output size, and time zone.
  - **Stock Unity**: Daily stock ata, including date, open price, high price, low price, close price, volume and symbol.
- **Period Calculation**: Enumeration representing different date ranges for Stock Unity (DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUALLY)