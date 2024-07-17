import React from 'react';
import DatePicker from "react-datepicker";
import 'react-datepicker/dist/react-datepicker.css';

interface DatePickerComponentProps {
    selectedDate: Date | null;
    setSelectedDate: (date: Date | null) => void; //React.Dispatch<React.SetStateAction<Date | null>>;
    selectionType: 'daily' | 'weekly';
}

const DatePickerComponent: React.FC<DatePickerComponentProps> = ({ selectedDate, setSelectedDate, selectionType}) => {
    return (
        <DatePicker
            selected={selectedDate}
            onChange={(date: Date | null) => setSelectedDate(date)}
            dateFormat="MM-dd-yyyy"
            className="custom-date-picker"
            showWeekNumbers={selectionType === 'weekly'}
            showWeekPicker={selectionType === 'weekly'}
        />
    );
}

export default DatePickerComponent;