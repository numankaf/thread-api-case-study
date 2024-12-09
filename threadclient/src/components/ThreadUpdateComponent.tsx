import { Form, Formik } from 'formik';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { InputNumber } from 'primereact/inputnumber';
import { ReactNode } from 'react';
import * as Yup from 'yup';
import useThreadApi from '../hooks/api/useThreadApi';
import { useToast } from '../hooks/useToast';
import { Thread } from '../types/thread';

interface Props {
  isVisible: boolean;
  setIsVisible(val: boolean): void;
  loadData(): void;
  thread: Thread;
}
const ThreadUpdateComponent = ({
  thread,
  isVisible,
  setIsVisible,
  loadData,
}: Props) => {
  const { updateThread } = useThreadApi();
  const { showSuccessToast, showErrorToast } = useToast();

  const updateValidation = Yup.object().shape({
    priority: Yup.number()
      .required('Thread Priority Required.')
      .min(1, 'Thread Priority should be at least 1.')
      .max(10, 'Thread Priority should be less then 100.'),
  });

  return (
    <Dialog
      header={`Update Thread : Thread ${thread.id}`}
      visible={isVisible}
      draggable={false}
      style={{ width: '25vw' }}
      breakpoints={{ '960px': '75vw', '641px': '100vw' }}
      onHide={() => setIsVisible(false)}
    >
      <Formik
        initialValues={{
          priority: thread.priority,
        }}
        validationSchema={updateValidation}
        onSubmit={async (values, { setSubmitting }) => {
          setSubmitting(true);
          updateThread(thread.id, {
            priority: values.priority,
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
              <label className="mb-2">Thread Priority</label>
              <InputNumber
                id="priority"
                name="priority"
                className="w-full"
                placeholder="Enter a priority "
                onValueChange={handleChange}
                onBlur={handleBlur}
                value={values.priority}
                min={1}
                max={10}
              />
              <span className={'text-red-400 m-1'}>
                {errors.priority &&
                  touched.priority &&
                  (errors.priority as ReactNode)}
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

export default ThreadUpdateComponent;
