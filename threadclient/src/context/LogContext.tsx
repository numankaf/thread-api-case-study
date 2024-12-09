import { createContext, useState } from 'react';
import { useSubscription } from 'react-stomp-hooks';
import { LogMessage } from '../types/log';

interface LogContextType {
  logMessages: LogMessage[];
}
interface Props {
  children: React.ReactNode;
}

export const LogContext = createContext<LogContextType | undefined>(undefined);
export const LogProvider = ({ children }: Props) => {
  const MAX_LOGS = 10000;

  const [logMessages, setLogMessages] = useState<LogMessage[]>([]);
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
  return (
    <LogContext.Provider value={{ logMessages }}>
      <>{children}</>
    </LogContext.Provider>
  );
};
