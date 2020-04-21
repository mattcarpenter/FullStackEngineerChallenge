import axios from 'axios';
import { SubmissionError } from 'redux-form';
import { EMAIL_ALREADY_EXISTS } from '../../constants/ErrorCodes';
import { CREATE_EMPLOYEE_MODAL, UPDATE_EMPLOYEE_MODAL } from '../../constants/Modals';
import { GET_EMPLOYEE_SUCCESS } from '../../stores/employees/EmployeesActions';
import { hideModal } from '../../stores/modals/ModalsActions';
import { toastr } from 'react-redux-toastr';

export function submitUpdate(values, dispatch) {
  const payload = {
    firstName: values.firstName || null,
    lastName: values.lastName || null,
    emailAddress: values.emailAddress || null,
    password: values.password || null,
    admin: values.admin
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
      toastr.success('Update successful');
    })
    .catch(({response : { data }}) => {
      if (data.code === EMAIL_ALREADY_EXISTS) {
        throw new SubmissionError({
          'emailAddress': data.message,
          _error: 'Creation failed'
        });
      } else {
        toastr.error('An unknown error occurred');
      }
    });
}

export function submitCreate(values, dispatch) {
  return axios
      .post('/api/v1/employees/', values)
      .then(({data}) => {
        dispatch(hideModal(CREATE_EMPLOYEE_MODAL));
        toastr.success('Creation successful');
      }).catch(({response : { data }}) => {
        if (data.code === EMAIL_ALREADY_EXISTS) {
          throw new SubmissionError({
            'emailAddress': data.message,
            _error: 'Creation failed'
          });
        } else {
          toastr.error('An unknown error occurred');
        }
      });
}
