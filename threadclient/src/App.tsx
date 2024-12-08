import { PrimeReactProvider } from 'primereact/api';
import { Button } from 'primereact/button';
import './common/styles/main.css';
import './App.css';

function App() {
  return (
    <PrimeReactProvider>
      <div className="flex items-center gap-2 ">
        <Button label="Primary" />
        <Button label="Secondary" severity="secondary" />
        <Button label="Success" severity="success" />
        <Button label="Info" severity="info" />
        <Button label="Warning" severity="warning" />
        <Button label="Help" severity="help" />
        <Button label="Danger" severity="danger" />
      </div>
    </PrimeReactProvider>
  );
}

export default App;
