import React from 'react';
import { useSelector } from 'react-redux';
import { Redirect, Route } from 'react-router-dom';
import * as Routes from '../constants/Routes';

export default function LoginRequiredRoute({ component: Component, ...rest }) {
  const isLoggedIn = useSelector(state => !!state.user);
  return (
    <Route {...rest} render={props => (
      isLoggedIn ? (
        <Component {...props} />
      ) : (
        <Redirect to={{
          pathname: Routes.LOGIN,
          state: { from: props.location }
        }} />
      )
    )} />
  );
}
