import * as React from 'react';
import * as ReactDOM from 'react-dom';
import App from './App';
import registerServiceWorker from './utils/registerServiceWorker';
// import Raven from 'raven-js';
// Raven.config('http://e88cf3f1a9ec456895830ca3d71bb1c9@121.40.160.240:9000/2').install();

ReactDOM.render(<App />, document.getElementById('root') as HTMLElement);
registerServiceWorker();
