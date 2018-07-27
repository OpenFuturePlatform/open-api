import React from 'react';
import { Divider } from 'semantic-ui-react';

export const TransactionError = ({ message, children }) => {
  if (!message && !children) {
    return null;
  }

  return (
    <div>
      <Divider />
      <div style={{ color: 'red' }}>{message || children}</div>
    </div>
  );
};
