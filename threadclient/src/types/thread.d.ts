import { ThreadType } from '../enums/thread-type.enum';

export class Thread {
  id: number;
  createdDate: string;
  lastModifiedDate: string;
  type: ThreadType;
  priority: number;
  isActive: boolean;
}

export class ThreadCreateDto {
  threadNumber: number;
  threadType: ThreadType;
}

export class ThreadUpdateDto {
  priority: number;
  isActive: boolean;
}
