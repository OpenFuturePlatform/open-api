import React from 'react';
import { Message } from 'semantic-ui-react';
import '../css/error-message.css';

export const ErrorMessage = ({ errorList, isVisible }) => {
  if (!errorList.length || !isVisible) {
    return null;
  }

  return <Message error list={errorList} />;
};
