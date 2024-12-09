import { LogType } from '../enums/log-type.enum';

export const LogColorMap: Record<LogType, string> = {
  [LogType.PRODUCE_MESSAGE]: 'text-yellow-500',
  [LogType.CONSUME_MESSAGE]: 'text-teal-500',
};
