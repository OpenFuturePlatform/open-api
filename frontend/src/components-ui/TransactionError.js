import React from 'react';
import { Divider } from 'semantic-ui-react';

export const TransactionError = ({ message }) => {
  if (!message) {
    return null;
  }

  return (
    <div>
      <Divider />
      <div style={{ color: 'red' }}>{message}</div>
    </div>
  );
};
