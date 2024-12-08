import { useEffect, useState } from 'react';
import useThreadApi from '../hooks/api/useThreadApi';
import { Thread } from '../types/thread';

const DashboardComponent = () => {
  const { findAllThreads } = useThreadApi();
  const [threads, setThreads] = useState<Thread[]>([]);

  const fetchThreads = () => {
    findAllThreads()
      .then((data) => {
        setThreads(data);
      })
      .catch((e) => {
        console.log(e.response.data.message);
      });
  };

  useEffect(() => {
    fetchThreads();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return <div>{JSON.stringify(threads)}</div>;
};

export default DashboardComponent;
