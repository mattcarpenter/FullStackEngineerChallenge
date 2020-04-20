import { LOGIN_SUCCESS } from './UserActions';

export default function userReducer(state = null, action) {
  switch (action.type) {
    case LOGIN_SUCCESS:
      state = action.payload;
      break;
    default:
      return state;
  }
  return state;
}
