import React from "react";

export const OpenScanLink = ({children}) => (
  <a href={`http://localhost:8080/widget/trx/address/${children}`} target="_blank" rel="noopener noreferrer">
    {children}
  </a>
);

