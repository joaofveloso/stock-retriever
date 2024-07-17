import React from 'react';

interface TabsProps {
    activeTab: 'stockUpdate' | 'stockDetails';
    setActiveTab: React.Dispatch<React.SetStateAction<'stockUpdate' | 'stockDetails'>>;
}

const Tabs: React.FC<TabsProps> = ({ activeTab, setActiveTab }) => {
    return (
        <div className="tabs">
            <button className={`tab-button ${activeTab === 'stockUpdate' ? 'active' : ''}`} onClick={() => setActiveTab('stockUpdate')}>
                Stock Update
            </button>
            <button className={`tab-button ${activeTab === 'stockDetails' ? 'active' : ''}`} onClick={() => setActiveTab('stockDetails')}>
                Stock Details
            </button>
        </div>
    );
}

export default Tabs;