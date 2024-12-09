import { Form, Formik } from 'formik';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { Dropdown } from 'primereact/dropdown';
import { InputNumber } from 'primereact/inputnumber';
import { ReactNode } from 'react';
import * as Yup from 'yup';
import { threadTypes } from '../constants/dropdown-constants';
import useThreadApi from '../hooks/api/useThreadApi';
import { useToast } from '../hooks/useToast';

interface Props {
  isVisible: boolean;
  setIsVisible(val: boolean): void;
  loadData(): void;
}
const createValidation = Yup.object().shape({
  threadNumber: Yup.number()
    .required('Thread Number Required.')
    .min(1, 'Thread number should be at least 1.')
    .max(100, 'Thread number should be less then 100.'),
  threadType: Yup.object().required('Thread Type Required.'),
});
const ThreadCreateComponent = ({
  isVisible,
  setIsVisible,
  loadData,
}: Props) => {
  const { createThreads } = useThreadApi();
  const { showSuccessToast, showErrorToast } = useToast();

  return (
    <Dialog
      header="Create Threads"
      visible={isVisible}
      draggable={false}
      style={{ width: '25vw' }}
      breakpoints={{ '960px': '75vw', '641px': '100vw' }}
      onHide={() => setIsVisible(false)}
    >
      <Formik
        initialValues={{
          threadNumber: 1,
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          threadType: null as any,
        }}
        validationSchema={createValidation}
        onSubmit={async (values, { setSubmitting }) => {
          setSubmitting(true);
          createThreads({
            threadNumber: values.threadNumber,
            threadType: values.threadType.name,
          })
            .then((data) => {
              showSuccessToast(data.message);
              loadData();
              setIsVisible(false);
            })
            .catch((e) => {
              showErrorToast(e.response.data.message);
            })
            .finally(() => {
              setSubmitting(false);
            });
        }}
      >
        {({
          values,
          errors,
          touched,
          handleChange,
          handleBlur,
          isSubmitting,
        }) => (
          <Form className="space-y-4 md:space-y-4">
            <div className="flex flex-col w-full">
              <label className="mb-2">Thread Number</label>
              <InputNumber
                id="threadNumber"
                name="threadNumber"
                className="w-full"
                placeholder="Enter a Thread Number"
                onValueChange={handleChange}
                onBlur={handleBlur}
                value={values.threadNumber}
              />
              <span className={'text-red-400 m-1'}>
                {errors.threadNumber &&
                  touched.threadNumber &&
                  (errors.threadNumber as ReactNode)}
              </span>
            </div>
            <div className="flex flex-col w-full">
              <label className="mb-2">Thread Type</label>
              <Dropdown
                id="threadType"
                name="threadType"
                options={threadTypes}
                optionLabel="name"
                className="w-full"
                placeholder="Select Thread Type"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.threadType}
                min={1}
                max={100}
              />
              <span className={'text-red-400 m-1'}>
                {errors.threadType &&
                  touched.threadType &&
                  (errors.threadType as ReactNode)}
              </span>
            </div>
            <div className="flex items-center justify-end gap-3">
              <Button
                severity="danger"
                type="button"
                outlined
                rounded
                label="Cancel"
                icon="pi pi-times"
                onClick={() => setIsVisible(false)}
              />
              <Button
                disabled={isSubmitting}
                rounded
                type="submit"
                severity="success"
                label="Save"
                icon="pi pi-check"
              />
            </div>
          </Form>
        )}
      </Formik>
    </Dialog>
  );
};

export default ThreadCreateComponent;
