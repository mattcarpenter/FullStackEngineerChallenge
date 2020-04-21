import React from 'react';
import MUIDataTable from "mui-datatables";
import { useSelector } from 'react-redux';
import { Grid, withWidth } from '@material-ui/core';
import faker from 'faker';

export default withWidth()((props) => {
  const nameCell = /xs/.test(props.width) ? SimpleNameCell : NameCell;
  const data = useSelector(state => state.employees.employees)
    .map(e => [ { lastName: e.lastName, firstName: e.firstName, emailAddress: e.emailAddress, employeeId: e.id }, e.department, e.supervisor ]);
  const nameCol = {
    name: 'Name',
    options: {
      customBodyRender: function (parentProps) { return nameCell({ ...parentProps, onEmployeeClick: props.onEmployeeClick })}
    }
  };
  const columns = [nameCol, 'Department', 'Supervisor'];
  const options = {
    selectableRowsHeader: false,
    selectableRows: false
  };

  return (
    <MUIDataTable
      title="Employees"
      data={data}
      columns={columns}
      options={options}
    />
  );
});

function SimpleNameCell(props) {
  function handleClick(event) {
    event.preventDefault();
    (props.onEmployeeClick || function () {})(props.employeeId);
  }

  return (
    <div>
      <a href={`/employees/${props.employeeId}`} onClick={handleClick}>{ props.firstName } { props.lastName }</a>
    </div>
  )
}

function NameCell(props) {
  const avatarStyle = {
    width: 50,
    height: 50,
    borderRadius: 25
  };

  faker.seed((props.emailAddress || '').split('').reduce(function(a,b){a=((a<<5)-a)+b.charCodeAt(0);return a&a},0));

  function handleClick(event) {
    event.preventDefault();
    (props.onEmployeeClick || function () {})(props.employeeId);
  }

  return (
    <div>
      <Grid container spacing={3}>
        <Grid item>
          <img alt={ props.firstName + ' ' + props.lastName } src={faker.image.avatar()} style={avatarStyle} />
        </Grid>
        <Grid item>
          <div>
            { props.firstName} { props.lastName}
          </div>
          <div>
            <a href={`/employees/${props.employeeId}`} onClick={handleClick}>
              { props.emailAddress }
            </a>
          </div>
        </Grid>
      </Grid>
    </div>
  );
}

