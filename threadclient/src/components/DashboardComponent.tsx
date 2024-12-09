import { Button } from 'primereact/button';
import { DataView } from 'primereact/dataview';
import { ScrollPanel } from 'primereact/scrollpanel';
import { Tag } from 'primereact/tag';
import { useEffect, useState } from 'react';
import { ThreadType } from '../enums/thread-type.enum';
import useThreadApi from '../hooks/api/useThreadApi';
import { Thread, ThreadsStatus } from '../types/thread';
import { formatDate } from '../utils/date-formatter';
import ThreadCreateComponent from './ThreadCreateComponent';

const DashboardComponent = () => {
  const { findAllThreads } = useThreadApi();
  const [threadsStatus, setThreadsStatus] = useState<ThreadsStatus>();
  const [createVisible, setCreateVisible] = useState<boolean>(false);
  const fetchThreads = async () => {
    await findAllThreads()
      .then((data) => {
        const senderThreads = data.filter(
          (thread) => thread.type === ThreadType.SENDER,
        );
        const receiverThreads = data.filter(
          (thread) => thread.type === ThreadType.RECEIVER,
        );
        const activeSenderThreads = senderThreads.filter(
          (thread) => thread.isActive,
        ).length;
        const activeReceiverThreads = receiverThreads.filter(
          (thread) => thread.isActive,
        ).length;
        setThreadsStatus({
          senderThreads: senderThreads,
          receiverThreads: receiverThreads,
          activeSenderThreads: activeSenderThreads,
          activeReceiverThreads: activeReceiverThreads,
          passiveSenderThreads: senderThreads.length - activeSenderThreads,
          passiveReceiverThreads:
            receiverThreads.length - activeReceiverThreads,
        });
      })
      .catch((e) => {
        console.log(e.response.data.message);
      });
  };

  useEffect(() => {
    fetchThreads();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const threadItemTemplate = (thread: Thread) => {
    return (
      <div className="surface-hover rounded-md p-3 space-y-2" key={thread.id}>
        <div className="flex items-center justify-between">
          <div className="font-semibold text-lg"> Thread {thread.id}</div>
          <Tag
            value={thread.isActive ? 'ACTIVE' : 'PASSIVE'}
            severity={thread.isActive ? 'success' : 'danger'}
          ></Tag>
        </div>
        <div className="flex items-center gap-2">
          <div> Priority : </div>
          {thread.priority}
        </div>
        <div className="flex items-center gap-2">
          <div>Type: </div>
          <div
            className={`p-1 rounded-md text-white text-center ${
              thread.type === ThreadType.RECEIVER
                ? 'bg-teal-500'
                : 'bg-yellow-500'
            }`}
          >
            {thread.type}
          </div>
        </div>
        <div>Oluşturulma Tarihi : {formatDate(thread.createdDate)}</div>
        <div>Güncellenme Tarihi: {formatDate(thread.lastModifiedDate)}</div>
      </div>
    );
  };

  const listTemplate = (items: Thread[]) => {
    if (!items || items.length === 0) return null;

    const list = items.map((thread) => {
      return threadItemTemplate(thread);
    });

    return <div className="grid grid-cols-3 gap-4">{list}</div>;
  };

  return (
    <>
      <ThreadCreateComponent
        isVisible={createVisible}
        setIsVisible={setCreateVisible}
        loadData={fetchThreads}
      ></ThreadCreateComponent>
      <div className="grid grid-cols-2 gap-x-4 ">
        <div className="col-span-2 font-bold text-lg text-primary">
          Queue Status
        </div>
        <div className="card col-span-2" style={{ height: '10vh' }}></div>
        <div className="col-span-2 flex items-center justify-between pb-2">
          <div className="font-bold text-lg text-primary">Threads</div>
          <Button
            label="Create Threads"
            icon="pi pi-plus"
            onClick={() => setCreateVisible(true)}
          ></Button>
        </div>
        {threadsStatus && (
          <div className="card">
            <div className="flex items-center justify-between">
              <div className="pb-4 font-semibold text-lg text-yellow-500">
                Sender Threads
              </div>
              <div className="flex gap-2">
                <div className="flex items-center text-lg gap-1 text-green-500">
                  {threadsStatus.activeSenderThreads}
                  <div className="rounded-full bg-green-500 w-5 h-5"></div>
                </div>
                <div className="flex items-center text-lg gap-1 text-red-500">
                  {threadsStatus.passiveSenderThreads}
                  <div className="rounded-full bg-red-500 w-5 h-5"></div>
                </div>
              </div>
            </div>
            <ScrollPanel className="customscrollbar" style={{ height: '65vh' }}>
              <DataView
                value={threadsStatus.senderThreads}
                listTemplate={listTemplate}
              ></DataView>
            </ScrollPanel>
          </div>
        )}
        {threadsStatus && (
          <div className="card ">
            <div className="flex items-center justify-between">
              <div className="pb-4 font-semibold text-lg text-teal-500">
                Receiver Threads
              </div>
              <div className="flex gap-2">
                <div className="flex items-center text-lg gap-1 text-green-500">
                  {threadsStatus.activeReceiverThreads}
                  <div className="rounded-full bg-green-500 w-5 h-5"></div>
                </div>
                <div className="flex items-center text-lg gap-1 text-red-500">
                  {threadsStatus.passiveReceiverThreads}
                  <div className="rounded-full bg-red-500 w-5 h-5"></div>
                </div>
              </div>
            </div>
            <ScrollPanel className="customscrollbar" style={{ height: '65vh' }}>
              <DataView
                value={threadsStatus.receiverThreads}
                listTemplate={listTemplate}
              ></DataView>
            </ScrollPanel>
          </div>
        )}
      </div>
    </>
  );
};

export default DashboardComponent;
