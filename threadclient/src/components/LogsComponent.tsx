import { useEffect, useRef } from 'react';
import { LogColorMap } from '../constants/log-color-map';
import { useSocketData } from '../hooks/useSocketData';

const LogsComponent = () => {
  const { logMessages } = useSocketData();
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'instant' });
    }
  };

  useEffect(() => {
    scrollToBottom();
  }, [logMessages]);

  return (
    <div>
      <div className="col-span-2 font-bold text-lg text-primary">
        Thread Logs
      </div>
      <div
        className="terminal overflow-y-scroll"
        style={{ padding: '10px', height: 'calc(100vh - 150px)' }}
      >
        {logMessages.map((logMessage, index) => (
          <div
            key={index}
            className={`${LogColorMap[logMessage.type]}`}
            style={{ marginBottom: '5px' }}
          >
            {logMessage.message}
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>
    </div>
  );
};

export default LogsComponent;
