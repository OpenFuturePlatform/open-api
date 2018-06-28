import React from 'react';
import { EntityRemove } from './EntityRemove';
import { Divider } from 'semantic-ui-react';

export const ShareHolderRemove = ({ onSubmit }) => (
  <EntityRemove onSubmit={onSubmit} header="Remove Share Holder">
    <div>
      You are removing Share Holder. Are you sure?
      <Divider />
      <span>PS: Please be patient this may take a while...</span>
    </div>
  </EntityRemove>
);
