import React from 'react';
import { reduxForm, Field } from 'redux-form'
import { TextField, FormControlLabel, Checkbox } from '@material-ui/core';
import { submitUpdate, submitCreate } from './submit';
import { connect } from 'react-redux';

const renderTextField = ({
  label,
  input,
  meta: { touched, invalid, error },
  ...custom
}) => (
  <TextField
    label={label}
    placeholder={label}
    error={touched && invalid}
    helperText={touched && error}
    {...input}
    {...custom}
  />
)

const renderPasswordField = ({
  label,
  input,
  meta: { touched, invalid, error },
  ...custom
}) => (
  <TextField
    label={label}
    type="password"
    placeholder={label}
    error={touched && invalid}
    helperText={touched && error}
    {...input}
    {...custom}
  />
)

const renderCheckbox = ({ input, label }) => (
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
)

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

function EmployeeDetailsForm(props) { 
  const passwordLabel = props.form === 'updateEmployeeForm' ? 'New Password' : 'Password';
  return (
    <form onSubmit={props.handleSubmit}>
      <div>
        <Field label="First Name" name="firstName" component={renderTextField} />
      </div>
      <div>
        <Field label="Last Name" name="lastName" component={renderTextField} />
      </div>
      <div>
        <Field label="Email Address" name="emailAddress" component={renderTextField} />
      </div>
      <div>
        <Field label={passwordLabel} name="password" component={renderPasswordField} />
      </div>
      <div>
        <Field label="Confirm Password" name="confirmPassword" component={renderPasswordField} />
      </div>
      <div>
        <Field label="Is Admin" name="isAdmin" component={renderCheckbox} />
      </div>
    </form>
  )
}

export const EmployeeDetailsFormCreate = reduxForm({
  form: 'createEmployeeForm',
  onSubmit: submitCreate,
  validate: makeValidator('create')
})(EmployeeDetailsForm)

const InitializeFromStateUpdateForm = reduxForm({
  form: 'updateEmployeeForm',
  onSubmit: submitUpdate,
  validate: makeValidator('update')
})(EmployeeDetailsForm)

export const EmployeeDetailsFormUpdate = connect(
  state => ({
    initialValues: state.employees.current
  })
)(InitializeFromStateUpdateForm);
