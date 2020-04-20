import { GET_ALL_EMPLOYEES_SUCCESS, GET_EMPLOYEE, GET_EMPLOYEE_SUCCESS } from './EmployeesActions';

const initialState = {
  employees: [],
  current: null
};

export default function employeeReducer(state = initialState, action) {
  switch (action.type) {
    case GET_ALL_EMPLOYEES_SUCCESS:
      state.employees = action.payload.employees;
      break;
    case GET_EMPLOYEE:
      state.current = null;
      break;
    case GET_EMPLOYEE_SUCCESS:
      state.current = action.payload;
      break;
    default:
      return state;
  }

  return state;
}
