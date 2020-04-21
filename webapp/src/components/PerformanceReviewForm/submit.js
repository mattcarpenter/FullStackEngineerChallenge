import axios from 'axios';
import { CREATE_PERFORMANCE_REVIEW_MODAL } from '../../constants/Modals';
import { hideModal } from '../../stores/modals/ModalsActions';
import { getEmployee } from '../../stores/employees/EmployeesActions';

export function submitCreate(values, dispatch) {
  const payload = {
    reviewee: values.employeeId,
    memo: values.memo
  };

  return axios
      .post('/api/v1/performance-reviews/', payload)
      .then(({data}) => {
        dispatch(getEmployee(values.employeeId));
        dispatch(hideModal(CREATE_PERFORMANCE_REVIEW_MODAL));
      }).catch(({response : { data }}) => {
        // not yet implemented
      });
}
