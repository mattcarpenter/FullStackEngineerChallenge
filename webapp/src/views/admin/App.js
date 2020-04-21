import AppBar from '@material-ui/core/AppBar';
import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import Hidden from '@material-ui/core/Hidden';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import MenuIcon from '@material-ui/icons/Menu';
import PeopleIcon from '@material-ui/icons/People';
import QuestionAnswerIcon from '@material-ui/icons/QuestionAnswer';
import { push, replace } from 'connected-react-router';
import React from 'react';
import { useDispatch } from 'react-redux';
import { toastr } from 'react-redux-toastr';
import { Switch, useLocation } from 'react-router-dom';
import LoginRequiredRoute from '../../components/LoginRequiredRoute';
import * as Routes from '../../constants/Routes';
import * as routes from '../../constants/Routes';
import EmployeeDetailsPage from './EmployeeDetailsPage';
import EmployeesPage from './EmployeesPage';

const drawerWidth = 240;

export default function App(props) {
  const { container } = props;
  const dispatch = useDispatch();
  const classes = useStyles();
  const theme = useTheme();
  const [mobileOpen, setMobileOpen] = React.useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  // there's currently nothing at the root. redirect to another route if necessary
  if (useLocation().pathname === '/') {
    dispatch(replace(routes.EMPLOYEES));
  }

  const drawer = (
    <div>
      <div className={classes.toolbar} />
      <Divider />
      <List>
        <ListItem button onClick={() => dispatch(push(routes.EMPLOYEES))}>
          <ListItemIcon><PeopleIcon /></ListItemIcon><ListItemText>Employees</ListItemText>
        </ListItem>
        <ListItem button onClick={() => toastr.warning('Not implemented')}>
          <ListItemIcon><QuestionAnswerIcon /></ListItemIcon><ListItemText>Reviews</ListItemText>
        </ListItem>
      </List>
      <Divider />
      <List>
      <ListItem button onClick={() => dispatch(replace(routes.LOGOUT))}>
          <ListItemIcon><ExitToAppIcon /></ListItemIcon><ListItemText>Log out</ListItemText>
        </ListItem>
      </List>
    </div>
  );

  return (
    <div className={classes.root}>
      <AppBar position="fixed" className={classes.appBar}>
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            className={classes.menuButton}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap>
            Employee Performance Reviews
          </Typography>
        </Toolbar>
      </AppBar>
      <nav className={classes.drawer} aria-label="mailbox folders">
        {/* The implementation can be swapped with js to avoid SEO duplication of links. */}
        <Hidden smUp implementation="css">
          <Drawer
            container={container}
            variant="temporary"
            anchor={theme.direction === 'rtl' ? 'right' : 'left'}
            open={mobileOpen}
            onClose={handleDrawerToggle}
            classes={{
              paper: classes.drawerPaper,
            }}
            ModalProps={{
              keepMounted: true, // Better open performance on mobile.
            }}
          >
            {drawer}
          </Drawer>
        </Hidden>
        <Hidden xsDown implementation="css">
          <Drawer
            classes={{
              paper: classes.drawerPaper,
            }}
            variant="permanent"
            open
          >
            {drawer}
          </Drawer>
        </Hidden>
      </nav>
      <main className={classes.content}>
        <div className={classes.toolbar} />
        <Switch>
          <LoginRequiredRoute path={Routes.EMPLOYEE_DETAILS} component={EmployeeDetailsPage} />
          <LoginRequiredRoute path={Routes.EMPLOYEES} component={EmployeesPage} />
        </Switch>
      </main>
    </div>
  );
}

function useStyles() {
  return makeStyles((theme) => ({
    root: {
      display: 'flex',
    },
    drawer: {
      [theme.breakpoints.up('sm')]: {
        width: drawerWidth,
        flexShrink: 0,
      },
    },
    appBar: {
      [theme.breakpoints.up('sm')]: {
        width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: drawerWidth,
      },
    },
    menuButton: {
      marginRight: theme.spacing(2),
      [theme.breakpoints.up('sm')]: {
        display: 'none',
      },
    },
    // necessary for content to be below app bar
    toolbar: theme.mixins.toolbar,
    drawerPaper: {
      width: drawerWidth,
    },
    content: {
      flexGrow: 1,
      padding: theme.spacing(3),
    },
  }))();
}
