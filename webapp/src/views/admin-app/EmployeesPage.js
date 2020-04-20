import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getAllEmployees } from '../../stores/employees/EmployeesActions';
import { CREATE_EMPLOYEE_MODAL } from '../../constants/Modals';
import { Grid, Button, withWidth } from '@material-ui/core';
import EmployeesTable from '../../components/EmployeesTable';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import { makeStyles } from '@material-ui/core/styles';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { EmployeeDetailsFormCreate } from '../../components/EmployeeDetailsForm/index';
import { submit } from 'redux-form'
import { showModal, hideModal } from '../../stores/modals/ModalsActions';
import { push } from 'connected-react-router';

const useStyles = makeStyles((theme) => ({
  actions: {
      marginBottom: theme.spacing(2)
  },
  addIcon: {
    marginRight: theme.spacing(1)
  }
}));

function EmployeesPage(props) {
  const dispatch = useDispatch();
  const classes = useStyles();
  const showCreateEmployeeModal = !!useSelector(state => state.modals[CREATE_EMPLOYEE_MODAL]);
  const fullScreen = /xs/.test(props.width);

  function handleEmployeeClick(employeeId) {
    dispatch(push('/employees/' + employeeId));
  }

  dispatch(getAllEmployees());

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
    </div>
  );
}

export default withWidth()(EmployeesPage);
