import React, { useEffect } from 'react';
import { Grid, Typography, CircularProgress} from '@material-ui/core';
import { logout } from '../../stores/user/UserActions';
import { useDispatch } from 'react-redux';

export default function LogoutPage() {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(logout());
  }, [dispatch]);

  return (
    <Grid
      container
      spacing={0}
      direction="column"
      alignItems="center"
      justify="center"
      style={{ minHeight: '100vh' }}
    >
      <CircularProgress /> Logging out...
    </Grid>
  )
}