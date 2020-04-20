import React from 'react';
import ReactDOM from 'react-dom';
import App from './views/App';
import { Provider } from 'react-redux';
import { createBrowserHistory } from 'history';
import rootStore from './stores/rootStore';
import { getUserFromPersistentStorage, persistAuthChanges } from './services/AuthService';

(async (window) => {
  const initialState = { user: getUserFromPersistentStorage() };
  const history = createBrowserHistory({ basename: '/' });
  const store = rootStore(initialState, history);
  persistAuthChanges(store);
  const rootEl = document.getElementById('root');
  const render = (Component, el) => {
    ReactDOM.render(
      <Provider store={store}>
        <Component history={history} dispatch={store.dispatch} />
      </Provider>,
      document.getElementById('root')
    );
  };

  render(App, rootEl);
})(window);
