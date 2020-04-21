import { GET_REVIEW, GET_REVIEW_SUCCESS } from './ReviewsActions';

const initialState = {
  reviews: [],
  current: null
};

export default function reviewsReducer(state = initialState, action) {
  switch (action.type) {
    case GET_REVIEW:
      state.current = null;
      break;
    case GET_REVIEW_SUCCESS:
      state.current = action.payload;
      break;
    default:
      return state;
  }

  return state;
}
