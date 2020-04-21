import CssBaseline from '@material-ui/core/CssBaseline';
import { ConnectedRouter } from 'connected-react-router';
import React from 'react';
import { Route, Switch } from 'react-router-dom';
import LoginRequiredRoute from '../components/LoginRequiredRoute';
import * as Routes from '../constants/Routes';
import AdminApp from './admin/App';
import EmployeeApp from './employee-app/App';
import LoginPage from './login/LoginPage';
import { useSelector } from 'react-redux';
import LogoutPage from '../views/logout/LogoutPage';

export default function App(props) {
  const isAdmin = useSelector(state => state.user?.admin);

  return (
    <ConnectedRouter history={props.history}>
      <CssBaseline />
      <Switch>
        <Route path={Routes.LOGIN} component={LoginPage} />
        <Route path={Routes.LOGOUT} component={LogoutPage} />
        <LoginRequiredRoute component={ isAdmin ? AdminApp : EmployeeApp } />
      </Switch>
    </ConnectedRouter>
  );
}