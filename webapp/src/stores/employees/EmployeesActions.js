import axios from 'axios';

export const GET_ALL_EMPLOYEES = 'EmployeesActions.GET_ALL';
export const GET_ALL_EMPLOYEES_REQUEST = 'EmployeesActions.GET_ALL_REQUEST';
export const GET_ALL_EMPLOYEES_SUCCESS = 'EmployeesActions.GET_ALL_SUCCESS';
export const GET_ALL_EMPLOYEES_FAILURE = 'EmployeesActions.GET_ALL_FAILURE';

export const GET_EMPLOYEE = 'EmployeesActions.GET';
export const GET_EMPLOYEE_REQUEST = 'EmployeesActions.GET_REQUEST';
export const GET_EMPLOYEE_SUCCESS = 'EmployeesActions.GET_SUCCESS';
export const GET_EMPLOYEE_FAILURE = 'EmployeesActions.GET_FAILURE';

export function getAllEmployees() {
  return dispatch => {
    dispatch({ type: GET_ALL_EMPLOYEES_REQUEST });
    axios
      .get('/api/v1/employees/')
      .then(({data}) => {
        dispatch({ type: GET_ALL_EMPLOYEES_SUCCESS, payload: data});
      })
      .catch(({data}) => {
        dispatch({ type: GET_ALL_EMPLOYEES_FAILURE, payload: data, error: true});
      });
  };
}

export function getEmployee(employeeId) {
  return dispatch => {
    dispatch({ type: GET_EMPLOYEE });
    axios
      .get(`/api/v1/employees/${employeeId}`)
      .then(({data}) => {
        dispatch({ type: GET_EMPLOYEE_SUCCESS, payload: data });
      })
      .catch(({data}) => {
        dispatch({ type: GET_EMPLOYEE_FAILURE, payload: data, error: true });
      });
  }
}
