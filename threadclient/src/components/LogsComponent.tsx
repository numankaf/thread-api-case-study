import { useEffect, useRef } from 'react';
import { LogColorMap } from '../constants/log-color-map';
import { useLogs } from '../hooks/useLogs';

const LogsComponent = () => {
  const { logMessages } = useLogs();
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
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
        className="terminal h-[600px] overflow-y-scroll"
        style={{ padding: '10px' }}
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
