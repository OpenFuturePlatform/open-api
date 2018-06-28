import React from 'react';
import { Icon, Button } from 'semantic-ui-react';

export const ShowAddEditButton = ({ editType, onShow }) => {
  if (editType) {
    return <Icon link name="edit" size="large" onClick={onShow} />;
  }
  return (
    <Button fluid attached="top" onClick={onShow}>
      Add Share
    </Button>
  );
};
