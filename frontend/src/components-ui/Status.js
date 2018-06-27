import React from 'react';
import { Icon } from 'semantic-ui-react';

export const Status = ({ value }) =>
  value ? (
    <div>
      <Icon name="check" />
      Active
    </div>
  ) : (
    <div>
      <Icon name="remove circle" />
      Inactive
    </div>
  );
