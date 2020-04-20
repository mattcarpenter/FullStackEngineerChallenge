import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';
import { reducer as formReducer } from 'redux-form';
import UserReducer from './user/UserReducer';
import LoadingReducer from './loading/LoadingReducer';
import employeesReducer from './employees/EmployeesReducer';
import modalsReducer from './modals/ModalsReducer';

export default function rootReducer(history) {
  const reducerMap = {
    router: connectRouter(history),
    user: UserReducer,
    loading: LoadingReducer,
    employees: employeesReducer,
    form: formReducer,
    modals: modalsReducer
  };

  return combineReducers(reducerMap);
}
