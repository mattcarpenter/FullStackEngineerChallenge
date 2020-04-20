import React from 'react';
import { Card, CardContent, makeStyles, Grid } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
  label: {
    width: 130,
    fontWeight: 'bold'
  },
  detail: {
    padding: theme.spacing(3)
  }
}));

export default function EmployeeGeneralInfo(props) {
  const classes = useStyles();
  return (
    <Card>
      <CardContent>
        <Grid container direction="row" spacing={3}>
          <Grid container sm={6} className={classes.detail}>
            <Grid item className={classes.label}>First Name:</Grid>
            <Grid item>{ props.employee.firstName }</Grid>
          </Grid>
          <Grid container sm={6} className={classes.detail}>
            <Grid item className={classes.label}>Last Name:</Grid>
            <Grid item>{ props.employee.lastName }</Grid>
          </Grid>
          <Grid container sm={6} className={classes.detail}>
            <Grid item className={classes.label}>Email Address:</Grid>
            <Grid item>{ props.employee.emailAddress }</Grid>
          </Grid>
          <Grid container sm={6} className={classes.detail}>
            <Grid item className={classes.label}>Is Admin:</Grid>
            <Grid item>{ props.employee.isAdmin ? 'yes' : 'no' }</Grid>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}
