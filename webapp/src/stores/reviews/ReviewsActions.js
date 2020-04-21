import axios from 'axios';

export const GET_REVIEW = 'EmployeesActions.GET';
export const GET_REVIEW_REQUEST = 'EmployeesActions.GET_REQUEST';
export const GET_REVIEW_SUCCESS = 'EmployeesActions.GET_SUCCESS';
export const GET_REVIEW_FAILURE = 'EmployeesActions.GET_FAILURE';

export function getPerformanceReview(performanceReviewId) {
  return dispatch => {
    dispatch({ type: GET_REVIEW });
    axios
      .get(`/api/v1/performance-reviews/${performanceReviewId}`)
      .then(({data}) => {
        dispatch({ type: GET_REVIEW_SUCCESS, payload: data });
      })
      .catch(({data}) => {
        dispatch({ type: GET_REVIEW_FAILURE, payload: data, error: true });
      });
  }
}
