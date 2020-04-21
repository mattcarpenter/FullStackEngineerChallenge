import { Button, Grid } from '@material-ui/core';
import { replace } from 'connected-react-router';
import React from 'react';
import { useDispatch } from 'react-redux';
import * as routes from '../../constants/Routes';

export default function EmployeeApp() {
  const dispatch = useDispatch();

  return (
    <Grid
      container
      spacing={0}
      direction="column"
      alignItems="center"
      justify="center"
      style={{ minHeight: '100vh' }}
    >
      <div>
        <div>
          Not implemented
        </div>
        <div>
          <Button variant="outlined" color="primary" onClick={() => { dispatch(replace(routes.LOGOUT)) }}>
            Log Out
          </Button>
        </div>
      </div>
    </Grid>
  )
}
