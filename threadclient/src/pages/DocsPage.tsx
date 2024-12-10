import SwaggerUI from 'swagger-ui-react';
import 'swagger-ui-react/swagger-ui.css';
const baseURL = import.meta.env.VITE_SERVER_URL;

const DocsPage = () => {
  return (
    <div className="card">
      <SwaggerUI supportedSubmitMethods={[]} url={`${baseURL}v3/api-docs`} />
    </div>
  );
};

export default DocsPage;
