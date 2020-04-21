import { connectRouter } from 'connected-react-router';
import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import employeesReducer from './employees/EmployeesReducer';
import LoadingReducer from './loading/LoadingReducer';
import modalsReducer from './modals/ModalsReducer';
import reviewsReducer from './reviews/ReviewsReducer';
import UserReducer from './user/UserReducer';
import {reducer as toastrReducer} from 'react-redux-toastr'

export default function rootReducer(history) {
  const reducerMap = {
    router: connectRouter(history),
    user: UserReducer,
    loading: LoadingReducer,
    employees: employeesReducer,
    form: formReducer,
    modals: modalsReducer,
    reviews: reviewsReducer,
    toastr: toastrReducer
  };

  return combineReducers(reducerMap);
}
