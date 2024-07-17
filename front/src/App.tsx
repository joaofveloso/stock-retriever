import React, { useState, useEffect } from 'react';
import 'react-datepicker/dist/react-datepicker.css';
import { format } from 'date-fns';
import './App.css';
import './components/DatePickerComponent.css'
import Tabs from "./components/Tabs";
import DatePickerComponent from "./components/DatePickerComponent";
import Carousel from "./components/Carousel";
import Notification from "./components/Notification";
import StockDetailsComponent from "./components/StockDetailsComponent";
import { StockData } from "./components/StockDetailsComponent"

interface Stock {
    symbol: string;
    name: string;
    image: string;
    isSelected: boolean;
}

const App = () => {

  const [selectedDate, setSelectedDate] = useState<Date | null>(new Date());
  const [toggleStock, setToggleStock] = useState<Stock[]>([]);
  const [notification, setNotification] = useState<{ message: string, type: 'success' | 'error', visible: boolean } | null>(null);
  const [activeTab, setActiveTab] = useState<'stockUpdate' | 'stockDetails'>('stockUpdate');
  const [stockDetailsData, setStockDetailsData] = useState<StockData[]>([]);

  useEffect(() => {
    fetch('http://localhost:8080/stocks/information')
        .then(response => {
          if (!response.ok) {
            throw new Error(response.statusText);
          }
          return response.json();
        }).then(data => {
          const stock = data.map((stock: Stock) => ({ ...stock, isSelected: false}));
          setToggleStock(stock);
        }).catch(error => {
          console.error('Error fetching stock information: ', error);
        });
  }, []);

    const handleStockDetailsDateChange = (date: Date | null) => {
        setSelectedDate(date);
        if (date) {
            const formattedDate = format(date, 'MM-dd-yyyy');
            fetch(`http://localhost:8080/stocks?date=${formattedDate}`)
                .then(response => response.json())
                .then(data => setStockDetailsData(data))
                .catch(error => console.error('Error fetching stock details:', error));
        }
    };

  const handleButtonClick = async () => {
    if (!selectedDate) {
      setNotification({message:'Please select a date', type: 'error', visible: true});
      setTimeout(() => {
          setNotification(prev => prev ? { ...prev, visible: false } : null);
      }, 3000);
      return;
    }

    const formattedDate = format(selectedDate, 'MM-dd-yyyy');
    const selectedStocks = toggleStock.filter(stock => stock.isSelected).map(stock => stock.symbol);

    const payload = {
      endDate: formattedDate,
      stocks: selectedStocks
    };

    fetch('http://localhost:8080/stocks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    }).then(response => {
      if (!response.ok) {
        throw new Error(response.statusText);
      }
      return {};
    }).then(() => {
      setNotification({message: 'Your request was accepted, we are processing it.', type: 'success', visible: true});
      setTimeout(() => {
          setNotification(prev => prev ? { ...prev, visible: false } : null);
      }, 3000);
    }).catch(error => {
      console.log('Error: ', error);
      setNotification({message: 'There was a error with the request.', type: 'error', visible: true});
      setTimeout(() => {
        setNotification(prev => prev ? { ...prev, visible: false } : null);
      }, 3000);
    });
  };

  const toggleStockSelection = (symbol: string) => {
      setToggleStock(prevStock => prevStock.map(stock =>
          stock.symbol === symbol ? { ...stock, isSelected: !stock.isSelected } : stock
      ));
  };

  return (
      <div className="app-container">
          <h1>Stock Update</h1>
          <Tabs activeTab={activeTab} setActiveTab={setActiveTab} />
          {activeTab === 'stockUpdate' && (
              <>
                  <DatePickerComponent selectedDate={selectedDate} setSelectedDate={setSelectedDate} selectionType='daily'/>
                  <Carousel toggleStock={toggleStock} stockSelection={toggleStockSelection}/>
                  <button className='tab-button' onClick={handleButtonClick}>Download Stock information</button>
              </>
          )}
          {activeTab === 'stockDetails' && (
              <>
                  <DatePickerComponent selectedDate={selectedDate} setSelectedDate={handleStockDetailsDateChange} selectionType='weekly'/>
                  <StockDetailsComponent data={stockDetailsData} />
              </>
          )}
          {notification && (
              <Notification notification={notification} />
          )}
      </div>
  );
}

export default App;