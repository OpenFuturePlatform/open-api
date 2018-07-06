import React from 'react';
import { EtherscanLink } from '../components-ui/EtherscanLink';
import styled from 'styled-components';

export const TransactionParam = styled.div`
  display: inline-block;
  padding-right: 20px;
`;

const renderAddress = (key, address) => {
  if (!address) {
    return null;
  }
  return (
    <TransactionParam>
      {key}: <EtherscanLink>{address}</EtherscanLink>
    </TransactionParam>
  );
};

export const TransactionEvent = ({
  event: {
    type,
    userAddress,
    partnerShare,
    activated,
    amount,
    toAddress,
    customerAddress,
    transactionAmount,
    scaffoldTransactionIndex
  }
}) => (
  <div>
    <i>{type.replace(/_/g, ' ')}</i>
    <div>
      {renderAddress('User Address', userAddress)}
      {renderAddress('to Address', toAddress)}
      {renderAddress('Customer Address', customerAddress)}
      {activated ? <TransactionParam>Activated: {activated}</TransactionParam> : null}
      {partnerShare ? <TransactionParam>Partner Share: {partnerShare}%</TransactionParam> : null}
      {amount ? <TransactionParam>Amount: {amount}%</TransactionParam> : null}
      {transactionAmount ? <TransactionParam>Transaction Amount: {transactionAmount}%</TransactionParam> : null}
      {scaffoldTransactionIndex ? (
        <TransactionParam>Transaction Index: {scaffoldTransactionIndex}%</TransactionParam>
      ) : null}
    </div>
  </div>
);
