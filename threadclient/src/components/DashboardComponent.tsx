import { Button } from 'primereact/button';
import { confirmDialog, ConfirmDialog } from 'primereact/confirmdialog';
import { DataView } from 'primereact/dataview';
import { Knob } from 'primereact/knob';
import { ScrollPanel } from 'primereact/scrollpanel';
import { Tag } from 'primereact/tag';
import { useEffect, useState } from 'react';
import { LogColorMap } from '../constants/log-color-map';
import { LogType } from '../enums/log-type.enum';
import { ThreadType } from '../enums/thread-type.enum';
import useThreadApi from '../hooks/api/useThreadApi';
import { useSocketData } from '../hooks/useSocketData';
import { useToast } from '../hooks/useToast';
import { Thread, ThreadsStatus } from '../types/thread';
import { formatDate } from '../utils/date-formatter';
import ThreadCreateComponent from './ThreadCreateComponent';
import ThreadUpdateComponent from './ThreadUpdateComponent';

const DashboardComponent = () => {
  const { showSuccessToast, showErrorToast } = useToast();
  const { queueStatistics } = useSocketData();
  const { findAllThreads, updateThread, deleteThread } = useThreadApi();
  const [threadsStatus, setThreadsStatus] = useState<ThreadsStatus>();
  const [createVisible, setCreateVisible] = useState<boolean>(false);
  const [updateVisible, setUpdateVisible] = useState<boolean>(false);
  const [selectedTread, setSelectedThread] = useState<Thread | undefined>(
    undefined,
  );

  const changeThreadStatus = async (id: number, isActive: boolean) => {
    await updateThread(id, { isActive: isActive })
      .then((data) => {
        showSuccessToast(data.message);
        fetchThreads();
      })
      .catch((e) => {
        showErrorToast(e.response.data.message);
      });
  };

  const deleteThreadById = async (id: number) => {
    await deleteThread(id)
      .then(() => {
        showSuccessToast('Thread is deleted.');
        fetchThreads();
      })
      .catch((e) => {
        showErrorToast(e.response.data.message);
      });
  };

  const confirmDeleteThread = (id: number) => {
    confirmDialog({
      message: 'Do you want to delete this thread?',
      header: 'Thread Delete Confirmation',
      icon: 'pi pi-exclamation-triangle',
      defaultFocus: 'accept',
      accept: () => deleteThreadById(id),
    });
  };

  const confirmStartThread = (id: number) => {
    confirmDialog({
      message: 'Are you sure you want to start this thread?',
      header: 'Thread Start Confirmation',
      icon: 'pi pi-exclamation-triangle',
      defaultFocus: 'accept',
      accept: () => changeThreadStatus(id, true),
    });
  };

  const confirmStopThread = (id: number) => {
    confirmDialog({
      message: 'Are you sure you want to stop this thread?',
      header: 'Thread Stop Confirmation',
      icon: 'pi pi-info-circle',
      defaultFocus: 'reject',
      acceptClassName: 'p-button-danger',
      accept: () => changeThreadStatus(id, false),
    });
  };
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
          <div className="flex gap-2">
            {thread.isActive && (
              <Button
                icon="pi pi-pencil"
                severity="info"
                onClick={() => {
                  setSelectedThread(thread);
                  setUpdateVisible(true);
                }}
              ></Button>
            )}
            {!thread.isActive && (
              <Button
                icon="pi pi-play-circle"
                severity="success"
                onClick={() => {
                  confirmStartThread(thread.id);
                }}
              ></Button>
            )}
            {thread.isActive && (
              <Button
                icon="pi pi-stop-circle"
                severity="warning"
                onClick={() => {
                  confirmStopThread(thread.id);
                }}
              ></Button>
            )}
            <Button
              icon="pi pi-trash"
              severity="danger"
              onClick={() => confirmDeleteThread(thread.id)}
            ></Button>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <div> Status : </div>
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
        <div>Created At : {formatDate(thread.createdDate)}</div>
        <div>Last Modified At: {formatDate(thread.lastModifiedDate)}</div>
      </div>
    );
  };

  const listTemplate = (items: Thread[]) => {
    if (!items || items.length === 0) return null;

    const list = items.map((thread) => {
      return threadItemTemplate(thread);
    });

    return <div className="grid grid-cols-2 gap-4">{list}</div>;
  };

  return (
    <>
      <ConfirmDialog />

      <ThreadCreateComponent
        isVisible={createVisible}
        setIsVisible={setCreateVisible}
        loadData={fetchThreads}
      ></ThreadCreateComponent>
      {selectedTread && (
        <ThreadUpdateComponent
          isVisible={updateVisible}
          setIsVisible={setUpdateVisible}
          thread={selectedTread}
          loadData={fetchThreads}
        ></ThreadUpdateComponent>
      )}
      <div className="grid grid-cols-2 gap-x-4 ">
        <div className="col-span-2 font-bold text-lg text-primary">
          Queue Status
        </div>
        <div className="card col-span-2">
          {queueStatistics && (
            <>
              <div className="flex gap-3 items-center">
                <div className="flex flex-col items">
                  <div className="text-primary text-lg font-semibold">
                    Queue Capacity
                  </div>
                  <Knob
                    value={queueStatistics.remaining}
                    size={200}
                    max={queueStatistics.capacity}
                    strokeWidth={4}
                    textColor="var(--primary-color)"
                    valueTemplate={`${queueStatistics.remaining}`}
                  />
                </div>
                <div className="grid grid-cols-5 w-full gap-5">
                  <div className="flex flex-col gap-2 text-primary surface-hover p-3 rounded-md">
                    <div className="uppercase text-lg">Current</div>
                    <div className="text-3xl text-green-500">
                      {queueStatistics.currentSize}
                    </div>
                  </div>
                  <div className="flex flex-col gap-2  text-primary surface-hover p-3 rounded-md">
                    <div className="uppercase text-lg">Remaining</div>
                    <div className="text-3xl text-red-500">
                      {queueStatistics.remaining}
                    </div>
                  </div>
                  <div className="flex flex-col gap-2 text-primary surface-hover p-3 rounded-md">
                    <div className="uppercase text-lg">Capacity</div>
                    <div className="text-3xl text-blue-500">
                      {queueStatistics.capacity}
                    </div>
                  </div>
                  <div className="flex flex-col gap-2 text-primary surface-hover p-3 rounded-md">
                    <div className="uppercase text-lg">Total Produced</div>
                    <div
                      className={`${
                        LogColorMap[LogType.PRODUCE_MESSAGE]
                      } text-3xl`}
                    >
                      {queueStatistics.totalProduced}
                    </div>
                  </div>
                  <div className="flex flex-col gap-2 text-primary surface-hover p-3 rounded-md">
                    <div className="uppercase text-lg">Total Consumed</div>
                    <div
                      className={`${
                        LogColorMap[LogType.CONSUME_MESSAGE]
                      } text-3xl`}
                    >
                      {queueStatistics.totalConsumed}
                    </div>
                  </div>
                </div>
              </div>
            </>
          )}
        </div>
        <div className="col-span-2 flex items-center justify-between pb-2">
          <div className="font-bold text-lg text-primary">Threads</div>
          <Button
            label="Create Threads"
            icon="pi pi-plus"
            onClick={() => setCreateVisible(true)}
          ></Button>
        </div>
        {threadsStatus && (
          <div className="card h-full">
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
            <ScrollPanel style={{ height: '600px' }}>
              <DataView
                value={threadsStatus.senderThreads}
                listTemplate={listTemplate}
              ></DataView>
            </ScrollPanel>
          </div>
        )}
        {threadsStatus && (
          <div className="card h-full ">
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
            <ScrollPanel style={{ height: '600px' }}>
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
