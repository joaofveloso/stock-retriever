import './StockDetailsComponent.css';
import React from "react";

interface TimeSeries {
    date: number[];
    open: number;
    high: number;
    low: number;
    close: number;
    volume: number;
}

interface MetaData {
    symbol: string;
    timeZone: string;
    name: string;
}

export interface StockData {
    metaData: MetaData;
    timeSeries: TimeSeries[];
}

interface StockDetailsComponentProps {
    data: StockData[];
}

const calculateAverages = (timeSeries: TimeSeries[]) => {
    const totalEntries = timeSeries.length;
    const total = timeSeries.reduce((acc, day) => {
        acc.open += day.open;
        acc.high += day.high;
        acc.low += day.low;
        acc.close += day.close;
        return acc;
    }, { open: 0, high: 0, low: 0, close: 0 });

    return {
        open: (total.open / totalEntries).toFixed(2),
        high: (total.high / totalEntries).toFixed(2),
        low: (total.low / totalEntries).toFixed(2),
        close: (total.close / totalEntries).toFixed(2)
    };
};

const StockDetailsComponent: React.FC<StockDetailsComponentProps> = ({ data }) => {
    return (
        <div className="stock-details-container">
            {data.map((stock, index) => {
                const averages = calculateAverages(stock.timeSeries);
                return (
                    <div key={index} className="stock-item">
                        <div className="stock-info-left">
                            <h2>{stock.metaData.name}</h2>
                            <p>Symbol: {stock.metaData.symbol}</p>
                            <p>Timezone: {stock.metaData.timeZone}</p>
                        </div>
                        <div className="stock-table">
                            <table>
                                <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Open</th>
                                    <th>High</th>
                                    <th>Low</th>
                                    <th>Close</th>
                                    <th>Volume</th>
                                </tr>
                                </thead>
                                <tbody>
                                {stock.timeSeries.map((day, dayIndex) => (
                                    <tr key={dayIndex}>
                                        <td>{day.date.join('-')}</td>
                                        <td>{day.open}</td>
                                        <td>{day.high}</td>
                                        <td>{day.low}</td>
                                        <td>{day.close}</td>
                                        <td>{day.volume}</td>
                                    </tr>
                                ))}
                                <tr className="average-row">
                                    <td>Average</td>
                                    <td>{averages.open}</td>
                                    <td>{averages.high}</td>
                                    <td>{averages.low}</td>
                                    <td>{averages.close}</td>
                                    <td>-</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

export default StockDetailsComponent;