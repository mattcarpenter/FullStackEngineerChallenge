import { submitCreate } from './submit';
import { TextField, Grid, withWidth } from '@material-ui/core';
import React from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form'

function PerformanceReviewForm(props) { 
  const fullScreen = /xs/.test(props.width);

  return (
    <form onSubmit={props.handleSubmit} style={{ width: fullScreen ? 'auto' : 500 }}>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <Field label="Memo" name="memo" component={renderTextField} />
        </Grid>
      </Grid>
    </form>
  )
}

function renderTextField({label, input, meta: { touched, invalid, error }, ...custom}) {
  return (
    <TextField
      fullWidth={true}
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
})(withWidth()(PerformanceReviewForm));

const InitializeFromStateUpdateForm = reduxForm({
  form: 'updatePerformanceReviewForm',
  onSubmit: function noop() {},
  validate: validator
})(withWidth()(PerformanceReviewForm));

export const PerformanceReviewFormUpdate = connect(
  state => ({
    initialValues: state.performanceReviews.current
  })
)(InitializeFromStateUpdateForm);
