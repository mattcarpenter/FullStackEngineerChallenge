import { SHOW_MODAL, HIDE_MODAL } from './ModalsActions';

export default function userReducer(state = {}, action) {
  switch (action.type) {
    case SHOW_MODAL:
      return { ...state, [action.payload]: true };
    case HIDE_MODAL: 
      return { ...state, [action.payload]: false };
    default:
      return state;
  }
}
