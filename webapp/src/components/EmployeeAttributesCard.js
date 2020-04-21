import { Card, CardContent, Grid, makeStyles } from '@material-ui/core';
import React from 'react';

export default function EmployeeAttributesCard(props) {
  const classes = useStyles();
  return (
    <Card>
      <CardContent>
        <Grid container direction="row" spacing={3}>
          <Grid container item xs={6} className={classes.detail}>
            <Grid item className={classes.label}>First Name:</Grid>
            <Grid item>{ props.employee.firstName }</Grid>
          </Grid>
          <Grid container item xs={6} className={classes.detail}>
            <Grid item className={classes.label}>Last Name:</Grid>
            <Grid item>{ props.employee.lastName }</Grid>
          </Grid>
          <Grid container item xsm={6} className={classes.detail}>
            <Grid item className={classes.label}>Email Address:</Grid>
            <Grid item>{ props.employee.emailAddress }</Grid>
          </Grid>
          <Grid container item xs={6} className={classes.detail}>
            <Grid item className={classes.label}>Is Admin:</Grid>
            <Grid item>{ props.employee.admin ? 'yes' : 'no' }</Grid>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}

function useStyles() {
  return makeStyles((theme) => ({
    label: {
      width: 130,
      fontWeight: 'bold'
    },
    detail: {
      padding: theme.spacing(3)
    }
  }))();
}
