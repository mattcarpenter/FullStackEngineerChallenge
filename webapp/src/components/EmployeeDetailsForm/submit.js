import { SubmissionError } from 'redux-form'
import { hideModal } from '../../stores/modals/ModalsActions';
import { GET_EMPLOYEE_SUCCESS } from '../../stores/employees/EmployeesActions';
import ErrorCodes from '../../constants/ErrorCodes';
import axios from 'axios';
import { CREATE_EMPLOYEE_MODAL, UPDATE_EMPLOYEE_MODAL } from '../../constants/Modals';

const sleep = ms => new Promise(resolve => setTimeout(resolve, ms))

export function submitUpdate(values, dispatch) {
  const payload = {
    firstName: values.firstName || null,
    lastName: values.lastName || null,
    emailAddress: values.emailAddress || null,
    password: values.password || null
  };

  Object.keys(payload).forEach(k => {
    if (payload[k] === null) {
      delete payload[k];
    }
  });

  return axios
    .post('/api/v1/employees/' + values.id, payload)
    .then(({data}) => {
      dispatch({ type: GET_EMPLOYEE_SUCCESS, payload: data });
      dispatch(hideModal(UPDATE_EMPLOYEE_MODAL));
    })
    .catch(({response : { data }}) => {
      if (data.code === ErrorCodes.EMAIL_ALREADY_EXISTS) {
        throw new SubmissionError({
          'emailAddress': data.message,
          _error: 'Creation failed'
        });
      }
    });
}

export function submitCreate(values, dispatch) {
  return axios
      .post('/api/v1/employees/', values)
      .then(({data}) => {
        dispatch(hideModal(CREATE_EMPLOYEE_MODAL));
      }).catch(({response : { data }}) => {
        if (data.code === ErrorCodes.EMAIL_ALREADY_EXISTS) {
          throw new SubmissionError({
            'emailAddress': data.message,
            _error: 'Creation failed'
          });
        }
      });
}
