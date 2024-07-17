import React, { useState } from 'react';
import { Tooltip } from '@mui/material';
import { CheckCircle } from '@mui/icons-material';

interface Stock {
    symbol: string;
    name: string;
    image: string;
    isSelected: boolean;
}

interface CarouselProps {
    toggleStock: Stock[];
    stockSelection: (symbol: string) => void;
}

const Carousel: React.FC<CarouselProps> = ({ toggleStock, stockSelection }) => {
    const [scrollInterval, setScrollInterval] = useState<NodeJS.Timeout | null>(null);

    const scroll = (direction: number) => {
        const track = document.querySelector('.carousel-track') as HTMLElement;
        if (track) {
            const itemWidth = (document.querySelector('.carousel-item') as HTMLElement)?.offsetWidth + 10 || 130;
            const scrollAmount = direction * itemWidth;
            track.scrollBy({ left: scrollAmount, behavior: 'smooth' });
        }
    };

    const startScrolling = (direction: number) => {
        stopScrolling();
        const interval = setInterval(() => scroll(direction), 100);
        setScrollInterval(interval);
    }

    const stopScrolling = () => {
        if (scrollInterval) {
            clearInterval(scrollInterval);
            setScrollInterval(null);
        }
    }

    return (
        <div className="carousel">
            <div className="carousel-track">
                {toggleStock.map(stock => (
                    <div key={stock.symbol} className="carousel-item">
                        <Tooltip title={stock.name}>
                            <button style={{ maxWidth: '125px' }} onClick={() => stockSelection(stock.symbol)}>
                                <img
                                    src={stock.image}
                                    alt={`Stock ${stock.name}`}
                                    style={{
                                        opacity: stock.isSelected ? 0.5 : 1,
                                        width: '100px',
                                        height: '100px'
                                    }} />
                                {stock.isSelected && (
                                    <CheckCircle
                                        style={{
                                            position: 'relative',
                                            bottom: '5px',
                                            right: '30px',
                                            color: 'blue',
                                            backgroundColor: 'white',
                                            borderRadius: '50%'
                                        }}
                                    />
                                )}
                            </button>
                        </Tooltip>
                    </div>
                ))}
            </div>
            <div className="carousel-buttons">
                <button
                    className="prev"
                    onMouseEnter={() => startScrolling(-1)}
                    onMouseLeave={stopScrolling}>Prev
                </button>
                <button
                    className="next"
                    onMouseEnter={() => startScrolling(1)}
                    onMouseLeave={stopScrolling}>Next
                </button>
            </div>
        </div>
    );
}

export default Carousel;