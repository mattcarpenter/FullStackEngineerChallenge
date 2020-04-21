import { LOGIN_SUCCESS, LOGOUT_SUCCESS } from './UserActions';

export default function userReducer(state = null, action) {
  switch (action.type) {
    case LOGIN_SUCCESS:
      state = action.payload;
      break;
    case LOGOUT_SUCCESS:
      state = null;
      break;
    default:
      return state;
  }
  return state;
}
