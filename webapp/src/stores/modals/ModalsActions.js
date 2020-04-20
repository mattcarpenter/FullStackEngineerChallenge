export const SHOW_MODAL = 'ModalsActions.SHOW_MODAL';
export const HIDE_MODAL = 'ModalsActions.HIDE_MODAL';

export function showModal(modalName) {
  return dispatch => {
    dispatch({ type: SHOW_MODAL, payload: modalName });
  };
}

export function hideModal(modalName) {
  return dispatch => {
    dispatch({ type: HIDE_MODAL, payload: modalName });
  };
}
