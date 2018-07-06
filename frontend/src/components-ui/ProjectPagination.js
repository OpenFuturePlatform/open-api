import React from 'react';
import { Pagination } from 'semantic-ui-react';

export const ProjectPagination = ({ totalCount, limit = 10, onChange, size }) => {
  const totalPages = Math.ceil(totalCount / limit);

  if (totalPages <= 1) {
    return null;
  }

  const onChangeHandle = (e, { activePage }) => {
    const offset = (activePage - 1) * limit;
    onChange(offset, limit);
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center' }}>
      <Pagination defaultActivePage={1} totalPages={totalPages} onPageChange={onChangeHandle} size={size} />
    </div>
  );
};
