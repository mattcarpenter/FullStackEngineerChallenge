import { Button, IconButton } from '@material-ui/core';
import DeleteIcon from '@material-ui/icons/Delete';
import { push } from 'connected-react-router';
import MUIDataTable from "mui-datatables";
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { toastr } from 'react-redux-toastr';

export default function PerformanceReviewsTable(props) {
  const dispatch = useDispatch();
  const data = useSelector(state => state.employees.current.performanceReviews).map(r => [
    new Date(r.createdAt).toLocaleDateString(),
    r.memo,
    r.feedbackRequests.length || 'None',
    r.id
  ]);
  const actionsCol = {
    name: '',
    options: {
      customBodyRender: function (performanceReviewId) {
        return ActionsCell({
          onManageClick: () => {
            dispatch(push(`/performance-reviews/${performanceReviewId}`));
          }
        });
      }
    }
  };
  const columns = ['Created', 'Memo', 'Assigned Feedback Requests', actionsCol ];
  const options = {
    selectableRowsHeader: false,
    selectableRows: false
  }

  return (
    <MUIDataTable
      title="Performance Reviews"
      data={data}
      columns={columns}
      options={options}
    />
  );
}

function ActionsCell(props) {
  function handleClick() {
    toastr.warning('Not implemented');
  }

  return (
    <div style={{textAlign: 'right'}}>
      <Button
        variant="outlined"
        color="primary"
        onClick={handleClick}
      >
        Manage
      </Button>
      <IconButton
        variant="outlined"
        color="secondary"
        onClick={handleClick}
      >
        <DeleteIcon />
      </IconButton>
    </div>
  );
};
