import { Button, Grid, withWidth } from '@material-ui/core';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { makeStyles } from '@material-ui/core/styles';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import { push } from 'connected-react-router';
import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { submit } from 'redux-form';
import { EmployeeDetailsFormCreate } from '../../components/EmployeeDetailsForm/index';
import EmployeesTable from '../../components/EmployeesTable';
import { CREATE_EMPLOYEE_MODAL } from '../../constants/Modals';
import { getAllEmployees } from '../../stores/employees/EmployeesActions';
import { hideModal, showModal } from '../../stores/modals/ModalsActions';

export default withWidth()(EmployeesPage);

function EmployeesPage(props) {
  const dispatch = useDispatch();
  const classes = useStyles();
  const showCreateEmployeeModal = !!useSelector(state => state.modals[CREATE_EMPLOYEE_MODAL]);
  const fullScreen = /xs/.test(props.width);

  function handleEmployeeClick(employeeId) {
    dispatch(push('/employees/' + employeeId));
  }

  useEffect(() => {
    dispatch(getAllEmployees());
  }, [dispatch]);

  return (
    <div>
      <Grid container alignItems="flex-start" justify="flex-end" direction="row" className={classes.actions}>
        <Grid item>
        <Button variant="outlined" color="primary" onClick={() => { dispatch(showModal(CREATE_EMPLOYEE_MODAL)) }}>
          <PersonAddIcon className={classes.addIcon} /> Create Employee
        </Button>
        </Grid>
      </Grid>
      <EmployeesTable onEmployeeClick={handleEmployeeClick} />
      { CreateEmployeeDialog() }
    </div>
  );

  function CreateEmployeeDialog() {
    return (
      <Dialog
        fullScreen={fullScreen}
        open={showCreateEmployeeModal}
        onClose={() => { dispatch(hideModal(CREATE_EMPLOYEE_MODAL)) }}
        aria-labelledby="form-dialog-title">
          <DialogTitle id="form-dialog-title">Create Employee</DialogTitle>
          <DialogContent>
            <EmployeeDetailsFormCreate />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => { dispatch(hideModal(CREATE_EMPLOYEE_MODAL)) }} color="primary">
              Cancel
            </Button>
            <Button onClick={() => dispatch(submit('createEmployeeForm')) } color="primary">
              Create
            </Button>
          </DialogActions>
      </Dialog>
    );
  }
}

function useStyles() {
  return makeStyles((theme) => ({
    actions: {
        marginBottom: theme.spacing(2)
    },
    addIcon: {
      marginRight: theme.spacing(1)
    }
  }))();
}
