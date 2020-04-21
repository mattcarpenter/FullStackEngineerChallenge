import axios from 'axios';
import { replace } from 'connected-react-router';
import { toastr } from 'react-redux-toastr';
import * as routes from '../../constants/Routes';

export const LOGIN = 'UserActions.LOGIN';
export const LOGIN_REQUEST = 'UserActions.LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'UserActions.LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'UserActions.LOGIN_FAILURE';

export const LOGOUT = 'UserActions.LOGOUT';
export const LOGOUT_REQUEST = 'UserActions.LOGOUT_REQUEST';
export const LOGOUT_SUCCESS = 'UserActions.LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'UserActions.LOGOUT_FAILURE';

export function login(emailAddress, password) {
  return dispatch => {
    dispatch({ type: LOGIN_REQUEST });
    axios
      .post('/api/v1/auth/login', {
        emailAddress: emailAddress,
        password: password
      })
      .then(({data}) => {
        dispatch({ type: LOGIN_SUCCESS, payload: data});
        dispatch(replace(routes.HOME));
      })
      .catch(({response : { data }}) => {
        toastr.error(data?.code === 'AUTHORIZATION_CREDENTIALS' ?
          'Could not authorize the provided credentials' : 'An unexpected error occurred');
        dispatch({ type: LOGIN_FAILURE, payload: data, error: true});
      });
  };
}

export function logout() {
  return dispatch => {
    dispatch({ type: LOGOUT_REQUEST });
    axios
      .post('/api/v1/auth/logout')
      .then(() => {
        dispatch({ type: LOGOUT_SUCCESS });
        dispatch(replace(routes.LOGIN));
        toastr.success('Logged out');
      })
      .catch(() => {
        dispatch({ type: LOGOUT_FAILURE });
        dispatch(replace(routes.HOME));
        toastr.error('Error logging out. Please clear your cache and cookies.');
      });
  };
}
