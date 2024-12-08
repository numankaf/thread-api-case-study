import { axiosBase } from '../../api/axios';
import { endpoints } from '../../constants/endpoints';
import { HttpResponse } from '../../types/http-response';
import { Thread, ThreadCreateDto, ThreadUpdateDto } from '../../types/thread';

const useThreadApi = () => {
  const createThreads = async (dto: ThreadCreateDto): Promise<HttpResponse> => {
    return await axiosBase.post(endpoints.thread.create, dto).then((res) => {
      return res.data;
    });
  };

  const updateThread = async (
    id: number,
    dto: ThreadUpdateDto,
  ): Promise<HttpResponse> => {
    return await axiosBase
      .patch(`${endpoints.thread.update}${id}`, dto)
      .then((res) => {
        return res.data;
      });
  };

  const deleteThread = async (id: number): Promise<void> => {
    return await axiosBase
      .delete(`${endpoints.thread.delete}${id}`)
      .then((res) => {
        return res.data;
      });
  };

  const findAllThreads = async (): Promise<Thread[]> => {
    return await axiosBase.get(`${endpoints.thread.findall}`).then((res) => {
      return res.data;
    });
  };
  return {
    createThreads,
    updateThread,
    deleteThread,
    findAllThreads,
  };
};

export default useThreadApi;
