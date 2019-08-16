import React from 'react';

export const EtherscanLink = ({children}) => (
  <a href={`https://etherscan.io/address/${children}`} target="_blank" rel="noopener noreferrer">
    {children}
  </a>
);
