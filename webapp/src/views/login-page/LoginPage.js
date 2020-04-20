import React, { useState } from 'react';
import Container from '@material-ui/core/Container';
import { Card, CardContent, Typography, TextField, Grid, Button, withWidth } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles';
import { useDispatch, useSelector } from 'react-redux';
import { login } from '../../stores/user/UserActions';
import { CircularProgress } from '@material-ui/core'
import { LOGIN } from '../../stores/user/UserActions';

const useStyles = makeStyles((theme) => ({
  root: {
    [theme.breakpoints.down('xs')]: {
      padding: 0
    }
  },
  card: {
    [theme.breakpoints.up('sm')]: {
      marginTop: theme.spacing(8),
      padding: theme.spacing(2)
    }
  },
  title: {
    fontWeight: 'bold',
    marginBottom: theme.spacing(4)
  },
  button: {
    [theme.breakpoints.down('xs')]: {
      marginTop: theme.spacing(3),
      marginBottom: theme.spacing(2)
    }
  },
  spinner: {
    marginRight: theme.spacing(1)
  }
}));

function LoginPage(props) {
  const classes = useStyles();
  const isSmall = /xs/.test(props.width);
  const dispatch = useDispatch();
  const isLoggingIn = useSelector(state => !!state.loading[LOGIN]);

  const [EmailAddress, setEmailAddress] = useState('');
  const [password, setPassword] = useState('');
  
  return (
    <Container maxWidth="sm" className={classes.root}>
      <Card className={classes.card}>
        <CardContent>
          <Typography variant="h6" component="h1" align="center" color="textPrimary" className={classes.title}>
            Sign In
          </Typography>  
          <TextField
            id="username"
            label="Username"
            type="email"
            margin="normal"
            disabled={isLoggingIn}
            onChange={(e)=>setEmailAddress(e.target.value)}
            fullWidth
            autoFocus
            required />   
          <TextField
            id="username"
            label="Password"
            type="password"
            margin="normal"
            disabled={isLoggingIn}
            onChange={(e)=>setPassword(e.target.value)}
            fullWidth
            required />
          {!isSmall && <Grid container alignItems="flex-start" justify="flex-end" direction="row">
            <Button disableFocusRipple disableRipple style={{ textTransform: "none" }} variant="text" color="primary">Forgot password?</Button>
          </Grid>}
          <Grid container justify="center">
            <Button
              className={classes.button}
              fullWidth={isSmall}
              variant="outlined"
              color="primary"
              onClick={() => dispatch(login(EmailAddress, password))}
              style={{ textTransform: "none" }}>
                { isLoggingIn && <CircularProgress size={20} className={classes.spinner} />}
                Sign In
              </Button>
          </Grid>
          {isSmall && <Grid container justify="center">
            <Button disableFocusRipple disableRipple style={{ textTransform: "none" }} variant="text" color="primary">Forgot password?</Button>
          </Grid>}
        </CardContent>
      </Card>
    </Container>
  );
}

export default withWidth()(LoginPage);
