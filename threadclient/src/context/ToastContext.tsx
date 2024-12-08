import { Toast } from 'primereact/toast';
import React, { createContext, useRef } from 'react';

interface ToastContextType {
  showSuccessToast: (message: string, life?: number) => void;
  showWarningToast: (message: string, life?: number) => void;
  showErrorToast: (message: string, life?: number) => void;
}
interface Props {
  children: React.ReactNode;
}

export const ToastContext = createContext<ToastContextType | undefined>(
  undefined,
);

export const ToastProvider = ({ children }: Props) => {
  const toastRef = useRef<Toast>(null);

  const showToast = (
    severity: 'error' | 'success' | 'info' | 'warn',
    summary: string,
    detail: string,
    life: number = 3000,
  ) => {
    if (toastRef.current) {
      toastRef.current.show({
        severity: severity,
        summary: summary,
        detail: detail,
        life: life,
      });
    }
  };

  const showSuccessToast = (detail: string, life = 2000) => {
    showToast('success', 'Success', detail, life);
  };

  const showWarningToast = (detail: string, life = 2000) => {
    showToast('warn', 'Warning', detail, life);
  };
  const showErrorToast = (detail: string, life = 5000) => {
    showToast('error', 'Error', detail, life);
  };

  return (
    <ToastContext.Provider
      value={{ showSuccessToast, showWarningToast, showErrorToast }}
    >
      <>
        <Toast ref={toastRef} />
        {children}
      </>
    </ToastContext.Provider>
  );
};
