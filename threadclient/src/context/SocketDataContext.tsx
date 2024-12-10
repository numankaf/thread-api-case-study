import { createContext, useState } from 'react';
import { useSubscription } from 'react-stomp-hooks';
import { LogMessage } from '../types/log';
import { QueueStatistics } from '../types/queue';

interface SocketDataContextType {
  logMessages: LogMessage[];
  queueStatistics: QueueStatistics | undefined;
}
interface Props {
  children: React.ReactNode;
}

export const SocketDataContext = createContext<
  SocketDataContextType | undefined
>(undefined);
export const SocketDataProvider = ({ children }: Props) => {
  const MAX_LOGS = 1000;

  const [logMessages, setLogMessages] = useState<LogMessage[]>([]);
  const [queueStatistics, setQueueStatistics] = useState<
    QueueStatistics | undefined
  >(undefined);
  useSubscription('/topic/queueLog', (message) => {
    const newMessage = JSON.parse(message.body);

    setLogMessages((prevMessages) => {
      const updatedMessages = [...prevMessages, newMessage];

      if (updatedMessages.length > MAX_LOGS) {
        return updatedMessages.slice(updatedMessages.length - MAX_LOGS);
      }

      return updatedMessages;
    });
  });
  useSubscription('/topic/queueStatistics', (message) => {
    setQueueStatistics(JSON.parse(message.body));
  });
  return (
    <SocketDataContext.Provider value={{ logMessages, queueStatistics }}>
      <>{children}</>
    </SocketDataContext.Provider>
  );
};
