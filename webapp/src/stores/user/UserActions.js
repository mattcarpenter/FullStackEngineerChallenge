import axios from 'axios';
import { replace } from 'connected-react-router';

export const LOGIN = 'UserActions.LOGIN';
export const LOGIN_REQUEST = 'UserActions.LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'UserActions.LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'UserActions.LOGIN_FAILURE';

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
        dispatch(replace('/'));
      })
      .catch(({data}) => {
        setTimeout(() => {
          dispatch({ type: LOGIN_FAILURE, payload: data, error: true});
        }, 1000);
      });
  };
}
