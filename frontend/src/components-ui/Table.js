import React from 'react';
import ReactTable from 'react-table';

export const Table = ({ data, columns, noDataText }) => (
  <ReactTable
    data={data}
    columns={columns}
    className="-striped"
    showPagination={false}
    resizable={false}
    minRows={1}
    pageSize={data.length}
    noDataText={noDataText}
  />
);
