import { Checkbox, FormControlLabel, Grid, TextField, withWidth } from '@material-ui/core';
import React from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { submitCreate, submitUpdate } from './submit';

function EmployeeDetailsForm(props) { 
  const fullScreen = /xs/.test(props.width);

  const passwordLabel = props.form === 'updateEmployeeForm' ? 'New Password' : 'Password';
  return (
    <form onSubmit={props.handleSubmit} style={{ width: fullScreen ? 'auto' : 500 }}>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <Field label="First Name" name="firstName" component={renderTextField} />
        </Grid>
        <Grid item xs={12}>
          <Field label="Last Name" name="lastName" component={renderTextField} />
        </Grid>
        <Grid item xs={12}>
          <Field label="Email Address" name="emailAddress" component={renderTextField} />
        </Grid>
        <Grid item xs={12}>
          <Field label={passwordLabel} name="password" component={renderPasswordField} />
        </Grid>
        <Grid item xs={12}>
          <Field label="Confirm Password" name="confirmPassword" component={renderPasswordField} />
        </Grid>
        <Grid item xs={12}>
          <Field label="Is Admin" name="admin" component={renderCheckbox} />
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

function renderPasswordField({label, input, meta: { touched, invalid, error }, ...custom}) {
  return (
    <TextField
      fullWidth={true}
      label={label}
      type="password"
      placeholder={label}
      error={touched && invalid}
      helperText={touched && error}
      {...input}
      {...custom}
    />
  );
}

function renderCheckbox({ input, label }) {
  return (
    <div>
      <FormControlLabel
        control={
          <Checkbox
            checked={input.value ? true : false}
            onChange={input.onChange}
          />
        }
        label={label}
      />
    </div>
  );
}

function makeValidator(context) {
  return values => {
    const errors = {}
    
    if (!values.emailAddress) {
      errors.emailAddress = 'Required'
    } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.emailAddress)) {
      errors.emailAddress = 'Invalid email address'
    }

    ['firstName', 'lastName'].forEach(f => {
      if (!values[f]) {
        errors[f] = 'Required';
      }
    });

    if (context === 'create') {
      if (!values.password) {
        errors.password = 'Required';
      }
    }

    if (values.password && values.password !== values.confirmPassword) {
      errors.confirmPassword = 'Passwords do not match';
    }
  
    return errors
  }
}

export const EmployeeDetailsFormCreate = reduxForm({
  form: 'createEmployeeForm',
  onSubmit: submitCreate,
  validate: makeValidator('create')
})(withWidth()(EmployeeDetailsForm));

const InitializeFromStateUpdateForm = reduxForm({
  form: 'updateEmployeeForm',
  onSubmit: submitUpdate,
  validate: makeValidator('update')
})(EmployeeDetailsForm)

export const EmployeeDetailsFormUpdate = connect(
  state => ({
    initialValues: state.employees.current
  })
)(withWidth()(InitializeFromStateUpdateForm));
