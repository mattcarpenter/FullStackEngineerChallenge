import { Button, Grid, Typography, withWidth } from '@material-ui/core';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { makeStyles } from '@material-ui/core/styles';
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/Edit';
import faker from 'faker';
import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { submit } from 'redux-form';
import EmployeeAttributesCard from '../../components/EmployeeAttributesCard';
import { EmployeeDetailsFormUpdate } from '../../components/EmployeeDetailsForm/index';
import { PerformanceReviewFormCreate } from '../../components/PerformanceReviewForm/index';
import PerformanceReviewsTable from '../../components/PerformanceReviewsTable';
import { CREATE_PERFORMANCE_REVIEW_MODAL, UPDATE_EMPLOYEE_MODAL } from '../../constants/Modals';
import { getEmployee } from '../../stores/employees/EmployeesActions';
import { hideModal, showModal } from '../../stores/modals/ModalsActions';

export default withWidth()(EmployeeDetailsPage);

function EmployeeDetailsPage(props) {
  const dispatch = useDispatch();
  const classes = useStyles();
  const fullScreen = /xs/.test(props.width);
  const { employeeId } = useParams();
  const employee = useSelector(state => state.employees.current);
  const showUpdateEmployeeModal = !!useSelector(state => state.modals[UPDATE_EMPLOYEE_MODAL]);
  const showCreatePerformanceReviewModal = !!useSelector(state => state.modals[CREATE_PERFORMANCE_REVIEW_MODAL]);

  useEffect(() => {
    dispatch(getEmployee(employeeId));
  }, [dispatch, employeeId]);

  if (employee) {
    faker.seed((employee?.emailAddress || '').split("").reduce(function(a,b){a=((a<<5)-a)+b.charCodeAt(0);return a&a},0));
  }

  return (
    <div>
      <Typography variant="h6" component="h1" color="textPrimary" className={classes.title}>
        Employee
      </Typography>
      { employee && 
        <div>
          { Header() }
          <EmployeeAttributesCard employee={employee} />
          <Grid container alignItems="flex-start" justify="flex-end" direction="row" className={classes.performanceReviewSectionHeading}>
            <Grid item>
            <Button variant="outlined" color="primary" onClick={() => { dispatch(showModal(CREATE_PERFORMANCE_REVIEW_MODAL)) }}>
              <AddIcon className={classes.addIcon} /> New Performance Review
            </Button>
            </Grid>
          </Grid>
          <PerformanceReviewsTable employeeId={employeeId} />
          { UpdateEmployeeDialog() }
          { CreatePerformanceReviewDialog() }
        </div>
      }
    </div>
  );

  function Header() {
    return (
      <Grid container className={classes.basic}>
        <Grid item>
          <img className={classes.avatar} src={ faker.image.avatar() } alt={`${employee.firstName} ${employee.lastName}`} />
        </Grid>
        <Grid item>
          <div className={classes.name}>
            { `${employee.firstName} ${employee.lastName}` }
          </div>
          <div>
            { employee.emailAddress }
          </div>
          <div>
            <Button
              variant="outlined"
              color="primary"
              className={classes.editButton}
              onClick={() => dispatch(showModal(UPDATE_EMPLOYEE_MODAL))}
            >
              <EditIcon className={classes.editIcon} /> Edit
            </Button>
          </div>
        </Grid>
      </Grid>
    );
  }

  function UpdateEmployeeDialog() {
    return (
      <Dialog
        fullScreen={fullScreen}
        open={showUpdateEmployeeModal}
        onClose={() => { dispatch(hideModal(UPDATE_EMPLOYEE_MODAL)) }}
        aria-labelledby="form-dialog-title">
          <DialogTitle id="form-dialog-title">Update Employee</DialogTitle>
          <DialogContent>
            <EmployeeDetailsFormUpdate />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => { dispatch(hideModal(UPDATE_EMPLOYEE_MODAL)) }} color="primary">
              Cancel
            </Button>
            <Button onClick={() => dispatch(submit('updateEmployeeForm')) } color="primary">
              Update
            </Button>
          </DialogActions>
        </Dialog>
    );
  }

  function CreatePerformanceReviewDialog() {
    return (
      <Dialog
        fullScreen={fullScreen}
        open={showCreatePerformanceReviewModal}
        onClose={() => { dispatch(hideModal(CREATE_PERFORMANCE_REVIEW_MODAL)) }}
        aria-labelledby="form-dialog-title">
          <DialogTitle id="form-dialog-title">Create Performance Review</DialogTitle>
          <DialogContent>
            <PerformanceReviewFormCreate initialValues={{ employeeId: employee.id}} />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => { dispatch(hideModal(CREATE_PERFORMANCE_REVIEW_MODAL)) }} color="primary">
              Cancel
            </Button>
            <Button onClick={() => dispatch(submit('createPerformanceReviewForm')) } color="primary">
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
    },
    editIcon: {
      marginRight: theme.spacing(1)
    },
    performanceReviewSectionHeading: {
      marginTop: theme.spacing(3),
      marginBottom: theme.spacing(3)
    },
    title: {
      marginBottom: theme.spacing(6)
    },
    avatar: {
      width: 100,
      height: 100,
      borderRadius: 10,
      marginRight: theme.spacing(3)
    },
    name: {
      fontSize: '1.2em',
      fontWeight: 'bold'
    },
    basic: {
      marginBottom: theme.spacing(3)
    },
    editButton: {
      marginTop: theme.spacing(2)
    }
  }))();
}
