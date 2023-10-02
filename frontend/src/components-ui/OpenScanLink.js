import React from "react";

export const OpenScanLink = ({children}) => (
  <a href={`https://api.openfuture.io/widget/trx/address/${children}`} target="_blank" rel="noopener noreferrer">
    {children}
  </a>
);

