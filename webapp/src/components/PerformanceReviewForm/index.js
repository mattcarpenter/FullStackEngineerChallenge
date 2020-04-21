import { submitCreate } from './submit';
import { TextField } from '@material-ui/core';
import React from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form'

function PerformanceReviewForm(props) { 
  return (
    <form onSubmit={props.handleSubmit}>
      <div>
        <Field label="Memo" name="memo" component={renderTextField} />
      </div>
    </form>
  )
}

function renderTextField({label, input, meta: { touched, invalid, error }, ...custom}) {
  return (
    <TextField
      label={label}
      placeholder={label}
      error={touched && invalid}
      helperText={touched && error}
      {...input}
      {...custom}
    />
  );
}

function validator(values) {
  return !values.memo ? { memo: 'Required' } : {};
}

export const PerformanceReviewFormCreate = reduxForm({
  form: 'createPerformanceReviewForm',
  onSubmit: submitCreate,
  validate: validator
})(PerformanceReviewForm)

const InitializeFromStateUpdateForm = reduxForm({
  form: 'updatePerformanceReviewForm',
  onSubmit: function noop() {},
  validate: validator
})(PerformanceReviewForm)

export const PerformanceReviewFormUpdate = connect(
  state => ({
    initialValues: state.performanceReviews.current
  })
)(InitializeFromStateUpdateForm);
