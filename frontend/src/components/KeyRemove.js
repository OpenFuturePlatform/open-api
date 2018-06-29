import React from 'react';
import { EntityRemove } from './EntityRemove';

export const KeyRemove = ({ onSubmit }) => (
  <EntityRemove onSubmit={onSubmit} header="Key Deactivation">
    <div>You are about to deactivate the key. Are you sure?</div>
  </EntityRemove>
);
