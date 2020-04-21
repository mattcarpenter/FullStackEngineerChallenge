import { createBrowserHistory } from 'history';
import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import ReduxToastr from 'react-redux-toastr';
import 'react-redux-toastr/lib/css/react-redux-toastr.min.css';
import { connectStoreToLocalStorage, getUserFromPersistentStorage } from './services/AuthService';
import rootStore from './stores/rootStore';
import App from './views/App';

(async (window) => {
  const initialState = { user: getUserFromPersistentStorage() };
  const history = createBrowserHistory({ basename: '/' });
  const store = rootStore(initialState, history);

  // listens to auth changes in the store and persists to localStorage
  connectStoreToLocalStorage(store);
  
  const rootEl = document.getElementById('root');
  const render = (Component, el) => {
    ReactDOM.render(
      <Provider store={store}>
        <Component history={history} dispatch={store.dispatch} />
        <ReduxToastr
          imeOut={4000}
          newestOnTop={false}
          preventDuplicates
          position="bottom-center"
          getState={(state) => state.toastr}
          transitionIn="fadeIn"
          transitionOut="fadeOut"
          progressBar
          closeOnToastrClick/>
      </Provider>,
      document.getElementById('root')
    );
  };

  render(App, rootEl);
})(window);
