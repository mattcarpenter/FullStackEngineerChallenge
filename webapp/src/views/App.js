import React from 'react';
import { ConnectedRouter } from 'connected-react-router';
import { Route, Switch } from 'react-router-dom';
import CssBaseline from '@material-ui/core/CssBaseline';
import RouteEnum from '../constants/RouteEnum';
import LoginPage from './login-page/LoginPage';
import AdminApp from './admin-app/App';
import LoginRequiredRoute from '../components/LoginRequiredRoute';

export default function App(props) {
  return (
    <ConnectedRouter history={props.history}>
      <CssBaseline />
      <Switch>
        <Route path={RouteEnum.Login} component={LoginPage} />
        <LoginRequiredRoute component={AdminApp} />
      </Switch>
    </ConnectedRouter>
  );
}